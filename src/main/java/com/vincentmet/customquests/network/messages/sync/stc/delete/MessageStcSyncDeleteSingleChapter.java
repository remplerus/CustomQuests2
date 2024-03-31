package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteSingleChapter implements ICQPacket {
    private int chapterId;

    public MessageStcSyncDeleteSingleChapter(){}

    public MessageStcSyncDeleteSingleChapter(int chapterId){
        this.chapterId = chapterId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSingleChapter packet = (MessageStcSyncDeleteSingleChapter) clazz;
        buffer.writeInt(packet.chapterId);
    }
    
    public MessageStcSyncDeleteSingleChapter decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(4)){
            return new MessageStcSyncDeleteSingleChapter(buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSingleChapter message = (MessageStcSyncDeleteSingleChapter) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSingleChapter(message.chapterId);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}