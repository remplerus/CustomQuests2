package com.vincentmet.customquests.platform;

import com.vincentmet.customquests.helpers.DeferredRegistryObject;
import com.vincentmet.customquests.helpers.ForgeDeferredRegistryObject;
import com.vincentmet.customquests.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <T, U extends T> DeferredRegistryObject<U> register(Registry<T> objRegistry, String objName, Supplier<U> objSupplier) {
        DeferredRegister<T> registry = ForgeRegistryHelper.deferredRegisterFor(objRegistry);
        return new ForgeDeferredRegistryObject<>(registry.register(objName, objSupplier));
    }
}