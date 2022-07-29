package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PhantomEntityRenderer;
import net.minecraft.client.render.entity.feature.PhantomEyesFeatureRenderer;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreedPhantomEntityRenderer extends PhantomEntityRenderer {
    public GreedPhantomEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.features.removeIf(p -> p instanceof PhantomEyesFeatureRenderer);
    }

    @Override
    public Identifier getTexture(PhantomEntity phantomEntity) {
        return GreedRenderers.TEXTURE_64;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(PhantomEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}
