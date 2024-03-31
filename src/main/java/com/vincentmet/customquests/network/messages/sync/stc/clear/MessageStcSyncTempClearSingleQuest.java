package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearSingleQuest implements ICQPacket {
    private int questId;

    public MessageStcSyncTempClearSingleQuest(){}

    public MessageStcSyncTempClearSingleQuest(int questId){
        this.questId = questId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncTempClearSingleQuest packet = (MessageStcSyncTempClearSingleQuest) clazz;
        buffer.writeInt(packet.questId);
    }
    
    public MessageStcSyncTempClearSingleQuest decode(FriendlyByteBuf buffer) {
        if (buffer.isReadable(4)){
            return new MessageStcSyncTempClearSingleQuest(buffer.readInt());
        }
        return null;
    }
    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncTempClearSingleQuest message = (MessageStcSyncTempClearSingleQuest) clazz;
        ctx.get().enqueueWork(() -> {
            if (message != null){
                EditorClientProcessor.Clear.Quests.clearSingleQuest(message.questId);
            }
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}