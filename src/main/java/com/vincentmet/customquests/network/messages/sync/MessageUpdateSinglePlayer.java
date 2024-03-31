package com.vincentmet.customquests.network.messages.sync;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.api.ClientUtils;
import com.vincentmet.customquests.api.QuestingStorage;
import com.vincentmet.customquests.hierarchy.progress.QuestingPlayer;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

import static com.vincentmet.customquests.Ref.CustomQuests.LOGGER;

public class MessageUpdateSinglePlayer implements ICQPacket {//todo is this class still needed? if so, probably need to rename and move to correct folder
	public JsonObject json;//used for receiving packet only
	public String uuid;
	
	public MessageUpdateSinglePlayer(){}

	public MessageUpdateSinglePlayer(String uuid){
		this.uuid = uuid;
	}

	public MessageUpdateSinglePlayer(String uuid, JsonObject json){//used for receiving packet only
		this.uuid = uuid;
		this.json = json;
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageUpdateSinglePlayer packet = (MessageUpdateSinglePlayer) clazz;
		buffer.writeUtf(packet.uuid);
		JsonObject modifiedJson = QuestingStorage.getSidedPlayersMap().get(packet.uuid).getJson();
		modifiedJson.get("individual_progress").getAsJsonObject().remove("entries");
		buffer.writeUtf(modifiedJson.toString());
	}
	
	public MessageUpdateSinglePlayer decode(FriendlyByteBuf buffer) {
		String uuid = buffer.readUtf();
		String data = buffer.readUtf();
		JsonObject json = JsonParser.parseString(data).getAsJsonObject();
		return new MessageUpdateSinglePlayer(uuid, json);
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateSinglePlayer message = (MessageUpdateSinglePlayer) clazz;
		ctx.get().enqueueWork(() -> {
			String uuid = message.uuid;
			JsonObject data = message.json;
			QuestingPlayer p = new QuestingPlayer(UUID.fromString(uuid));
			p.processJson(data);
			QuestingStorage.getSidedPlayersMap().put(uuid, p);
			ClientUtils.reloadEditorIfOpen();
			LOGGER.info("Basic user info for " + uuid + " synced!");
		});
		ctx.get().setPacketHandled(true);
	}
}
