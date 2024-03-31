package com.vincentmet.customquests.integrations.jei;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JEIHelper{
    public static boolean isRuntimePresent(){
        try{
            Class.forName("mezz.jei.api.IModPlugin");
            return true;
        }catch(ClassNotFoundException ignored){}
        return false;
    }
    
    public static void openRecipe(ItemStack ingredient){
        if(isRuntimePresent())CQPlugin.runtime.getRecipesGui().show(CQPlugin.runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, ingredient));
    }
    
    public static void openUses(ItemStack ingredient){
        if(isRuntimePresent())CQPlugin.runtime.getRecipesGui().show(CQPlugin.runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.INPUT, VanillaTypes.ITEM_STACK, ingredient));
    }
    
    public static void openRecipe(FluidStack ingredient){
        if(isRuntimePresent())CQPlugin.runtime.getRecipesGui().show(CQPlugin.runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.OUTPUT, ForgeTypes.FLUID_STACK, ingredient));
    }
    
    public static void openUses(FluidStack ingredient){
        if(isRuntimePresent())CQPlugin.runtime.getRecipesGui().show(CQPlugin.runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.INPUT, ForgeTypes.FLUID_STACK, ingredient));
    }
    
    public static boolean hasRecipe(ItemStack stack){
        long count = Minecraft.getInstance().level.getRecipeManager().getRecipes().stream().filter(iRecipe -> iRecipe.getResultItem().getItem().equals(stack.getItem())).count();
        return count>0;
    }
    
    public static boolean hasUse(ItemStack stack){
        long count = Minecraft.getInstance().level.getRecipeManager().getRecipes()
                                                  .stream()
                                                  .filter(recipe -> recipe.getIngredients().stream().noneMatch(ingredient->ingredient.test(stack)))
                                                  .count();
        return count>0;
    }
}