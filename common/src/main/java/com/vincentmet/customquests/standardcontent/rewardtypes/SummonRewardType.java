package com.vincentmet.customquests.standardcontent.rewardtypes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.IRewardType;
import com.vincentmet.customquests.helpers.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Objects;
import java.util.function.Consumer;

public class SummonRewardType implements IRewardType{
	private static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "summon");
	private EntityType entity;
	private int count;
	private Item icon = Items.DIAMOND_SWORD;
	
	private int parentQuestId;
	private int parentRewardId;
    
    @Override
    public ResourceLocation getId(){
        return ID;
    }
    
    @Override
	public void executeReward(ServerPlayer player){
		for(int i=0; i<count;i++){
			//TODO null check
			(player.level()).addFreshEntity(Objects.requireNonNull(entity.create((ServerLevel) player.level(), new CompoundTag(), null, player.blockPosition(), MobSpawnType.COMMAND, true, false)));
		}
	}
	
	@Override
	public Item getIcon(){
		return icon;
	}
	
	@Override
	public Runnable onSlotHover(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks){
		return ()->{/*NOOP*/};
	}
	
	@Override
	public String getText(){
		return count + "x " + entity.getDescription().getString();
	}
	
	@Override
	public Consumer<MouseButton> onSlotClick(){
		return (mouseButton)->{};
	}
	
	@Override
	public String toString(){
		return count + "x " + entity.getDescription().getString();
	}
	
	@Override
	public void processJson(JsonObject json){
		if(json.has("parent_quest_id")){
			JsonElement jsonElement = json.get("parent_quest_id");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()){
					parentQuestId = jsonPrimitive.getAsInt();
				}
			}
		}
		if(json.has("parent_reward_id")){
			JsonElement jsonElement = json.get("parent_reward_id");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()){
					parentRewardId = jsonPrimitive.getAsInt();
				}
			}
		}
		
		if(json.has("icon")){
			JsonElement jsonElement = json.get("icon");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isString()){
					String jsonPrimitiveStringValue = jsonPrimitive.getAsString();
					ResourceLocation rl = ResourceLocation.tryParse(jsonPrimitiveStringValue);
					if(rl != null){
						icon = BuiltInRegistries.ITEM.get(rl);
					}else{
						CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > icon': Value is not a valid item, please use a valid item id, defaulting to 'minecraft:zombie_spawn_egg'!");
						icon = Items.ZOMBIE_SPAWN_EGG;
					}
				}else{
					CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > icon': Value is not a String, defaulting to 'minecraft:zombie_spawn_egg'!");
					icon = Items.ZOMBIE_SPAWN_EGG;
				}
			}else{
				CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > icon': Value is not a JsonPrimitive, please use a String, defaulting to 'minecraft:zombie_spawn_egg'!");
				icon = Items.ZOMBIE_SPAWN_EGG;
			}
		}else{
			CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > icon': Not detected, defaulting to 'minecraft:zombie_spawn_egg'!");
			icon = Items.ZOMBIE_SPAWN_EGG;
		}
		
		if(json.has("count")){
			JsonElement jsonElement = json.get("count");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()){
					int jsonPrimitiveIntValue = jsonPrimitive.getAsInt();
					if(jsonPrimitiveIntValue >= 1){
						count = jsonPrimitiveIntValue;
					}else{
						CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > count': Value is not >= 1, defaulting to '1'!");
						count = 1;
					}
				}else{
					CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > count': Value is not an Integer, defaulting to '1'!");
					count = 1;
				}
			}else{
				CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > count': Value is not a JsonPrimitive, please use a Double, defaulting to '1'!");
				count = 1;
			}
		}else{
			CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > count': Not detected, defaulting to '1'!");
			count = 1;
		}
		
		if(json.has("entity")){
			JsonElement jsonElement = json.get("entity");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isString()){
					String jsonPrimitiveStringValue = jsonPrimitive.getAsString();
					ResourceLocation rl = ResourceLocation.tryParse(jsonPrimitiveStringValue);
					if(rl != null){
						entity = BuiltInRegistries.ENTITY_TYPE.get(rl);
					}else{
						CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > entity': Value is not a valid item, please use a valid item id, defaulting to 'minecraft:zombie_spawn_egg'!");
						entity = EntityType.SHEEP;
					}
				}else{
					CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > entity': Value is not a String, defaulting to 'minecraft:zombie_spawn_egg'!");
					entity = EntityType.SHEEP;
				}
			}else{
				CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > entity': Value is not a JsonPrimitive, please use a String, defaulting to 'minecraft:zombie_spawn_egg'!");
				entity = EntityType.SHEEP;
			}
		}else{
			CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > rewards > entries > " + parentRewardId + " > content > entity': Not detected, defaulting to 'minecraft:zombie_spawn_egg'!");
			entity = EntityType.SHEEP;
		}
	}
	
	@Override
	public JsonObject getJson(){
		JsonObject json = new JsonObject();
		json.addProperty("entity", BuiltInRegistries.ENTITY_TYPE.getKey(entity).toString());
		json.addProperty("count", count);
		json.addProperty("icon", BuiltInRegistries.ITEM.getKey(icon).toString());
		return json;
	}
}