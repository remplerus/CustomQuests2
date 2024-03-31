package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearAllPlayers implements ICQPacket {
    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {}
    
    public MessageStcSyncTempClearAllPlayers decode(FriendlyByteBuf buffer) {
        return new MessageStcSyncTempClearAllPlayers();
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(EditorClientProcessor.Clear.Players::clearAllPlayers).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}