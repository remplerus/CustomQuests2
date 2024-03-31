package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearSingleTask implements ICQPacket {
    private int questId;
    private int taskId;

    public MessageStcSyncTempClearSingleTask(){}

    public MessageStcSyncTempClearSingleTask(int questId, int taskId){
        this.questId = questId;
        this.taskId = taskId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncTempClearSingleTask packet = (MessageStcSyncTempClearSingleTask) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.taskId);
    }
    
    public MessageStcSyncTempClearSingleTask decode(FriendlyByteBuf buffer) {
        if (buffer.isReadable(8)){
            return new MessageStcSyncTempClearSingleTask(buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncTempClearSingleTask message = (MessageStcSyncTempClearSingleTask) clazz;
        ctx.get().enqueueWork(() -> {
            if (message != null){
                EditorClientProcessor.Clear.Quests.Tasks.clearSingleTask(message.questId, message.taskId);
            }
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}