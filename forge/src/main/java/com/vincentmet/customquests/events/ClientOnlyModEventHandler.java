package com.vincentmet.customquests.events;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.Objects;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientOnlyModEventHandler {
    @SubscribeEvent
    public static void registerItem(RegisterKeyMappingsEvent event){
        //Main
        event.register(Objects.KeyBinds.OPEN_QUESTING_SCREEN);
        event.register(Objects.KeyBinds.CLAIM_ALL_REWARDS);
    }
}