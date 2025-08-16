package com.vincentmet.customquests.standardcontent.tasktypes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.ButtonContext;
import com.vincentmet.customquests.api.CombinedProgressHelper;
import com.vincentmet.customquests.api.IQuestingTexture;
import com.vincentmet.customquests.api.ITaskType;
import com.vincentmet.customquests.api.ServerUtils;
import com.vincentmet.customquests.helpers.MouseButton;
import com.vincentmet.customquests.helpers.PlayerBoundSubtaskReference;
import com.vincentmet.customquests.hierarchy.quest.ItemSlideshowTexture;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HuntTaskType implements ITaskType {
	private static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "hunt");
	private static final Component TRANSLATION = Component.translatable(Constants.MODID + ".standardcontent.tasks.hunt");
	public static final List<PlayerBoundSubtaskReference> TRACKING_LIST = new ArrayList<>();
	private int questId;
	private int taskId;
	private int subtaskId;
	
	private ResourceLocation ogRL;
	private int count;
	
	ItemSlideshowTexture icon = new ItemSlideshowTexture(BuiltInRegistries.ITEM.getKey(Items.DIAMOND_SWORD), new ItemStack(Items.DIAMOND_SWORD));
	private EntityType<?> entityType;
	
	@Override
	public ResourceLocation getId(){
		return ID;
	}
    
    @Override
    public Component getTranslation(){
        return TRANSLATION;
    }
	
	@Override
	public List<PlayerBoundSubtaskReference> getCurrentlyTrackingList(){
		return TRACKING_LIST;
	}
    
    @Override
	public boolean hasButton(ButtonContext context){
		return false;
	}
	
	@Override
	public void executeSubtaskCheck(Player player, Object object){
		if(!CombinedProgressHelper.isQuestCompleted(player.getUUID(), questId)){
			LivingDeathEvent event = (LivingDeathEvent)object;
			if(event.getEntity().getType().equals(entityType)){
				CombinedProgressHelper.addValue(player.getUUID(), questId, taskId, subtaskId, 1);
				ServerUtils.Packets.SyncToClient.Progress.syncAllProgressAndPartiesToPlayer((ServerPlayer)player);
			}
			processValue(player);
		}
	}
	
	public void processValue(Player player){
		if(CombinedProgressHelper.getValue(player.getUUID(), questId, taskId, subtaskId) >= count){
			CombinedProgressHelper.completeSubtask(player.getUUID(), questId, taskId, subtaskId);
		}
	}
	
	@Override
	public void executeSubtaskButton(Player player){
		/*NOOP*/
	}
	
	@Override
	public IQuestingTexture getIcon(LocalPlayer player){
		return icon;
	}
	
	@Override
	public Runnable onSlotHover(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks, LocalPlayer player){
		return ()->{};
	}
	
	@Override
	public String getText(LocalPlayer player){
		return count + "x " + entityType.getDescription().getString();
	}
	
	@Override
	public int getCompletionAmount(){
		return count;
	}
    
    @Override
    public Consumer<MouseButton> onSlotClick(LocalPlayer player){
		return (mouseButton)->{};
    }
    
    @Override
	public void processJson(JsonObject json){
		if(json.has("quest_id")){
			JsonElement jsonElement = json.get("quest_id");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()){
					questId = jsonPrimitive.getAsInt();
				}
			}
		}
		if(json.has("task_id")){
			JsonElement jsonElement = json.get("task_id");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()){
					taskId = jsonPrimitive.getAsInt();
				}
			}
		}
		if(json.has("subtask_id")){
			JsonElement jsonElement = json.get("subtask_id");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isNumber()){
					subtaskId = jsonPrimitive.getAsInt();
				}
			}
		}
		
		if(json.has("entity")){
			JsonElement jsonElement = json.get("entity");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isString()){
					String jsonPrimitiveStringValue = jsonPrimitive.getAsString();
					ogRL = ResourceLocation.tryParse(jsonPrimitiveStringValue);
					if(ogRL == null){
						CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > entity': Value is not a valid item, please use a valid item id, defaulting to 'minecraft:pig'!");
						ogRL = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIG);
					}
				}else{
					CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > entity': Value is not a String, defaulting to 'minecraft:pig'!");
					ogRL = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIG);
				}
			}else{
				CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > entity': Value is not a JsonPrimitive, please use a String, defaulting to 'minecraft:pig'!");
				ogRL = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIG);
			}
		}else{
			CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > entity': Not detected, defaulting to 'minecraft:pig'!");
			ogRL = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.PIG);
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
						CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > count': Value is not >= 1, defaulting to '1'!");
						count = 1;
					}
				}else{
					CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > count': Value is not an Integer, defaulting to '1'!");
					count = 1;
				}
			}else{
				CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > count': Value is not a JsonPrimitive, please use a Double, defaulting to '1'!");
				count = 1;
			}
		}else{
			CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > count': Not detected, defaulting to '1'!");
			count = 1;
		}
		
		if(json.has("icon")){
			JsonElement jsonElement = json.get("icon");
			if(jsonElement.isJsonPrimitive()){
				JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
				if(jsonPrimitive.isString()){
					String jsonPrimitiveStringValue = jsonPrimitive.getAsString();
					ResourceLocation rl = ResourceLocation.tryParse(jsonPrimitiveStringValue);
					if(rl != null){
						icon = new ItemSlideshowTexture(rl, new ItemStack(BuiltInRegistries.ITEM.get(rl)));
					}else{
						CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > icon': Value is not a valid item, please use a valid item id, defaulting to 'minecraft:diamond_sword'!");
					}
				}else{
					CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > icon': Value is not a String, defaulting to 'minecraft:diamond_sword'!");
				}
			}else{
				CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > icon': Value is not a JsonPrimitive, please use a String, defaulting to 'minecraft:diamond_sword'!");
			}
		}else{
			CustomQuestsLogger.warn("'Quest > " + questId + " > tasks > entries > " + taskId + " > sub_tasks > entries > " + subtaskId + " > icon': Not detected, defaulting to 'minecraft:diamond_sword'!");
		}
		
		entityType = BuiltInRegistries.ENTITY_TYPE.get(ogRL);
	}
	
	@Override
	public JsonObject getJson(){
		JsonObject json = new JsonObject();
		json.addProperty("entity", ogRL.toString());
		json.addProperty("count", count);
		json.addProperty("icon", icon.getResourceLocation().toString());
		return json;
	}
}