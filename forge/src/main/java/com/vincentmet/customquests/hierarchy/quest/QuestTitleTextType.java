package com.vincentmet.customquests.hierarchy.quest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.CQRegistry;
import com.vincentmet.customquests.api.EditorGuiHelper;
import com.vincentmet.customquests.api.IJsonObjectProcessor;
import com.vincentmet.customquests.api.IJsonObjectProvider;
import com.vincentmet.customquests.api.ITextType;
import com.vincentmet.customquests.gui.editor.EditorEntryWrapper;
import com.vincentmet.customquests.gui.editor.IEditorEntry;
import com.vincentmet.customquests.gui.editor.IEditorPage;
import com.vincentmet.customquests.standardcontent.texttypes.PlainTextTextType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class QuestTitleTextType implements IJsonObjectProcessor, IJsonObjectProvider, IEditorPage {
    private final int parentId;
    private ITextType type;

    public QuestTitleTextType(int parentId){
        this.parentId = parentId;
    }
    
    @Override
    public void processJson(JsonObject json){
        if(json.has("type")){
            JsonElement jsonElement = json.get("type");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    setType(ResourceLocation.tryParse(jsonPrimitive.getAsString()));
                }else{
                    CustomQuestsLogger.warn("'Quest > " + parentId + " > title > type': Value is not a String, defaulting to 'customquests:plaintext'!");
                    setType(new PlainTextTextType());
                }
            }else{
                CustomQuestsLogger.warn("'Quest > " + parentId + " > title > type': Value is not a JsonPrimitive, please use a String, defaulting to 'customquests:plaintext'!");
                setType(new PlainTextTextType());
            }
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentId + " > title > type': Not detected, defaulting to 'customquests:plaintext'!");
            setType(new PlainTextTextType());
        }
        
        if(json.has("text")){
            JsonElement jsonElement = json.get("text");
            if(jsonElement.isJsonPrimitive()){
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if(jsonPrimitive.isString()){
                    setText(jsonPrimitive.getAsString());
                }else{
                    CustomQuestsLogger.warn("'Quest > " + parentId + " > title > text': Value is not a String, defaulting to an empty String!");
                }
            }else{
                CustomQuestsLogger.warn("'Quest > " + parentId + " > title > text': Value is not a JsonPrimitive, please use a String, defaulting to an empty String!");
            }
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentId + " > title > text': Not detected, defaulting to an empty String!");
        }
    }
    
    @Override
    public JsonObject getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("type", type.getId().toString());
        json.addProperty("text", type.getOgText());
        return json;
    }

    public void setType(ITextType type){
        if(type != null){
            String ogText = "";
            if(this.type!=null){
                ogText = this.type.getOgText();
            }
            this.type = type;
            this.type.setOgText(ogText);
        }else{
            CustomQuestsLogger.warn("'Quest > " + parentId + " > title > type': Value does not match a registered TextType or is null, please download the addon mod it belongs to, or change it to something valid. Defaulting to 'customquests:plaintext'");
            setType(new PlainTextTextType());
        }
    }

    public void setType(ResourceLocation typeRL){
        for(Map.Entry<ResourceLocation, Supplier<ITextType>> entry : CQRegistry.getTextTypes().entrySet()){
            if(entry.getKey().toString().equals(typeRL.toString())){
                setType(entry.getValue().get());
            }
        }
    }
    
    public ITextType getType(){
        return type;
    }
    
    public String getStyledText(){
        return type.getStyledText();
    }

    public String getOgText(){
        return type.getOgText();
    }

    public void setText(String newText){
        type.setOgText(newText);
    }

    @Override
    public void addPageEntries(List<IEditorEntry> list) {
        list.add(new EditorEntryWrapper(Component.literal("Type"), new ResourceLocation(Constants.MODID, "resourcelocation"), () -> type.getId().toString(), newValueObject -> {
            setType(ResourceLocation.tryParse(newValueObject.toString()));
            EditorGuiHelper.Update.Quest.Title.requestUpdateType(parentId, getType().getId());
        }));
        list.add(new EditorEntryWrapper(Component.literal("Text"), new ResourceLocation(Constants.MODID, "plaintext"), () -> type.getOgText(), newValueObject -> {
            setText(newValueObject.toString());
            EditorGuiHelper.Update.Quest.Title.requestUpdateText(parentId, getOgText());
        }));
    }
}