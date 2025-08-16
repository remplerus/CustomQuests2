package com.vincentmet.customquests.network.messages.sync;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.api.ProgressHelper;
import com.vincentmet.customquests.api.QuestHelper;
import com.vincentmet.customquests.api.QuestingStorage;
import com.vincentmet.customquests.api.ServerUtils;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageUpdateSinglePlayerQuestProgress implements ICQPacket {
	public UUID uuid;
	public int questId;
	public JsonObject jsonObject;

	public MessageUpdateSinglePlayerQuestProgress(){}

	private MessageUpdateSinglePlayerQuestProgress(UUID uuid, int questId, JsonObject json){
		this.uuid = uuid;
		this.questId = questId;
		this.jsonObject = json;
	}

	public MessageUpdateSinglePlayerQuestProgress(UUID uuid, int questId){
		this.uuid = uuid;
		this.questId = questId;
		if(ProgressHelper.doesPlayerExist(uuid)){
			jsonObject = QuestingStorage.getSidedPlayersMap().get(uuid.toString()).getIndividualProgress().get(questId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSinglePlayerAtAllClients(uuid);//todo is this class and the call on this line still needed? if so, probably need to rename and move to correct folder
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageUpdateSinglePlayerQuestProgress packet = (MessageUpdateSinglePlayerQuestProgress) clazz;
		if(ProgressHelper.doesPlayerExist(packet.uuid) && QuestHelper.doesQuestExist(packet.questId) && packet.jsonObject != null){
			buffer.writeUUID(packet.uuid);
			buffer.writeInt(packet.questId);
			buffer.writeUtf(QuestingStorage.getSidedPlayersMap().get(packet.uuid.toString()).getIndividualProgress().get(packet.questId).getJson().toString());
		}
	}
	
	public MessageUpdateSinglePlayerQuestProgress decode(FriendlyByteBuf buffer) {
		if(buffer.isReadable(22)){//16 for uuid, 4 for int, 2+ for json
			return new MessageUpdateSinglePlayerQuestProgress(buffer.readUUID(), buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateSinglePlayerQuestProgress message = (MessageUpdateSinglePlayerQuestProgress) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Players.Progress.updateSingleQuestingPlayer(message.uuid, message.questId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}