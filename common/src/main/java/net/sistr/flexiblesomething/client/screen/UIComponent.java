package net.sistr.flexiblesomething.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public class UIComponent {
    private int x;
    private int y;
    private int z;
    private Identifier texture;
    private int width;
    private int height;
    private float u1;
    private float v1;
    private float u2;
    private float v2;

    public UIComponent(int x, int y, int width, int height,
                       Identifier texture, int u, int v, int textureWidth, int textureHeight) {
        this(x, y, width, height,
                (float) u / textureWidth, (float) v / textureHeight,
                (float) (u + width) / textureWidth, (float) (v + height) / textureHeight, texture);
    }

    public UIComponent(int x, int y, int width, int height,
                       float u1, float v1, float u2, float v2, Identifier texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
    }

    public boolean isInPoint(double x, double y) {
        return this.x <= x && x < this.x + width
                && this.y <= y && y < this.y + height;
    }

    public void render(MatrixStack matrices) {
        setTexture();
        drawTexture(matrices.peek().getPositionMatrix(),
                x, x + width, y, y + height, 0,
                u1, u2, v1, v2);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    private void setTexture() {
        RenderSystem.setShaderTexture(0, this.texture);
    }

    private static void drawTexture(Matrix4f matrix, int x0, int x1, int y0, int y1,
                                    int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, x0, y1, z).texture(u0, v1).next();
        bufferBuilder.vertex(matrix, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix, x1, y0, z).texture(u1, v0).next();
        bufferBuilder.vertex(matrix, x0, y0, z).texture(u0, v0).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
}
