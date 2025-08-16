package com.vincentmet.customquests.events;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler{

	/*@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event){
		//SoundEvent se = new SoundEvent(new ResourceLocation(Ref.MODID, "quest0")).setRegistryName(new ResourceLocation(Ref.MODID, "quest0"));
		//QuestingStorage.SOUNDS.put("quest0", se); //todo SOUNDS make this some kind of modular system
		//Main
		event.getRegistry().registerAll(
				//QuestingStorage.SOUNDS.get("quest0")
		);
	}*/
}