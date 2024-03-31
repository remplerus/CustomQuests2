package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearSingleSubtask implements ICQPacket {
    private int questId;
    private int taskId;
    private int subtaskId;

    public MessageStcSyncTempClearSingleSubtask(){}

    public MessageStcSyncTempClearSingleSubtask(int questId, int taskId, int subtaskId){
        this.questId = questId;
        this.taskId = taskId;
        this.subtaskId = subtaskId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncTempClearSingleSubtask packet = (MessageStcSyncTempClearSingleSubtask) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.taskId);
        buffer.writeInt(packet.subtaskId);
    }
    
    public MessageStcSyncTempClearSingleSubtask decode(FriendlyByteBuf buffer) {
        if (buffer.isReadable(12)){
            return new MessageStcSyncTempClearSingleSubtask(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncTempClearSingleSubtask message = (MessageStcSyncTempClearSingleSubtask) clazz;
        ctx.get().enqueueWork(() -> {
            if (message != null){
                EditorClientProcessor.Clear.Quests.Tasks.Subtasks.clearSingleSubtask(message.questId, message.taskId, message.subtaskId);
            }
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}