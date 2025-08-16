package com.vincentmet.customquests.mixin;

import com.vincentmet.customquests.imixin.IResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public class MixinResultSlot implements IResultSlot {
    private ItemStack cq$craftingStack = ItemStack.EMPTY;
    public ItemStack getCrafting() {
        return cq$craftingStack;
    }

    @Inject(method="checkTakeAchievements", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V", shift = At.Shift.AFTER))
    private void cq$checkTakeAchievements(ItemStack stack, CallbackInfo ci) {
        cq$craftingStack = stack.copy();

    }
}
