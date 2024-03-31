package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearAllParties implements ICQPacket {
    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {}

    public MessageStcSyncTempClearAllParties decode(FriendlyByteBuf buffer) {
        return new MessageStcSyncTempClearAllParties();
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(EditorClientProcessor.Clear.Parties::clearAllParties).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}