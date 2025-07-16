package com.vincentmet.customquests;

import net.fabricmc.api.ModInitializer;

public class FabricBaseClass implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonBaseClass.init();
    }
}
