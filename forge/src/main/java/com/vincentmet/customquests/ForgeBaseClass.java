package com.vincentmet.customquests;

import com.vincentmet.customquests.command.CQCommand;
import com.vincentmet.customquests.network.messages.PacketHandler;
import com.vincentmet.customquests.standardcontent.StandardContentRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

@Mod(Constants.MODID)
public class ForgeBaseClass {
	public static final Path PATH_CONFIG = FMLPaths.CONFIGDIR.get().resolve(Constants.MODID);
    public ForgeBaseClass(){
		CommonBaseClass.init();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
		MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopping);
	}
	
	private void setupCommon(final FMLCommonSetupEvent event){
    	//Main
		Constants.questsBackupDirectory = FMLPaths.CONFIGDIR.get().resolve("customquests").resolve("backups");
		Config.readConfigToMemory(PATH_CONFIG, "config.json");
		PacketHandler.init();
		
		//Standard Content
		StandardContentRegistry.registerTaskTypes();
		StandardContentRegistry.registerRewardTypes();
		StandardContentRegistry.registerButtonShapes();
		StandardContentRegistry.registerTextTypes();
		StandardContentRegistry.registerEditorFieldTypes();
	}
	
	private void serverStopping(final ServerStoppingEvent event){
    	Config.writeConfigToDisk(PATH_CONFIG, "config.json");
	}
	
	private void serverStartup(final ServerStartingEvent event){
    	//Main
		Constants.currentServerInstance = event.getServer();
        CQCommand.register(event.getServer().getCommands().getDispatcher());
	}
}