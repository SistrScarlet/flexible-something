package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreedZombieEntityRenderer extends ZombieEntityRenderer {
    public GreedZombieEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return GreedRenderers.TEXTURE_64;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(ZombieEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        if (!translucent && showBody) {
            return GreedRenderers.getGreedRenderLayer(getTexture(entity), entity.age);
        }
        return super.getRenderLayer(entity, showBody, translucent, showOutline);
    }
}
