package net.sistr.flexiblesomething.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.Heightmap;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.setup.ClientSetup;
import net.sistr.flexiblesomething.setup.ModSetup;
import net.sistr.flexiblesomething.setup.Registration;

public class FlexibleSomethingModFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        FlexibleSomethingMod.init();
        ModSetup.init();
        SpawnRestrictionAccessor.callRegister(Registration.BOT_ENTITY.get(),
                SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                HostileEntity::canSpawnInDark);
    }

    @Override
    public void onInitializeClient() {
        ClientSetup.init();
    }
}
