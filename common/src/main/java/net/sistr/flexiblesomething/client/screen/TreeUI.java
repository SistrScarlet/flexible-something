package net.sistr.flexiblesomething.client.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface TreeUI {
    void render(MatrixStack matrices, TextRenderer textRenderer, int x, int y);
}
