package net.sistr.flexiblesomething.setup;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.sistr.flexiblesomething.client.model.FEntityModelLayers;
import net.sistr.flexiblesomething.client.renderer.BulletEntityRenderer;

public class ClientSetup {

    public static void init() {
        FEntityModelLayers.init();
        EntityRendererRegistry.register(Registration.BASIC_BULLET, BulletEntityRenderer::new);
    }

}
