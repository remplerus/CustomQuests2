package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteAllQuests implements ICQPacket {
    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {}
    
    public MessageStcSyncDeleteAllQuests decode(FriendlyByteBuf buffer) {
        return new MessageStcSyncDeleteAllQuests();
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            EditorClientProcessor.Delete.deleteAllQuests();
            ClientUtils.reloadMainGuiIfOpen();
            ClientUtils.reloadEditorIfOpen();
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}