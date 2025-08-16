package com.vincentmet.customquests.network.messages.sync.stc.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.api.QuestHelper;
import com.vincentmet.customquests.api.QuestingStorage;
import com.vincentmet.customquests.api.ServerUtils;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncUpdateSingleQuest implements ICQPacket {
	private int questId;
	private JsonObject jsonObject;

	public MessageStcSyncUpdateSingleQuest(){}

	private MessageStcSyncUpdateSingleQuest(int questId, JsonObject jsonObject){
		this.questId = questId;
		this.jsonObject = jsonObject;
	}

	public MessageStcSyncUpdateSingleQuest(int questId){
		this.questId = questId;
		if(QuestHelper.doesQuestExist(questId)){
			jsonObject = QuestingStorage.getSidedQuestsMap().get(questId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSingleQuestAtAllClients(questId);
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageStcSyncUpdateSingleQuest packet = (MessageStcSyncUpdateSingleQuest) clazz;
		if(QuestHelper.doesQuestExist(packet.questId) && packet.jsonObject != null){
			buffer.writeInt(packet.questId);
			buffer.writeUtf(packet.jsonObject.toString());
		}
	}
	
	public MessageStcSyncUpdateSingleQuest decode(FriendlyByteBuf buffer) {
		if(buffer.isReadable(6)){//4 for int, 2+ for json
			return new MessageStcSyncUpdateSingleQuest(buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageStcSyncUpdateSingleQuest message = (MessageStcSyncUpdateSingleQuest) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Quests.updateSingleQuest(message.questId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
				CustomQuestsLogger.info("Quest " + message.questId + " synced!");
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}
