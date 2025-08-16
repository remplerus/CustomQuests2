package com.vincentmet.customquests.hierarchy.chapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.CustomQuestsLogger;
import com.vincentmet.customquests.api.IJsonArrayProcessor;
import com.vincentmet.customquests.api.IJsonArrayProvider;
import com.vincentmet.customquests.gui.editor.EditorEntryWrapper;
import com.vincentmet.customquests.gui.editor.IEditorEntry;
import com.vincentmet.customquests.gui.editor.IEditorPage;
import com.vincentmet.customquests.helpers.IntCounter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;

public class QuestList extends HashSet<Integer> implements IJsonArrayProcessor, IJsonArrayProvider, IEditorPage {
	private final int parentChapterId;
	
	public QuestList(int parentChapterId){
		this.parentChapterId = parentChapterId;
	}
	
	public QuestList add(int id){
		if(id >= 0){
			super.add(id);
		}
		return this;
	}
	
	@Override
	public void processJson(JsonArray json){
		clear();
		IntCounter counter = new IntCounter();
		for(JsonElement jsonEntriesElement : json){
			if(jsonEntriesElement.isJsonPrimitive()){
				JsonPrimitive jsonEntriesPrimitive = jsonEntriesElement.getAsJsonPrimitive();
				if(jsonEntriesPrimitive.isNumber()){
					int jsonEntriesPrimitiveIntValue = jsonEntriesPrimitive.getAsInt();
					add(jsonEntriesPrimitiveIntValue);
				}else{
					CustomQuestsLogger.warn("'Chapter > " + parentChapterId + " > quests > " + counter.getValue() + "': Value is not an Integer, discarding it for now!");
				}
			}else{
				CustomQuestsLogger.warn("'Chapter > " + parentChapterId + " > quests > " + counter.getValue() + "': Value is not a JsonPrimitive, please use an Integer, discarding it for now!");
			}
			counter.count();
		}
	}
	
	@Override
	public JsonArray getJson(){
		JsonArray json = new JsonArray();
		forEach(json::add);
		return json;
	}

	@Override
	public void addPageEntries(List<IEditorEntry> list) {
		forEach(questId -> {
			list.add(new EditorEntryWrapper(Component.literal(""), new ResourceLocation(Constants.MODID, "integer"), () -> questId, newValueObject -> {
				//todo maybe create a new screen for it, passing important data to it, including the instance of the editor screen, then go back to that instance on close or on save, instead of opening a new screen
			}));
		});
	}
}