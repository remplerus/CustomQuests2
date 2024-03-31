package com.vincentmet.customquests.network.messages.sync.stc.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.api.*;
import com.vincentmet.customquests.network.messages.ICQPacket;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncUpdateSingleReward implements ICQPacket {
	private int questId;
	private int rewardId;
	private JsonObject jsonObject;

	public MessageStcSyncUpdateSingleReward(){}

	private MessageStcSyncUpdateSingleReward(int questId, int rewardId, JsonObject jsonObject){
		this.questId = questId;
		this.rewardId = rewardId;
		this.jsonObject = jsonObject;
	}

	public MessageStcSyncUpdateSingleReward(int questId, int rewardId){
		this.questId = questId;
		this.rewardId = rewardId;
		if(QuestHelper.doesRewardExist(questId, rewardId)){
			jsonObject = QuestingStorage.getSidedQuestsMap().get(questId).getRewards().get(rewardId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSingleRewardAtAllClients(questId, rewardId);
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageStcSyncUpdateSingleReward packet = (MessageStcSyncUpdateSingleReward) clazz;
		if(QuestHelper.doesRewardExist(packet.questId, packet.rewardId) && packet.jsonObject != null){
			buffer.writeInt(packet.questId);
			buffer.writeInt(packet.rewardId);
			buffer.writeUtf(packet.jsonObject.toString());
		}
	}
	
	public MessageStcSyncUpdateSingleReward decode(FriendlyByteBuf buffer) {
		if(buffer.isReadable(10)){//4 for int, 4 for int, 2+ for json
			return new MessageStcSyncUpdateSingleReward(buffer.readInt(), buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageStcSyncUpdateSingleReward message = (MessageStcSyncUpdateSingleReward) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Quests.updateSingleReward(message.questId, message.rewardId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}
