package com.vincentmet.customquests.api;

import com.mojang.datafixers.util.Pair;
import com.vincentmet.customquests.api.logic.*;
import com.vincentmet.customquests.hierarchy.quest.*;
import java.util.*;
import java.util.function.Supplier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CQRegistry{
	//------------------Type Registering------------------
	private static final Map<ResourceLocation, Pair<ITextComponent, Supplier<ITaskType>>> taskTypes = new HashMap<>();
	private static final Map<ResourceLocation, Supplier<IRewardType>> rewardTypes = new HashMap<>();
	private static final Map<ResourceLocation, Supplier<IButtonShape>> buttonShapes = new HashMap<>();
	private static final Map<ResourceLocation, Supplier<ITextType>> textTypes = new HashMap<>();
	
	public static void registerTaskType(Supplier<ITaskType> taskType){
		taskTypes.put(taskType.get().getId(), new Pair<>(taskType.get().getTranslation(), taskType));
	}
	public static void registerRewardType(Supplier<IRewardType> rewardType){
		rewardTypes.put(rewardType.get().getId(), rewardType);
	}
	public static void registerButtonShapes(Supplier<IButtonShape> buttonShape){
		buttonShapes.put(buttonShape.get().getId(), buttonShape);
	}
	public static void registerTextTypes(Supplier<ITextType> textType){
		textTypes.put(textType.get().getId(), textType);
	}
	
	public static Map<ResourceLocation, Pair<ITextComponent, Supplier<ITaskType>>> getTaskTypes(){
		return taskTypes;
	}
	public static Map<ResourceLocation, Supplier<IRewardType>> getRewardTypes(){
		return rewardTypes;
	}
	public static Map<ResourceLocation, Supplier<IButtonShape>> getButtonShapes(){
		return buttonShapes;
	}
	public static Map<ResourceLocation, Supplier<ITextType>> getTextTypes(){
		return textTypes;
	}
}