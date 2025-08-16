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

public class MessageStcSyncUpdateSingleTask implements ICQPacket {
	private int questId;
	private int taskId;
	private JsonObject jsonObject;

	public MessageStcSyncUpdateSingleTask(){}

	private MessageStcSyncUpdateSingleTask(int questId, int taskId, JsonObject jsonObject){
		this.questId = questId;
		this.taskId = taskId;
		this.jsonObject = jsonObject;
	}

	public MessageStcSyncUpdateSingleTask(int questId, int taskId){
		this.questId = questId;
		this.taskId = taskId;
		if(QuestHelper.doesTaskExist(questId, taskId)){
			jsonObject = QuestingStorage.getSidedQuestsMap().get(questId).getTasks().get(taskId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSingleTaskAtAllClients(questId, taskId);
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageStcSyncUpdateSingleTask packet = (MessageStcSyncUpdateSingleTask) clazz;
		if(QuestHelper.doesTaskExist(packet.questId, packet.taskId) && packet.jsonObject != null){
			buffer.writeInt(packet.questId);
			buffer.writeInt(packet.taskId);
			buffer.writeUtf(packet.jsonObject.toString());
		}
	}
	
	public MessageStcSyncUpdateSingleTask decode(FriendlyByteBuf buffer) {
		if(buffer.isReadable(10)){//4 for int, 4 for int, 2+ for json
			return new MessageStcSyncUpdateSingleTask(buffer.readInt(), buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageStcSyncUpdateSingleTask message = (MessageStcSyncUpdateSingleTask) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Quests.updateSingleTask(message.questId, message.taskId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}