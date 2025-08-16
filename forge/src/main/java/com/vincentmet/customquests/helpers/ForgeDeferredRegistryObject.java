package com.vincentmet.customquests.helpers;

import net.minecraftforge.registries.RegistryObject;

public class ForgeDeferredRegistryObject<T> implements DeferredRegistryObject<T> {
    private final RegistryObject<T> registryObject;
    public ForgeDeferredRegistryObject(RegistryObject<T> registryObject) {
        this.registryObject = registryObject;
    }
    @Override
    public T get() {
        return this.registryObject.get();
    }
}
