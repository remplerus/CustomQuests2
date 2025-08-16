package com.vincentmet.customquests.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

import java.util.*;

public class TooltipBuffer{
	public static final List<Runnable> tooltipBuffer = new ArrayList<>();
    public static final Font font = Minecraft.getInstance().font;
}
