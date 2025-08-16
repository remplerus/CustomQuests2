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

public class MessageStcSyncUpdateSingleSubtask implements ICQPacket {
	private int questId;
	private int taskId;
	private int subtaskId;
	private JsonObject jsonObject;

	public MessageStcSyncUpdateSingleSubtask(){}

	private MessageStcSyncUpdateSingleSubtask(int questId, int taskId, int subtaskId, JsonObject jsonObject){
		this.questId = questId;
		this.taskId = taskId;
		this.subtaskId = subtaskId;
		this.jsonObject = jsonObject;
	}

	public MessageStcSyncUpdateSingleSubtask(int questId, int taskId, int subtaskId){
		this.questId = questId;
		this.taskId = taskId;
		this.subtaskId = subtaskId;
		if(QuestHelper.doesSubtaskExist(questId, taskId, subtaskId)){
			jsonObject = QuestingStorage.getSidedQuestsMap().get(questId).getTasks().get(taskId).getSubtasks().get(subtaskId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSingleSubtaskAtAllClients(questId, taskId, subtaskId);
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageStcSyncUpdateSingleSubtask packet = (MessageStcSyncUpdateSingleSubtask) clazz;
		if(QuestHelper.doesSubtaskExist(packet.questId, packet.taskId, packet.subtaskId) && packet.jsonObject != null){
			buffer.writeInt(packet.questId);
			buffer.writeInt(packet.taskId);
			buffer.writeInt(packet.subtaskId);
			buffer.writeUtf(packet.jsonObject.toString());
		}
	}
	
	public MessageStcSyncUpdateSingleSubtask decode(FriendlyByteBuf buffer) {
		if(buffer.isReadable(14)){//4 for int, 4 for int, 4 for int, 2+ for json
			return new MessageStcSyncUpdateSingleSubtask(buffer.readInt(), buffer.readInt(), buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageStcSyncUpdateSingleSubtask message = (MessageStcSyncUpdateSingleSubtask) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Quests.updateSingleSubtask(message.questId, message.taskId, message.subtaskId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}
