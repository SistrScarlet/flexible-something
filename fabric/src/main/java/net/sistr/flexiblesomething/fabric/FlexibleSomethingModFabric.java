package net.sistr.flexiblesomething.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.setup.ClientSetup;
import net.sistr.flexiblesomething.setup.ModSetup;

public class FlexibleSomethingModFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        FlexibleSomethingMod.init();
        ModSetup.init();
    }

    @Override
    public void onInitializeClient() {
        ClientSetup.init();
    }
}
