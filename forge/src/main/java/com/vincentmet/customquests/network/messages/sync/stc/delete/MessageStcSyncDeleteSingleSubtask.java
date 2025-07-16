package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteSingleSubtask implements ICQPacket {
    private int questId;
    private int taskId;
    private int subtaskId;

    public MessageStcSyncDeleteSingleSubtask(){}

    public MessageStcSyncDeleteSingleSubtask(int questId, int taskId, int subtaskId){
        this.questId = questId;
        this.taskId = taskId;
        this.subtaskId = subtaskId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSingleSubtask packet = (MessageStcSyncDeleteSingleSubtask) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.taskId);
        buffer.writeInt(packet.subtaskId);
    }
    
    public MessageStcSyncDeleteSingleSubtask decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(12)){
            return new MessageStcSyncDeleteSingleSubtask(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSingleSubtask message = (MessageStcSyncDeleteSingleSubtask) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSingleSubtask(message.questId, message.taskId, message.subtaskId);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}