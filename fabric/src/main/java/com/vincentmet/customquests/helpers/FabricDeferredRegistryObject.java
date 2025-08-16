package com.vincentmet.customquests.helpers;

public class FabricDeferredRegistryObject<T> implements DeferredRegistryObject<T> {
    private final T object;

    public FabricDeferredRegistryObject(T object) {
        this.object = object;
    }
    @Override
    public T get() {
        return this.object;
    }
}
