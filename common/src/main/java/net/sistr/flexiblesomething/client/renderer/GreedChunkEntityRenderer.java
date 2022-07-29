package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.GreedChunkEntity;

import java.util.Random;

public class GreedChunkEntityRenderer extends EntityRenderer<GreedChunkEntity> {
    public static final Identifier TEXTURE =
            new Identifier(FlexibleSomethingMod.MOD_ID, "textures/block/greed_chunk.png");

    public GreedChunkEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(GreedChunkEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        BlockState blockState = entity.getBlockState();
        if (blockState.getRenderType() != BlockRenderType.MODEL) {
            return;
        }
        World world = entity.getWorld();
        if (blockState == world.getBlockState(entity.getBlockPos())
                || blockState.getRenderType() == BlockRenderType.INVISIBLE) {
            return;
        }
        matrices.push();
        float width = 0.5f + entity.getSpread(tickDelta) * 0.5f;
        float height = 0.125f / (width * width);
        matrices.scale(width, height, width);
        matrices.translate(-0.5, 0, -0.5);
        BlockPos blockPos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        blockRenderManager.getModelRenderer()
                .render(
                        world,
                        blockRenderManager.getModel(blockState),
                        blockState,
                        blockPos,
                        matrices,
                        vertexConsumers.getBuffer(RenderLayers.getMovingBlockLayer(blockState)),
                        false,
                        new Random(),
                        blockState.getRenderingSeed(entity.getBlockPos()),
                        OverlayTexture.DEFAULT_UV
                );
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(GreedChunkEntity entity) {
        return TEXTURE;
    }
}
