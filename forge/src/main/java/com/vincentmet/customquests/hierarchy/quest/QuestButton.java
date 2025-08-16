package com.vincentmet.customquests.hierarchy.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.CQRegistry;
import com.vincentmet.customquests.api.EditorGuiHelper;
import com.vincentmet.customquests.api.IButtonShape;
import com.vincentmet.customquests.api.IJsonObjectProcessor;
import com.vincentmet.customquests.api.IJsonObjectProvider;
import com.vincentmet.customquests.api.IQuestingTexture;
import com.vincentmet.customquests.gui.editor.EditorEntryWrapper;
import com.vincentmet.customquests.gui.editor.IEditorEntry;
import com.vincentmet.customquests.gui.editor.IEditorPage;
import com.vincentmet.customquests.helpers.TagHelper;
import com.vincentmet.customquests.standardcontent.buttonshapes.Shape;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QuestButton implements IJsonObjectProvider, IJsonObjectProcessor, IEditorPage {

    private static final IButtonShape DEFAULT_SHAPE = Shape.HEXAGON;
    private static final IQuestingTexture DEFAULT_ICON = new ItemSlideshowTexture(BuiltInRegistries.BLOCK.getKey(Blocks.GRASS_BLOCK), new ItemStack(Blocks.GRASS_BLOCK));
    private static final double DEFAULT_SCALE = 1D;
    private final int parentQuestId;
    private IButtonShape shape;
    private IQuestingTexture icon;
    private double scale = 1D;
    
    public QuestButton(int parentQuestId){
        this.parentQuestId = parentQuestId;
    }
    
    public IButtonShape getShape(){
        return shape;
    }
    
    public IQuestingTexture getIcon(){
        return icon;
    }
    
    public int getParentQuestId(){
        return parentQuestId;
    }
    
    public void setShape(IButtonShape shape){
        if(shape != null && CQRegistry.getButtonShapes().containsKey(shape.getId())){
            this.shape = shape;
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > shape': Value does not match a registered ButtonShape, please download the addon mod it belongs to, or change it to something valid. Defaulting to '"+Shape.HEXAGON.getId()+"'!");
            setShape(DEFAULT_SHAPE);
        }
    }

    public void setShape(ResourceLocation shapeRL){
        for(Map.Entry<ResourceLocation, Supplier<IButtonShape>> entry : CQRegistry.getButtonShapes().entrySet()){
            if(entry.getKey().toString().equals(shapeRL.toString())){
                setShape(entry.getValue().get());
            }
        }
        if(shape == null){
            CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > shape': Value does not match a registered ButtonShape, please download the addon mod it belongs to, or change it to something valid. Defaulting to '"+Shape.HEXAGON.getId()+"'!");
            setShape(Shape.HEXAGON);
        }
    }
    
    public void setIcon(IQuestingTexture icon){
        if (icon != null && icon.isValid()){
            this.icon = icon;
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > icon': The given texture is either null or invalid, defaulting to '"+BuiltInRegistries.BLOCK.getKey(Blocks.GRASS_BLOCK)+"'!");
            setIcon(DEFAULT_ICON);
        }
    }

    public void setIcon(ResourceLocation iconRL){
        if(TagHelper.Items.doesTagExist(iconRL)){
            List<ItemStack> tagStacks = new ArrayList<>();
            for (int i = 0; i < TagHelper.Items.getEntries(iconRL).size(); i++) {
                HolderSet.Named<Item> tagEntry = TagHelper.Items.getEntries(iconRL).get(i);
                for (int j = 0; j < tagEntry.size(); j++) {
                    Item item = tagEntry.get(j).get();
                    tagStacks.add(new ItemStack(item));
                }
            }
            //TagHelper.Items.getEntries(iconRL).stream().map(ItemStack::new).forEach(tagStacks::add);
            setIcon(new ItemSlideshowTexture(iconRL, tagStacks));
        }else{
            if(BuiltInRegistries.ITEM.containsKey(iconRL)){
                setIcon(new ItemSlideshowTexture(iconRL, new ItemStack(BuiltInRegistries.ITEM.get(iconRL))));
            }else{
                CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > icon': There is no valid item/tag with ResourceLocation '" + iconRL + "' found, defaulting to '"+BuiltInRegistries.BLOCK.getKey(Blocks.GRASS_BLOCK)+"'!");
                setIcon(DEFAULT_ICON);
            }
        }
    }
    
    public void setScale(double scale){
        if (scale < 0.5) scale = 0.5;
        if (scale > 20) scale = 20;
        this.scale = scale;
    }
    
    public double getScale(){
        return scale;
    }
    
    @Override
    public void processJson(JsonObject json){
        if(json.has("shape")){
            JsonElement jsonElement = json.get("shape");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    setShape(ResourceLocation.tryParse(jsonPrimitive.getAsString()));
                }else{
                    CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > shape': Value is not a String, defaulting to '"+DEFAULT_SHAPE.getId()+"'!");
                    setShape(DEFAULT_SHAPE);
                }
            }else{
                CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > shape': Value is not a JsonPrimitive, please use a String, defaulting to '"+DEFAULT_SHAPE.getId()+"'!");
                setShape(DEFAULT_SHAPE);
            }
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > shape': Not detected, defaulting to '"+DEFAULT_SHAPE.getId()+"'!");
            setShape(DEFAULT_SHAPE);
        }
        
        if(json.has("icon")){
            JsonElement jsonElement = json.get("icon");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    setIcon(ResourceLocation.tryParse(jsonPrimitive.getAsString()));
                }else{
                    CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > icon': Value is not a String, defaulting to '"+BuiltInRegistries.BLOCK.getKey(Blocks.GRASS_BLOCK)+"'!");
                    setIcon(DEFAULT_ICON);
                }
            }else{
                CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > icon': Value is not a JsonPrimitive, please use a String, defaulting to '"+BuiltInRegistries.BLOCK.getKey(Blocks.GRASS_BLOCK)+"'!");
                setIcon(DEFAULT_ICON);
            }
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > icon': Not detected, defaulting to '"+BuiltInRegistries.BLOCK.getKey(Blocks.GRASS_BLOCK)+"'!");
            setIcon(DEFAULT_ICON);
        }
    
        if(json.has("scale")){
            JsonElement jsonElement = json.get("scale");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isNumber()){
                    setScale(jsonPrimitive.getAsDouble());
                }else{
                    CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > scale': Value is not a Number, defaulting to '1.0D'!");
                    setScale(DEFAULT_SCALE);
                }
            }else{
                CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > scale': Value is not a JsonPrimitive, please use a Double, defaulting to '1.0D'!");
                setScale(DEFAULT_SCALE);
            }
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentQuestId + " > button > scale': Not detected, defaulting to '1.0D'!");
            setScale(DEFAULT_SCALE);
        }
    }
    
    @Override
    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("shape", shape.getId().toString());
        json.addProperty("icon", icon.toString());
        json.addProperty("scale", scale);
        return json;
    }

    @Override
    public void addPageEntries(List<IEditorEntry> list) {
        list.add(new EditorEntryWrapper(Component.translatable(Constants.MODID + ".editor.keys.shape"), new ResourceLocation(Constants.MODID, "resourcelocation"), () -> getShape().getId().toString(), newValueObject -> {
            ResourceLocation rlValue = ResourceLocation.tryParse(newValueObject.toString());
            setShape(rlValue);
            EditorGuiHelper.Update.Quest.Button.requestUpdateShape(parentQuestId, getShape().getId());
        }));
        list.add(new EditorEntryWrapper(Component.translatable(Constants.MODID + ".editor.keys.icon"), new ResourceLocation(Constants.MODID, "resourcelocation"), () -> getIcon().getResourceLocation().toString(), newValueObject -> {
            ResourceLocation newRL = ResourceLocation.tryParse(newValueObject.toString());
            setIcon(newRL);
            EditorGuiHelper.Update.Quest.Button.requestUpdateIcon(parentQuestId, getIcon().getResourceLocation());
        }));
        list.add(new EditorEntryWrapper(Component.translatable(Constants.MODID + ".editor.keys.scale"), new ResourceLocation(Constants.MODID, "double"), this::getScale, newValueObject -> {
            setScale(Double.parseDouble(newValueObject.toString()));
            EditorGuiHelper.Update.Quest.Button.requestUpdateScale(parentQuestId, getScale());
        }));
    }
}