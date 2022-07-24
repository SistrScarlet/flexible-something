package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EndermanEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreedEndermanEntityRenderer extends EndermanEntityRenderer {
    public GreedEndermanEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        features.removeIf(f -> f instanceof EndermanEyesFeatureRenderer);
    }

    @Override
    public Identifier getTexture(EndermanEntity endermanEntity) {
        return GreedRenderers.TEXTURE_64_32;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(EndermanEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}
