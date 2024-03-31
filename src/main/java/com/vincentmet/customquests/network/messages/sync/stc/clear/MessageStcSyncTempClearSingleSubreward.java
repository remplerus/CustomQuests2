package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearSingleSubreward implements ICQPacket {
    private int questId;
    private int rewardId;
    private int subrewardId;

    public MessageStcSyncTempClearSingleSubreward(){}

    public MessageStcSyncTempClearSingleSubreward(int questId, int rewardId, int subrewardId){
        this.questId = questId;
        this.rewardId = rewardId;
        this.subrewardId = subrewardId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncTempClearSingleSubreward packet = (MessageStcSyncTempClearSingleSubreward) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.rewardId);
        buffer.writeInt(packet.subrewardId);
    }
    
    public MessageStcSyncTempClearSingleSubreward decode(FriendlyByteBuf buffer) {
        if (buffer.isReadable(12)){
            return new MessageStcSyncTempClearSingleSubreward(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncTempClearSingleSubreward message = (MessageStcSyncTempClearSingleSubreward) clazz;
        ctx.get().enqueueWork(() -> {
            if (message != null){
                EditorClientProcessor.Clear.Quests.Rewards.Subrewards.clearSingleSubreward(message.questId, message.rewardId, message.subrewardId);
            }
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}