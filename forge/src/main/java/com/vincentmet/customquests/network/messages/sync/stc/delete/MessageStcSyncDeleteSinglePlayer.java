package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MessageStcSyncDeleteSinglePlayer implements ICQPacket {
    private UUID uuid;

    public MessageStcSyncDeleteSinglePlayer(){}

    public MessageStcSyncDeleteSinglePlayer(UUID uuid){
        this.uuid = uuid;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSinglePlayer packet = (MessageStcSyncDeleteSinglePlayer) clazz;
        buffer.writeUUID(packet.uuid);
    }
    
    public MessageStcSyncDeleteSinglePlayer decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(16)){//uuid == 2 long values
            return new MessageStcSyncDeleteSinglePlayer(buffer.readUUID());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSinglePlayer message = (MessageStcSyncDeleteSinglePlayer) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSinglePlayer(message.uuid);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}