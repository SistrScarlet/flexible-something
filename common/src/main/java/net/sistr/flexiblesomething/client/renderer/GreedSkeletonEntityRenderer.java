package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreedSkeletonEntityRenderer extends SkeletonEntityRenderer {
    public GreedSkeletonEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(AbstractSkeletonEntity abstractSkeletonEntity) {
        return GreedRenderers.TEXTURE_64_32;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(AbstractSkeletonEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}