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
    @SubscribeEvent
    public static void registerItem(RegisterEvent event){
        event.register(ForgeRegistries.Keys.ITEMS,
                helper -> {
                    //Main
                    helper.register(new ResourceLocation(Constants.MODID, "questing_device"), Objects.Items.QUESTING_DEVICE);
                    helper.register(new ResourceLocation(Constants.MODID, "questing_block"), Objects.ItemBlocks.QUESTING_BLOCK);
                    //Standard Content
                    //helper.register(new ResourceLocation(Ref.MODID, "delivery_block"), Objects.ItemBlocks.DELIVERY_BLOCK);
                }
        );
        event.register(ForgeRegistries.Keys.BLOCKS,
                helper -> {
                    //Main
                    helper.register(new ResourceLocation(Constants.MODID, "questing_block"), Objects.Blocks.QUESTING_BLOCK);
                    //Standard Content
                    //event.getRegistry().registerAll(Objects.Blocks.DELIVERY_BLOCK);
                }
        );
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES,
                helper -> {
                    //Standard Content
                    //helper.register(new ResourceLocation(Ref.MODID, "delivery_block"), Objects.TileEntities.DELIVERY_BLOCK);
                }
        );
    }
	
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