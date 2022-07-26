package net.sistr.flexiblesomething.setup;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.sistr.flexiblesomething.client.key.FlexibleKeys;
import net.sistr.flexiblesomething.client.model.FEntityModelLayers;
import net.sistr.flexiblesomething.client.renderer.*;

public class ClientSetup {

    public static void init() {
        FEntityModelLayers.init();
        EntityRendererRegistry.register(Registration.BASIC_BULLET, BulletEntityRenderer::new);
        EntityRendererRegistry.register(Registration.BOT_ENTITY, BotRenderer::new);

        EntityRendererRegistry.register(Registration.G_DROWNED, GreedDrownedEntityRenderer::new);
        EntityRendererRegistry.register(Registration.G_ENDERMAN, GreedEndermanEntityRenderer::new);
        EntityRendererRegistry.register(Registration.G_PHANTOM, GreedPhantomEntityRenderer::new);
        EntityRendererRegistry.register(Registration.G_SKELETON, GreedSkeletonEntityRenderer::new);
        EntityRendererRegistry.register(Registration.G_SPIDER, GreedSpiderEntityRenderer::new);
        EntityRendererRegistry.register(Registration.G_WITCH, GreedWitchEntityRenderer::new);
        EntityRendererRegistry.register(Registration.G_ZOMBIE, GreedZombieEntityRenderer::new);

        FlexibleKeys.init();
    }

}
