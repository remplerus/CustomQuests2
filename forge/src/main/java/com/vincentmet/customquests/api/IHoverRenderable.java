package com.vincentmet.customquests.api;

import net.minecraft.client.gui.GuiGraphics;

public interface IHoverRenderable extends IRenderable{
    void renderHover(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks);
}