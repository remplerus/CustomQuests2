package com.vincentmet.customquests.network.messages.sync;

import com.vincentmet.customquests.Config;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateServerSettings implements ICQPacket {
	public boolean edit_mode;
	public boolean reward_claim_one_per_party;
	public boolean give_device_on_first_login;

	public MessageUpdateServerSettings(){}

	public MessageUpdateServerSettings(boolean edit_mode, boolean reward_claim_one_per_party, boolean give_device_on_first_login){
		this.edit_mode = edit_mode;
		this.reward_claim_one_per_party = reward_claim_one_per_party;
		this.give_device_on_first_login = give_device_on_first_login;
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageUpdateServerSettings packet = (MessageUpdateServerSettings) clazz;
		buffer.writeBoolean(packet.edit_mode);
		buffer.writeBoolean(packet.reward_claim_one_per_party);
		buffer.writeBoolean(packet.give_device_on_first_login);
	}

	public MessageUpdateServerSettings decode(FriendlyByteBuf buffer) {
		return new MessageUpdateServerSettings(buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateServerSettings message = (MessageUpdateServerSettings) clazz;
		ctx.get().enqueueWork(() -> {
			Config.ServerToClientSyncedConfig.EDIT_MODE = message.edit_mode;
			Config.ServerToClientSyncedConfig.CAN_REWARD_ONLY_BE_CLAIMED_ONCE = message.reward_claim_one_per_party;
			Config.ServerToClientSyncedConfig.GIVE_DEVICE_ON_FIRST_LOGIN = message.give_device_on_first_login;
		});
		ctx.get().setPacketHandled(true);
	}
}
