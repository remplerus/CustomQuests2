package com.vincentmet.customquests.standardcontent.texttypes;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.api.ITextType;
import com.vincentmet.customquests.api.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;

public class TranslationTextType implements ITextType{
    private static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "translation_key");
    private Component translationKey;
    
    public TranslationTextType(){}
    
    public TranslationTextType(String translationKey){
        this.translationKey = Component.translatable(translationKey);
    }
    
    @Override
    public ResourceLocation getId(){
        return ID;
    }
    
    @Override
    public String getOgText(){
        if (translationKey != null){
            return ((TranslatableContents)translationKey.getContents()).getKey();
        }
        return "";
    }
    
    @Override
    public void setOgText(String newKey){
        translationKey = Component.translatable(newKey);
    }
    
    @Override
    public String getStyledText(){
        if(translationKey != null){
            return TextUtils.colorify(translationKey.getString());
        }
        return "";
    }
    
    public void setTranslationKey(Component translationKey){
        this.translationKey = translationKey;
    }
    
    public Component getTranslationKey(){
        return translationKey;
    }
}
