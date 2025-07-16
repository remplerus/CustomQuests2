package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteSingleParty implements ICQPacket {
    private int partyId;

    public MessageStcSyncDeleteSingleParty(){}

    public MessageStcSyncDeleteSingleParty(int partyId){
        this.partyId = partyId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSingleParty packet = (MessageStcSyncDeleteSingleParty) clazz;
        buffer.writeInt(packet.partyId);
    }

    public MessageStcSyncDeleteSingleParty decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(4)){
            return new MessageStcSyncDeleteSingleParty(buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSingleParty message = (MessageStcSyncDeleteSingleParty) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSingleParty(message.partyId);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}