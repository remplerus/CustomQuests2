package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteSingleTask implements ICQPacket {
    private int questId;
    private int taskId;

    public MessageStcSyncDeleteSingleTask(){}

    public MessageStcSyncDeleteSingleTask(int questId, int taskId){
        this.questId = questId;
        this.taskId = taskId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSingleTask packet = (MessageStcSyncDeleteSingleTask) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.taskId);
    }

    public MessageStcSyncDeleteSingleTask decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(8)){
            return new MessageStcSyncDeleteSingleTask(buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSingleTask message = (MessageStcSyncDeleteSingleTask) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSingleTask(message.questId, message.taskId);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}