package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.mob.BotEntity;

public class BotRenderer extends BipedEntityRenderer<BotEntity, BipedEntityModel<BotEntity>> {
    private static final Identifier TEXTURE = new Identifier(FlexibleSomethingMod.MOD_ID, "textures/entity/bot/bot.png");

    public BotRenderer(EntityRendererFactory.Context ctx) {
        this(ctx, new BipedEntityModel<>(ctx.getPart(EntityModelLayers.PLAYER)), 0.5f);
    }

    public BotRenderer(EntityRendererFactory.Context ctx, BipedEntityModel<BotEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    public BotRenderer(EntityRendererFactory.Context ctx, BipedEntityModel<BotEntity> model, float shadowRadius, float scaleX, float scaleY, float scaleZ) {
        super(ctx, model, shadowRadius, scaleX, scaleY, scaleZ);
    }

    @Override
    public Identifier getTexture(BotEntity mobEntity) {
        return TEXTURE;
    }
}
