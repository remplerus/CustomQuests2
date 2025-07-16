package com.vincentmet.customquests.network.messages.button;

import com.vincentmet.customquests.api.ProgressHelper;
import com.vincentmet.customquests.network.messages.ICQPacket;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateDelivery;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageTaskButton implements ICQPacket {
	public int questId;
	public int taskId;
	
	public MessageTaskButton(){}

	public MessageTaskButton(int questId, int taskId){
		this.questId = questId;
		this.taskId = taskId;
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageTaskButton packet = (MessageTaskButton) clazz;
		buffer.writeInt(packet.questId);
		buffer.writeInt(packet.taskId);
	}
	
	public MessageTaskButton decode(FriendlyByteBuf buffer) {
		if(buffer.readableBytes() >= 8){
			int questId = buffer.readInt();
			int taskId = buffer.readInt();
			return new MessageTaskButton(questId, taskId);
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageTaskButton message = (MessageTaskButton) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				ProgressHelper.executeTaskCallback(Objects.requireNonNull(ctx.get().getSender()), message.questId, message.taskId);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
