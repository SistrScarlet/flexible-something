package net.sistr.flexiblesomething.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.sistr.flexiblesomething.FlexibleSomethingMod;

import static net.minecraft.client.render.RenderPhase.*;

public class GreedRenderers {
    public static final Identifier TEXTURE_64 = new Identifier(FlexibleSomethingMod.MOD_ID, "textures/entity/greed/greed_64.png");
    public static final Identifier TEXTURE_64_32 = new Identifier(FlexibleSomethingMod.MOD_ID, "textures/entity/greed/greed_64_32.png");
    public static final Identifier TEXTURE_64_128 = new Identifier(FlexibleSomethingMod.MOD_ID, "textures/entity/greed/greed_64_128.png");

    public static RenderLayer getGreedRenderLayer(Identifier texture, int age) {
        float deltaAge = age + MinecraftClient.getInstance().getTickDelta();
        return RenderLayer.of("greed", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS, 256, false, true,
                RenderLayer.MultiPhaseParameters.builder().shader(ENERGY_SWIRL_SHADER)
                        .texture(new RenderPhase.Texture(texture, false, false))
                        .texturing(new RenderPhase.OffsetTexturing(MathHelper.cos(deltaAge * 0.005f) % 1.0f, (deltaAge * 0.00005f) % 1.0f))
                        .transparency(NO_TRANSPARENCY).cull(DISABLE_CULLING)
                        .lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR)
                        .build(true));
    }
}
