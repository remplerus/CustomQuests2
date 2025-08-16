package com.vincentmet.customquests.network.messages.button;

import com.vincentmet.customquests.api.CombinedProgressHelper;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageRewardClaim implements ICQPacket {
	public int questId;
	public int rewardId;
	
	public MessageRewardClaim(){}

	public MessageRewardClaim(int questId, int rewardId){
		this.questId = questId;
		this.rewardId = rewardId;
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageRewardClaim packet = (MessageRewardClaim) clazz;
		buffer.writeInt(packet.questId);
		buffer.writeInt(packet.rewardId);
	}

	public MessageRewardClaim decode(FriendlyByteBuf buffer) {
		if(buffer.readableBytes() >= 8){//2 ints
			int questID = buffer.readInt();
			int rewardID = buffer.readInt();
			return new MessageRewardClaim(questID, rewardID);
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageRewardClaim message = (MessageRewardClaim) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				CombinedProgressHelper.claimReward(Objects.requireNonNull(ctx.get().getSender()).getUUID(), message.questId, message.rewardId);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
