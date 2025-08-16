package com.vincentmet.customquests.network.messages.command;

import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class MessageHand implements ICQPacket {
	private ItemStack stack;

	public MessageHand(){}
	public MessageHand(ItemStack stack){
		this.stack = stack;
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageHand packet = (MessageHand) clazz;
		buffer.writeItemStack(packet.stack, false);
	}

	public MessageHand decode(FriendlyByteBuf buffer) {
		return new MessageHand(buffer.readItem());
	}
	
	public void handle(final MessageHand message, Supplier<NetworkEvent.Context> ctx) {
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageHand message = (MessageHand) clazz;
		ctx.get().enqueueWork(() -> {
			if (message.stack.getTag()!=null){
				GLFW.glfwSetClipboardString(Minecraft.getInstance().getWindow().getWindow(), "{\"item\":\""+ForgeRegistries.ITEMS.getKey(message.stack.getItem())+"\",\"count\":"+stack.getCount()+",\"nbt\":\""+message.stack.getTag().toString()+"\"}");
			}else{
				GLFW.glfwSetClipboardString(Minecraft.getInstance().getWindow().getWindow(), "{\"item\":\""+ForgeRegistries.ITEMS.getKey(message.stack.getItem())+"\",\"count\":"+message.stack.getCount()+",\"nbt\":null}");
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
