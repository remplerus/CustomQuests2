package com.vincentmet.customquests;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public class Constants {

	public static final String MODID = "customquests";
	public static final String MOD_NAME = "Custom Quests";
	public static final String VERSION_MOD = "2.4.0";
	public static final String VERSION_MC = "1.20.1";
	public static final String VERSION_COMBINED = VERSION_MC + "-" + VERSION_MOD;
	public static Path currentWorldDirectory;
	public static Path currentProgressDirectory;
	public static Path questsBackupDirectory;
	public static Path progressBackupDirectory;
	public static MinecraftServer currentServerInstance;
	public static final int NO_PARTY = -1;
	public static final ResourceLocation INVALID_RESOURCELOCATION = new ResourceLocation(MODID, "invalid");

	public static final String FILENAME_QUESTS = "Quests";
	public static final String FILENAME_PARTIES = "Parties";
	public static final String FILE_EXT_JSON = ".json";
}