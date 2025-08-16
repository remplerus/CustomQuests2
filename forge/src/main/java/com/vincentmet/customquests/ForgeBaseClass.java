package com.vincentmet.customquests;

import com.vincentmet.customquests.command.CQCommand;
import com.vincentmet.customquests.network.messages.PacketHandler;
import com.vincentmet.customquests.platform.ForgeRegistryHelper;
import com.vincentmet.customquests.standardcontent.StandardContentRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
public class ForgeBaseClass {
    public ForgeBaseClass(){
		CommonBaseClass.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		Constants.PATH_CONFIG = FMLPaths.CONFIGDIR.get().resolve(Constants.MODID);
		modEventBus.addListener(this::setupCommon);
		MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStartup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopping);
        Objects.loadClass(modEventBus);
	}
	
	private void setupCommon(final FMLCommonSetupEvent event){
    	//Main
		Constants.questsBackupDirectory = FMLPaths.CONFIGDIR.get().resolve("customquests").resolve("backups");
		Config.readConfigToMemory(Constants.PATH_CONFIG, "config.json");
		PacketHandler.init();
		
		//Standard Content
		StandardContentRegistry.registerTaskTypes();
		StandardContentRegistry.registerRewardTypes();
		StandardContentRegistry.registerButtonShapes();
		StandardContentRegistry.registerTextTypes();
		StandardContentRegistry.registerEditorFieldTypes();
	}
	
	private void serverStopping(final ServerStoppingEvent event){
    	Config.writeConfigToDisk(Constants.PATH_CONFIG, "config.json");
	}
	
	private void serverStartup(final ServerStartingEvent event){
    	//Main
		Constants.currentServerInstance = event.getServer();
        CQCommand.register(event.getServer().getCommands().getDispatcher());
	}
}