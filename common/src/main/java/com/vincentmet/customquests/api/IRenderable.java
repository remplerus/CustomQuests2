package com.vincentmet.customquests.api;

import net.minecraft.client.gui.GuiGraphics;

public interface IRenderable{
    void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks);
}