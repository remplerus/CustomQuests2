package com.vincentmet.customquests.messages;

import com.vincentmet.customquests.api.CombinedProgressHelper;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageCheckboxClick implements ICQPacket {
	public int questId;
	public int taskId;
	public int subtaskId;
	
	public MessageCheckboxClick(){}

	public MessageCheckboxClick(int questId, int taskId, int subtaskId){
		this.questId = questId;
		this.taskId = taskId;
		this.subtaskId = subtaskId;
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageCheckboxClick packet = (MessageCheckboxClick) clazz;
		buffer.writeInt(packet.questId);
		buffer.writeInt(packet.taskId);
		buffer.writeInt(packet.subtaskId);
	}
	
	public MessageCheckboxClick decode(FriendlyByteBuf buffer) {
		if(buffer.readableBytes() >= 12){
			int questId = buffer.readInt();
			int taskId = buffer.readInt();
			int subtaskId = buffer.readInt();
			return new MessageCheckboxClick(questId, taskId, subtaskId);
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageCheckboxClick message = (MessageCheckboxClick) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				CombinedProgressHelper.setValue(ctx.get().getSender().getUUID(), message.questId, message.taskId, message.subtaskId, 1);
				CombinedProgressHelper.completeSubtask(ctx.get().getSender().getUUID(), message.questId, message.taskId, message.subtaskId);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
