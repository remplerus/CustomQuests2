package com.vincentmet.customquests.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public interface ICQPacket {
    <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer);

    <T extends ICQPacket> T decode(FriendlyByteBuf buffer);

    <T extends ICQPacket> void handle(final T clazz, Supplier<NetworkEvent.Context> ctx);
}