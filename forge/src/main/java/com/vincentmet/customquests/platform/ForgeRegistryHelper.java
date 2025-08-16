package com.vincentmet.customquests.platform;

import com.vincentmet.customquests.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ForgeRegistryHelper {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MODID);

    @SuppressWarnings("unchecked")
    public static <T> DeferredRegister<T> deferredRegisterFor(Registry<T> objRegistry) {

        if (objRegistry.key().location() == ForgeRegistries.Keys.ITEMS.location()) return (DeferredRegister<T>) ITEMS;
        else if (objRegistry.key().location() == ForgeRegistries.Keys.BLOCKS.location()) return (DeferredRegister<T>) BLOCKS;
        else if (objRegistry.key().location() == ForgeRegistries.Keys.BLOCK_ENTITY_TYPES.location()) return (DeferredRegister<T>) BLOCK_ENTITIES;

        throw new IllegalArgumentException("No registry linked in Forge module to register type: " + objRegistry.key());
    }
}
