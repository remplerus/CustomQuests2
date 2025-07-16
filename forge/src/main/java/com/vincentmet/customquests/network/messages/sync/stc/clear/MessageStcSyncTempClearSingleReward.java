package com.vincentmet.customquests.network.messages.sync.stc.clear;

import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncTempClearSingleReward implements ICQPacket {
    private int questId;
    private int rewardId;

    public MessageStcSyncTempClearSingleReward(){}

    public MessageStcSyncTempClearSingleReward(int questId, int rewardId){
        this.questId = questId;
        this.rewardId = rewardId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncTempClearSingleReward packet = (MessageStcSyncTempClearSingleReward) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.rewardId);
    }
    
    public MessageStcSyncTempClearSingleReward decode(FriendlyByteBuf buffer) {
        if (buffer.isReadable(8)){
            return new MessageStcSyncTempClearSingleReward(buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncTempClearSingleReward message = (MessageStcSyncTempClearSingleReward) clazz;
        ctx.get().enqueueWork(() -> {
            if (message != null){
                EditorClientProcessor.Clear.Quests.Rewards.clearSingleReward(message.questId, message.rewardId);
            }
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}