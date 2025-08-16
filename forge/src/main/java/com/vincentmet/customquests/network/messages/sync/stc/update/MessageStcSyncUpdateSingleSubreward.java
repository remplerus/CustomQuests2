package com.vincentmet.customquests.network.messages.sync.stc.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.api.QuestHelper;
import com.vincentmet.customquests.api.QuestingStorage;
import com.vincentmet.customquests.api.ServerUtils;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncUpdateSingleSubreward implements ICQPacket {
	private int questId;
	private int rewardId;
	private int subrewardId;
	private JsonObject jsonObject;

	public MessageStcSyncUpdateSingleSubreward(){}

	private MessageStcSyncUpdateSingleSubreward(int questId, int rewardId, int subrewardId, JsonObject jsonObject){
		this.questId = questId;
		this.rewardId = rewardId;
		this.subrewardId = subrewardId;
		this.jsonObject = jsonObject;
	}

	public MessageStcSyncUpdateSingleSubreward(int questId, int rewardId, int subrewardId){
		this.questId = questId;
		this.rewardId = rewardId;
		this.subrewardId = subrewardId;
		if(QuestHelper.doesSubrewardExist(questId, rewardId, subrewardId)){
			jsonObject = QuestingStorage.getSidedQuestsMap().get(questId).getRewards().get(rewardId).getSubRewards().get(subrewardId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSingleSubrewardAtAllClients(questId, rewardId, subrewardId);
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageStcSyncUpdateSingleSubreward packet = (MessageStcSyncUpdateSingleSubreward) clazz;
		if(QuestHelper.doesSubrewardExist(packet.questId, packet.rewardId, packet.subrewardId) && packet.jsonObject != null){
			buffer.writeInt(packet.questId);
			buffer.writeInt(packet.rewardId);
			buffer.writeInt(packet.subrewardId);
			buffer.writeUtf(packet.jsonObject.toString());
		}
	}
	
	public MessageStcSyncUpdateSingleSubreward decode(FriendlyByteBuf buffer) {
		if(buffer.isReadable(14)){//4 for int, 4 for int, 4 for int, 2+ for json
			return new MessageStcSyncUpdateSingleSubreward(buffer.readInt(), buffer.readInt(), buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageStcSyncUpdateSingleSubreward message = (MessageStcSyncUpdateSingleSubreward) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Quests.updateSingleSubreward(message.questId, message.rewardId, message.subrewardId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}