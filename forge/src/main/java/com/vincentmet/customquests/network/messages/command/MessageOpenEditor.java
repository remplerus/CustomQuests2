package com.vincentmet.customquests.network.messages.command;

import com.vincentmet.customquests.Config;
import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageOpenEditor implements ICQPacket {
	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {}

	public MessageOpenEditor decode(FriendlyByteBuf buffer) {
		return new MessageOpenEditor();
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if (Config.SidedConfig.isEditModeOn()){
				ClientUtils.openEditorScreen();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}
