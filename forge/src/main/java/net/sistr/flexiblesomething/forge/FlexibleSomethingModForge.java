package net.sistr.flexiblesomething.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sistr.flexiblesomething.FlexibleSomethingMod;

@Mod(FlexibleSomethingMod.MOD_ID)
public class FlexibleSomethingModForge {
    public FlexibleSomethingModForge() {
        EventBuses.registerModEventBus(FlexibleSomethingMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FlexibleSomethingMod.init();
    }
}
