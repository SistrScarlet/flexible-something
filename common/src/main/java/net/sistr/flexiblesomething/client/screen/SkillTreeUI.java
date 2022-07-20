package net.sistr.flexiblesomething.client.screen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class SkillTreeUI {
    private final ClientSkillTree skillTree;
    private final int entryWidth;
    private final int entryHeight;
    private final int entryIntervalWidth;
    private final int entryIntervalHeight;

    public SkillTreeUI(ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        this.skillTree = skillTree;
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
        this.entryIntervalWidth = entryIntervalWidth;
        this.entryIntervalHeight = entryIntervalHeight;
    }

    public void render(MatrixStack matrices, TextRenderer textRenderer, int x, int y) {
        renderRecursion(matrices, textRenderer, x, y, this.skillTree.getRoot());
    }

    protected void renderRecursion(MatrixStack matrices, TextRenderer textRenderer, int x, int y, ClientSkillTree.Entry entry) {
        int width = entryWidth + entryIntervalWidth;
        int height = entryHeight + entryIntervalHeight;
        entry.children.forEach(child -> {
            int line = 2;
            //横棒
            if (entry.x != child.x) {
                if (entry.x < child.x) {
                    DrawableHelper.fill(matrices,
                            x + entry.x * width + entryWidth,
                            y + entry.y * height + (entryHeight - line) / 2,
                            x + child.x * width + (entryWidth + line) / 2,
                            y + entry.y * height + (entryHeight + line) / 2, 0xFF808080);
                } else {
                    DrawableHelper.fill(matrices,
                            x + child.x * width + (entryWidth - line) / 2,
                            y + entry.y * height + (entryHeight - line) / 2,
                            x + entry.x * width,
                            y + entry.y * height + (entryHeight + line) / 2, 0xFF808080);
                }
            }
            //縦棒
            DrawableHelper.fill(matrices,
                    x + child.x * width + (entryWidth - line) / 2,
                    y + child.y * height,
                    x + child.x * width + (entryWidth + line) / 2,
                    y + entry.y * height + (entryHeight - line) / 2, 0xFF808080);
        });

        DrawableHelper.fill(matrices,
                x + entry.x * width, y + entry.y * height,
                x + entry.x * width + entryWidth, y + entry.y * height + entryHeight, 0xFFFFFF00);

        textRenderer.drawWithShadow(matrices, entry.skill.getName(),
                x + entry.x * width, y + entry.y * height + textRenderer.fontHeight, 0xFFFFFF);
        if (entry.parent != null) {
            textRenderer.drawWithShadow(matrices, entry.parent.skill.getName(),
                    x + entry.x * width, y + entry.y * height, 0xFFFFFF);
        }
        entry.children.forEach(cEntry -> renderRecursion(matrices, textRenderer, x, y, cEntry));
    }

}
