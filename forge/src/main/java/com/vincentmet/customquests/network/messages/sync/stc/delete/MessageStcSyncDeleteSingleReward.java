package com.vincentmet.customquests.network.messages.sync.stc.delete;

import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.EditorClientProcessor;
import com.vincentmet.customquests.network.messages.ICQPacket;
import com.vincentmet.customquests.network.messages.sync.MessageUpdateSinglePlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncDeleteSingleReward implements ICQPacket {
    private int questId;
    private int rewardId;

    public MessageStcSyncDeleteSingleReward(){}

    public MessageStcSyncDeleteSingleReward(int questId, int rewardId){
        this.questId = questId;
        this.rewardId = rewardId;
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncDeleteSingleReward packet = (MessageStcSyncDeleteSingleReward) clazz;
        buffer.writeInt(packet.questId);
        buffer.writeInt(packet.rewardId);
    }
    
    public MessageStcSyncDeleteSingleReward decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(8)){
            return new MessageStcSyncDeleteSingleReward(buffer.readInt(), buffer.readInt());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncDeleteSingleReward message = (MessageStcSyncDeleteSingleReward) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Delete.deleteSingleReward(message.questId, message.rewardId);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
            }
        }).thenRun(()->ctx.get().setPacketHandled(true));
    }
}