package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SpiderEntityRenderer;
import net.minecraft.client.render.entity.feature.SpiderEyesFeatureRenderer;
import net.minecraft.util.Identifier;
import net.sistr.flexiblesomething.entity.mob.GreedSpiderEntity;
import org.jetbrains.annotations.Nullable;

public class GreedSpiderEntityRenderer extends SpiderEntityRenderer<GreedSpiderEntity> {
    public GreedSpiderEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        features.removeIf(f -> f instanceof SpiderEyesFeatureRenderer);
    }

    @Override
    public Identifier getTexture(GreedSpiderEntity spiderEntity) {
        return GreedRenderers.TEXTURE_64_32;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(GreedSpiderEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}
