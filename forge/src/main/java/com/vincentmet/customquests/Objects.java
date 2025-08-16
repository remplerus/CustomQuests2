package com.vincentmet.customquests;

import com.vincentmet.customquests.api.QuestingStorage;
import com.vincentmet.customquests.block.DeliveryBlock;
import com.vincentmet.customquests.block.QuestingBlock;
import com.vincentmet.customquests.block.blockentity.DeliveryBlockBlockEntity;
import com.vincentmet.customquests.gui.QuestingScreen;
import com.vincentmet.customquests.item.QuestingDevice;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Objects{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), Constants.MODID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MODID);

    public static final RegistryObject<Block> QUESTING_BLOCK = BLOCKS.register("questing_block", QuestingBlock::new);
    public static final RegistryObject<Block> DELIVERY_BLOCK = BLOCKS.register("delivery_block", DeliveryBlock::new);

    public static final RegistryObject<Item> QUESTING_BLOCK_ITEM = ITEMS.register("questing_block", () -> new BlockItem(QUESTING_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> DELIVERY_BLOCK_ITEM = ITEMS.register("delivery_block", () -> new BlockItem(DELIVERY_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> QUESTING_DEVICE = ITEMS.register("questing_device", () -> new QuestingDevice(new Item.Properties()));

    public static final RegistryObject<BlockEntityType<?>> DELIVERY_BLOCK_ENTITY = BLOCK_ENTITIES.register("questing_device", () ->
                BlockEntityType.Builder.of(DeliveryBlockBlockEntity::new, DELIVERY_BLOCK.get()).build(null));

    public static final RegistryObject<CreativeModeTab> cqTab = CREATIVE_TABS.register("customquests", () -> CreativeModeTab.builder().title(Component.literal("customquests")).build());
    //public static final RegistryObject<SoundEvent> QUESTING_DEVICE_USE = SOUNDS.register("questing_device_use", () -> SoundEvent.createVariableRangeEvent(Constants.rl("questing_device_use")));

	public static final class KeyBinds{
		public static final KeyMapping OPEN_QUESTING_SCREEN = new KeyMapping("customquests.keys.open_quests", 67, "customquests.keys.category");
		public static final KeyMapping CLAIM_ALL_REWARDS = new KeyMapping("customquests.keys.claim_all_rewards", 86, "customquests.keys.category");
	}

    public static void loadClass(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        QuestingStorage.SOUNDS.put("quest-default", SoundEvents.UI_BUTTON_CLICK.get());
    }
}
