package com.vincentmet.customquests.network.messages.command;

import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDiscord implements ICQPacket {

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {}

	public MessageDiscord decode(FriendlyByteBuf buffer) {
		return new MessageDiscord();
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> Util.getPlatform().openUri("https://discord.gg/TmgVdAb"));
		ctx.get().setPacketHandled(true);
	}
}
