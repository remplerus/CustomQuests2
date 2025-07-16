package com.vincentmet.customquests.network.messages;

import com.vincentmet.customquests.gui.QuestingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageReinitQuestingCanvas implements ICQPacket{
    @Override
    public <T extends ICQPacket> void encode(T clazz, FriendlyByteBuf buffer) {}

    public MessageReinitQuestingCanvas decode(FriendlyByteBuf buffer) {
        return new MessageReinitQuestingCanvas();
    }

    @Override
    public <T extends ICQPacket> void handle(T clazz, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Screen currentScreen = Minecraft.getInstance().screen;
            if(currentScreen instanceof QuestingScreen){
                ((QuestingScreen)currentScreen).requestPosRecalc();
                ((QuestingScreen)currentScreen).reInit();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}