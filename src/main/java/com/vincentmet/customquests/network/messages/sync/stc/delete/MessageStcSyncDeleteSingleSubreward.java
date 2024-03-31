package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteSingleSubreward implements ICQPacket {
    private int questId;
    private int taskId;
    private int subrewardId;

    public MessageStcSyncDeleteSingleSubreward(){}

    public MessageStcSyncDeleteSingleSubreward(int questId, int taskId, int subrewardId){
        this.questId = questId;
        this.taskId = taskId;
        this.subrewardId = subrewardId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSingleSubreward packet = (MessageStcSyncDeleteSingleSubreward) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.taskId);
        buffer.writeInt(packet.subrewardId);
    }
    
    public MessageStcSyncDeleteSingleSubreward decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(12)){
            return new MessageStcSyncDeleteSingleSubreward(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSingleSubreward message = (MessageStcSyncDeleteSingleSubreward) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSingleSubreward(message.questId, message.taskId, message.subrewardId);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}