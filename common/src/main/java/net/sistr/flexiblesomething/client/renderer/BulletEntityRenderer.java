package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.client.model.BulletEntityModel;
import net.sistr.flexiblesomething.client.model.FEntityModelLayers;
import net.sistr.flexiblesomething.entity.projectile.BulletEntity;

public class BulletEntityRenderer extends EntityRenderer<BulletEntity> {
    private static final Identifier TEXTURE = new Identifier(FlexibleSomethingMod.MOD_ID, "textures/entity/projectiles/bullet.png");
    private final BulletEntityModel bullet;

    public BulletEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        bullet = new BulletEntityModel(ctx.getPart(FEntityModelLayers.BULLET));
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(entity.getYaw(tickDelta) - 180f));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.getPitch(tickDelta)));

        matrices.scale(-1.0f, -1.0f, 1.0f);

        matrices.scale(0.25f, 0.25f, 0.25f);
        bullet.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(getTexture(entity))),
                light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 0.5f);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        return TEXTURE;
    }
}
