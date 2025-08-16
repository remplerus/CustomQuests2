package com.vincentmet.customquests.standardcontent.buttonshapes;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.api.IButtonShape;
import net.minecraft.resources.ResourceLocation;

public enum Shape implements IButtonShape{
    ROUND(new ResourceLocation(Constants.MODID, "round"), new ResourceLocation(Constants.MODID, "textures/gui/button_round.png")),
    TRIANGLE_UP(new ResourceLocation(Constants.MODID, "triangle_up"), new ResourceLocation(Constants.MODID, "textures/gui/button_triangle_up.png")),
    TRIANGLE_DOWN(new ResourceLocation(Constants.MODID, "triangle_down"), new ResourceLocation(Constants.MODID, "textures/gui/button_triangle_down.png")),
    TRIANGLE_LEFT(new ResourceLocation(Constants.MODID, "triangle_left"), new ResourceLocation(Constants.MODID, "textures/gui/button_triangle_left.png")),
    TRIANGLE_RIGHT(new ResourceLocation(Constants.MODID, "triangle_right"), new ResourceLocation(Constants.MODID, "textures/gui/button_triangle_right.png")),
    SQUARE(new ResourceLocation(Constants.MODID, "square"), new ResourceLocation(Constants.MODID, "textures/gui/button_square.png")),
    PENTAGON(new ResourceLocation(Constants.MODID, "pentagon"), new ResourceLocation(Constants.MODID, "textures/gui/button_pentagon.png")),
    HEXAGON(new ResourceLocation(Constants.MODID, "hexagon"), new ResourceLocation(Constants.MODID, "textures/gui/button_hexagon.png")),
    OCTAGON(new ResourceLocation(Constants.MODID, "octagon"), new ResourceLocation(Constants.MODID, "textures/gui/button_octagon.png")),
    HEX_STAR(new ResourceLocation(Constants.MODID, "hexagon_star"), new ResourceLocation(Constants.MODID, "textures/gui/button_hexagon_star.png")),
    DIAMOND(new ResourceLocation(Constants.MODID, "diamond"), new ResourceLocation(Constants.MODID, "textures/gui/button_diamond.png")),
    HEART(new ResourceLocation(Constants.MODID, "heart"), new ResourceLocation(Constants.MODID, "textures/gui/button_heart.png")),
    TRAPEZIUM_UP(new ResourceLocation(Constants.MODID, "trapezium_up"), new ResourceLocation(Constants.MODID, "textures/gui/button_trapezium_up.png")),
    TRAPEZIUM_DOWN(new ResourceLocation(Constants.MODID, "trapezium_down"), new ResourceLocation(Constants.MODID, "textures/gui/button_trapezium_down.png")),
    TRAPEZIUM_LEFT(new ResourceLocation(Constants.MODID, "trapezium_left"), new ResourceLocation(Constants.MODID, "textures/gui/button_trapezium_left.png")),
    TRAPEZIUM_RIGHT(new ResourceLocation(Constants.MODID, "trapezium_right"), new ResourceLocation(Constants.MODID, "textures/gui/button_trapezium_right.png")),
    PARALLELOGRAM(new ResourceLocation(Constants.MODID, "parallelogram"), new ResourceLocation(Constants.MODID, "textures/gui/button_parallelogram.png")),
    PARALLELOGRAM_INVERTED(new ResourceLocation(Constants.MODID, "parallelogram_inverted"), new ResourceLocation(Constants.MODID, "textures/gui/button_parallelogram_inverted.png")),
    PARALLELOGRAM_ROTATED(new ResourceLocation(Constants.MODID, "parallelogram_rotated"), new ResourceLocation(Constants.MODID, "textures/gui/button_parallelogram_rotated.png")),
    PARALLELOGRAM_ROTATED_INVERTED(new ResourceLocation(Constants.MODID, "parallelogram_rotated_inverted"), new ResourceLocation(Constants.MODID, "textures/gui/button_parallelogram_rotated_inverted.png")),
    SPIKED_SQUARE(new ResourceLocation(Constants.MODID, "spiked_square"), new ResourceLocation(Constants.MODID, "textures/gui/button_spiked_square.png")),
    ROUNDED_SQUARE(new ResourceLocation(Constants.MODID, "rounded_square"), new ResourceLocation(Constants.MODID, "textures/gui/button_rounded_square.png")),
    ROUNDED_SQUARE_EXTRA(new ResourceLocation(Constants.MODID, "rounded_square_extra"), new ResourceLocation(Constants.MODID, "textures/gui/button_rounded_square_extra.png")),
    ROUNDED_HEXAGON(new ResourceLocation(Constants.MODID, "rounded_hexagon"), new ResourceLocation(Constants.MODID, "textures/gui/button_rounded_hexagon.png")),
    GEAR(new ResourceLocation(Constants.MODID, "gear"), new ResourceLocation(Constants.MODID, "textures/gui/button_gear.png")),
    ;
    
    final ResourceLocation ID;
    final ResourceLocation TEXTURE;
    
    Shape(ResourceLocation id, ResourceLocation texture){
        this.ID = id;
        this.TEXTURE = texture;
    }
    
    @Override
    public ResourceLocation getId(){
        return ID;
    }
    
    @Override
    public ResourceLocation getTexture(){
        return TEXTURE;
    }
}