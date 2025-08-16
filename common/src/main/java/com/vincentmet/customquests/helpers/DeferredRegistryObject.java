package com.vincentmet.customquests.helpers;

import java.util.function.Supplier;

public interface DeferredRegistryObject<T> extends Supplier<T> {
    T get();
}
