package net.sistr.flexiblesomething.client.model;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.sistr.flexiblesomething.FlexibleSomethingMod;

public class FEntityModelLayers {
    private static final String MAIN = "main";
    public static final EntityModelLayer BULLET =
            new EntityModelLayer(new Identifier(FlexibleSomethingMod.MOD_ID, "bullet"), MAIN);

    public static void init() {
        EntityModelLayerRegistry.register(BULLET, BulletEntityModel::getTexturedModelData);
    }

}
