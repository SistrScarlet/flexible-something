package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.DrownedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.DrownedOverlayFeatureRenderer;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreedDrownedEntityRenderer extends DrownedEntityRenderer {
    public GreedDrownedEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.features.removeIf(p -> p instanceof DrownedOverlayFeatureRenderer);
    }

    @Override
    public Identifier getTexture(DrownedEntity zombieEntity) {
        return GreedRenderers.TEXTURE_64;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(DrownedEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}
