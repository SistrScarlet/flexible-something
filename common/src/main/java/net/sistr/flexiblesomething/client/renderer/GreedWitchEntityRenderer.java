package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.WitchEntityRenderer;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreedWitchEntityRenderer extends WitchEntityRenderer {
    public GreedWitchEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(WitchEntity witchEntity) {
        return GreedRenderers.TEXTURE_64_128;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(WitchEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}
