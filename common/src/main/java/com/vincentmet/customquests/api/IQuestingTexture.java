package com.vincentmet.customquests.api;

import net.minecraft.client.gui.GuiGraphics;

public interface IQuestingTexture extends IResourceLocationProvider{
	boolean isValid();
	void render(GuiGraphics stack, float scale, int x, int y, float offsetX, float offsetY, int mouseX, int mouseY);
}