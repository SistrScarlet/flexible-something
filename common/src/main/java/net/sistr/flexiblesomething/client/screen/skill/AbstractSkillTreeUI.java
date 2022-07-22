package net.sistr.flexiblesomething.client.screen.skill;

import com.google.common.collect.Maps;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.sistr.flexiblesomething.client.skill.ClientSkill;
import net.sistr.flexiblesomething.client.skill.ClientSkillTree;
import org.apache.commons.compress.utils.Lists;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public abstract class AbstractSkillTreeUI {
    protected final Screen parent;
    protected final Map<ClientSkill, RenderInfo> map;
    protected final ClientSkillTree skillTree;
    protected final int entryWidth;
    protected final int entryHeight;
    protected final int entryIntervalWidth;
    protected final int entryIntervalHeight;
    @Nullable
    protected ClientSkill selectEntry;
    protected int clickTimer;
    protected double x;
    protected double y;
    protected float scale = 1f;

    public AbstractSkillTreeUI(Screen parent, ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        this.parent = parent;
        this.skillTree = skillTree;
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
        this.entryIntervalWidth = entryIntervalWidth;
        this.entryIntervalHeight = entryIntervalHeight;
        this.map = calcXY(skillTree.getRoot());
    }

    protected Map<ClientSkill, RenderInfo> calcXY(ClientSkill clientSkill) {
        Map<ClientSkill, RenderInfo> map = Maps.newHashMap();
        List<Integer> layerCounts = Lists.newArrayList();
        int depth = 1;
        depth = countDepth(clientSkill, depth);
        for (int i = 0; i < depth; i++) {
            layerCounts.add(0);
        }
        calcXYRecursion(0, clientSkill, layerCounts, map);
        return map;
    }

    protected final int countDepth(ClientSkill clientSkill, int depth) {
        return clientSkill.children.stream()
                .mapToInt(e -> countDepth(e, depth + 1))
                .max().orElse(depth);
    }

    protected abstract void calcXYRecursion(int layer, ClientSkill clientSkill,
                                            List<Integer> layerCounts, Map<ClientSkill, RenderInfo> map);

    public void render(MatrixStack matrices, TextRenderer textRenderer) {
        matrices.push();
        matrices.translate(this.parent.width / 2f, this.parent.height / 2f, 0);
        matrices.scale(scale, scale, scale);
        matrices.translate(-this.parent.width / 2f, -this.parent.height / 2f, 0);
        matrices.translate(x, y, 0);
        renderLineRecursion(matrices, this.skillTree.getRoot());
        renderEntryRecursion(matrices, textRenderer, this.skillTree.getRoot());
        matrices.pop();
    }

    protected void renderLineRecursion(MatrixStack matrices, ClientSkill clientSkill) {
        int width = entryWidth + entryIntervalWidth;
        int height = entryHeight + entryIntervalHeight;
        RenderInfo info = map.get(clientSkill);
        //todo 細かい調整要らん？
        clientSkill.children.forEach(child -> {
            int line = 2;
            RenderInfo cInfo = map.get(child);
            //横棒
            int y1 = info.y * height + (entryHeight - line) / 2;
            int x1 = cInfo.x * width + (entryWidth + line) / 2;
            int x2 = cInfo.x * width + (entryWidth - line) / 2;
            if (info.x != cInfo.x) {
                int y2 = info.y * height + (entryHeight + line) / 2;
                if (info.x < cInfo.x) {
                    DrawableHelper.fill(matrices,
                            info.x * width + entryWidth,
                            y1,
                            x1,
                            y2, 0xFF808080);
                } else {
                    DrawableHelper.fill(matrices,
                            x2,
                            y1,
                            info.x * width,
                            y2, 0xFF808080);
                }
            }
            //縦棒
            DrawableHelper.fill(matrices,
                    x2,
                    cInfo.y * height,
                    x1,
                    y1, 0xFF808080);
        });
        clientSkill.children.forEach(cEntry -> renderLineRecursion(matrices, cEntry));
    }

    protected void renderEntryRecursion(MatrixStack matrices, TextRenderer textRenderer, ClientSkill clientSkill) {
        renderEntry(matrices, textRenderer, clientSkill);
        clientSkill.children.forEach(cEntry -> renderEntryRecursion(matrices, textRenderer, cEntry));
    }

    protected void renderEntry(MatrixStack matrices, TextRenderer textRenderer, ClientSkill clientSkill) {
        int width = entryWidth + entryIntervalWidth;
        int height = entryHeight + entryIntervalHeight;
        RenderInfo info = map.get(clientSkill);

        boolean select = this.selectEntry == clientSkill;

        DrawableHelper.fill(matrices,
                info.x * width, info.y * height,
                info.x * width + info.width, info.y * height + info.height,
                select ? 0xFF0000FF : 0xFFFFFF00);

        textRenderer.drawWithShadow(matrices, clientSkill.getName(),
                info.x * width, info.y * height + textRenderer.fontHeight, 0xFFFFFF);
        if (clientSkill.parent != null) {
            textRenderer.drawWithShadow(matrices, clientSkill.parent.getName(),
                    info.x * width, info.y * height, 0xFFFFFF);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_1) {
            return false;
        }
        double centerX = this.parent.width / 2f;
        double centerY = this.parent.height / 2f;
        boolean result = map.entrySet().stream().anyMatch(e -> {
            var info = e.getValue();
            double width = entryWidth + entryIntervalWidth;
            double height = entryHeight + entryIntervalHeight;
            double x1 = (this.x + info.x * width - centerX) * scale + centerX;
            double x2 = x1 + info.width * scale;
            double y1 = (this.y + info.y * height - centerY) * scale + centerY;
            double y2 = y1 + info.height * scale;
            if (x1 <= mouseX && mouseX < x2
                    && y1 <= mouseY && mouseY < y2) {
                if (this.selectEntry == e.getKey()) {
                    //選択状態かつダブルクリックなら実行
                    if (0 < this.clickTimer) {
                        execEntry(this.selectEntry);
                    }
                } else {
                    this.selectEntry = e.getKey();
                }
                this.clickTimer = 10;
                return true;
            }
            return false;
        });
        if (!result) {
            this.selectEntry = null;
        }
        return result;
    }

    protected void execEntry(ClientSkill clientSkill) {

    }

    public void tick() {
        if (0 < clickTimer) {
            clickTimer--;
        }
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public record RenderInfo(int x, int y, int width, int height) {
    }

}
