package com.vincentmet.customquests.helpers;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TagHelper{
	public static class Items{
		public static List<HolderSet.Named<Item>> getEntries(ResourceLocation tag){
			TagKey<Item> tagKey = getTagKeyFromRL(tag);
			List<HolderSet.Named<Item>> result = new ArrayList<>();
			if(doesTagExist(tagKey)){
				result.addAll(BuiltInRegistries.ITEM.getTag(tagKey).stream().toList());
			}
			return result;
		}

		public static boolean doesTagExist(ResourceLocation tag){
			return getTagKeyFromRL(tag)!=null;
		}

		public static boolean doesTagExist(TagKey<Item> tag){
			return tag!=null;
		}

		public static TagKey<Item> getTagKeyFromRL(ResourceLocation tagRL){
			if(tagRL != null){
				Optional<TagKey<Item>> optional = BuiltInRegistries.ITEM.getTagNames().filter(itemTagKey -> itemTagKey.location().equals(tagRL)).findFirst();
				if(optional.isPresent()){
					return optional.get();
				}
			}
			return null;
		}
	}
}