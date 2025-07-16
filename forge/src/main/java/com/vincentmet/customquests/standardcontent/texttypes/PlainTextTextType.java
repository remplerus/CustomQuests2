package com.vincentmet.customquests.standardcontent.texttypes;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.api.ITextType;
import com.vincentmet.customquests.api.TextUtils;
import net.minecraft.resources.ResourceLocation;

public class PlainTextTextType implements ITextType{
    private static final ResourceLocation ID = new ResourceLocation(Constants.MODID, "plaintext");
    private String text = "";
    
    public PlainTextTextType(){}
    
    public PlainTextTextType(String text){
        this.text = text;
    }
    
    @Override
    public ResourceLocation getId(){
        return ID;
    }
    
    @Override
    public String getOgText(){
        return text;
    }
    
    @Override
    public void setOgText(String newText){
        text = newText;
    }
    
    @Override
    public String getStyledText(){
        return TextUtils.colorify(text);
    }
}
