package com.vincentmet.customquests.network.messages.sync.stc.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.*;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStcSyncUpdateSingleParty implements ICQPacket {
	private int partyId;
	private JsonObject jsonObject;

	public MessageStcSyncUpdateSingleParty(){}

	private MessageStcSyncUpdateSingleParty(int partyId, JsonObject jsonObject){
		this.partyId = partyId;
		this.jsonObject = jsonObject;
	}

	public MessageStcSyncUpdateSingleParty(int partyId){
		this.partyId = partyId;
		if(PartyHelper.doesPartyExist(partyId)){
			jsonObject = QuestingStorage.getSidedPartiesMap().get(partyId).getJson();
		}else{
			ServerUtils.Packets.Delete.deleteSinglePartyAtAllClients(partyId);
		}
	}

	@Override
	public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
		MessageStcSyncUpdateSingleParty packet = (MessageStcSyncUpdateSingleParty) clazz;
		if(PartyHelper.doesPartyExist(packet.partyId) && packet.jsonObject != null){
			buffer.writeInt(packet.partyId);
			buffer.writeUtf(packet.jsonObject.toString());
		}
	}
	
	public MessageStcSyncUpdateSingleParty decode(FriendlyByteBuf buffer){
		if(buffer.isReadable(6)){//4 for int, 2+ for json
			return new MessageStcSyncUpdateSingleParty(buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
		}
		return null;
	}

	@Override
	public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
		MessageStcSyncUpdateSingleParty message = (MessageStcSyncUpdateSingleParty) clazz;
		ctx.get().enqueueWork(() -> {
			if(message!=null){
				EditorClientProcessor.Update.Parties.updateSingleParty(message.partyId, message.jsonObject);
				ClientUtils.reloadMainGuiIfOpen();
				ClientUtils.reloadEditorIfOpen();
				CustomQuestsLogger.info("Party " + message.partyId + " synced!");
			}
		}).thenRun(() -> ctx.get().setPacketHandled(true));
	}
}