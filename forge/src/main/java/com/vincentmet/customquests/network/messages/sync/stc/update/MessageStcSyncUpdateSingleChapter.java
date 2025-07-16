package com.vincentmet.customquests.network.messages.sync.stc.update;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.*;
import com.vincentmet.customquests.network.messages.ICQPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class MessageStcSyncUpdateSingleChapter implements ICQPacket {
    private int chapterId;
    private JsonObject jsonObject;

    public MessageStcSyncUpdateSingleChapter(){}

    private MessageStcSyncUpdateSingleChapter(int chapterId, JsonObject json){
        this.chapterId = chapterId;
        this.jsonObject = json;
    }

    public MessageStcSyncUpdateSingleChapter(int chapterId){
        this.chapterId = chapterId;
        if(ChapterHelper.doesChapterExist(chapterId)){
            jsonObject = QuestingStorage.getSidedChaptersMap().get(chapterId).getJson();
        }else{
            ServerUtils.Packets.Delete.deleteSingleChapterAtAllClients(chapterId);
        }
    }

    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {
        MessageStcSyncUpdateSingleChapter packet = (MessageStcSyncUpdateSingleChapter) clazz;
        if(ChapterHelper.doesChapterExist(packet.chapterId) && packet.jsonObject != null){
            buffer.writeInt(packet.chapterId);
            buffer.writeUtf(packet.jsonObject.toString());
        }
    }
    
    public MessageStcSyncUpdateSingleChapter decode(FriendlyByteBuf buffer) {
        if(buffer.isReadable(6)){//4 for int, 2+ for json
            return new MessageStcSyncUpdateSingleChapter(buffer.readInt(), JsonParser.parseString(buffer.readUtf()).getAsJsonObject());
        }
        return null;
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        MessageStcSyncUpdateSingleChapter message = (MessageStcSyncUpdateSingleChapter) clazz;
        ctx.get().enqueueWork(() -> {
            if(message!=null){
                EditorClientProcessor.Update.Chapters.updateSingleChapter(message.chapterId, message.jsonObject);
                ClientUtils.reloadMainGuiIfOpen();
                ClientUtils.reloadEditorIfOpen();
                CustomQuestsLogger.info("Chapter " + message.chapterId + " synced!");
            }
        }).thenRun(() -> ctx.get().setPacketHandled(true));
    }
}