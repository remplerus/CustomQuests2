package com.vincentmet.customquests.platform;

import com.vincentmet.customquests.Constants;
import com.vincentmet.customquests.helpers.DeferredRegistryObject;
import com.vincentmet.customquests.helpers.FabricDeferredRegistryObject;
import com.vincentmet.customquests.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <T, U extends T> DeferredRegistryObject<U> register(Registry<T> objRegistry, String objName, Supplier<U> objSupplier) {
        return new FabricDeferredRegistryObject<>(Registry.register(objRegistry, new ResourceLocation(Constants.MODID, objName), objSupplier.get()));
    }
}
