package net.sistr.flexiblesomething.client.screen;

import com.google.common.collect.Maps;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

public abstract class AbstractSkillTreeUI implements TreeUI {
    protected final Map<ClientSkillTree.Entry, RenderInfo> map;
    protected final ClientSkillTree skillTree;
    protected final int entryWidth;
    protected final int entryHeight;
    protected final int entryIntervalWidth;
    protected final int entryIntervalHeight;

    public AbstractSkillTreeUI(ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        this.map = calcXY(skillTree.getRoot());
        this.skillTree = skillTree;
        this.entryWidth = entryWidth;
        this.entryHeight = entryHeight;
        this.entryIntervalWidth = entryIntervalWidth;
        this.entryIntervalHeight = entryIntervalHeight;
    }

    protected Map<ClientSkillTree.Entry, RenderInfo> calcXY(ClientSkillTree.Entry entry) {
        Map<ClientSkillTree.Entry, RenderInfo> map = Maps.newHashMap();
        List<Integer> layerCounts = Lists.newArrayList();
        int depth = 1;
        depth = countDepth(entry, depth);
        for (int i = 0; i < depth; i++) {
            layerCounts.add(0);
        }
        calcXYRecursion(0, entry, layerCounts, map);
        return map;
    }

    protected int countDepth(ClientSkillTree.Entry entry, int depth) {
        return entry.children.stream()
                .mapToInt(e -> countDepth(e, depth + 1))
                .max().orElse(depth);
    }

    protected abstract void calcXYRecursion(int layer, ClientSkillTree.Entry entry,
                                            List<Integer> layerCounts, Map<ClientSkillTree.Entry, RenderInfo> map);

    public void render(MatrixStack matrices, TextRenderer textRenderer, int x, int y) {
        renderLineRecursion(matrices, textRenderer, x, y, this.skillTree.getRoot());
        renderEntryRecursion(matrices, textRenderer, x, y, this.skillTree.getRoot());
    }

    protected void renderLineRecursion(MatrixStack matrices, TextRenderer textRenderer, int x, int y, ClientSkillTree.Entry entry) {
        int width = entryWidth + entryIntervalWidth;
        int height = entryHeight + entryIntervalHeight;
        RenderInfo info = map.get(entry);
        //todo 細かい調整要らん？
        entry.children.forEach(child -> {
            int line = 2;
            RenderInfo cInfo = map.get(child);
            //横棒
            if (info.x != cInfo.x) {
                if (info.x < cInfo.x) {
                    DrawableHelper.fill(matrices,
                            x + info.x * width + entryWidth,
                            y + info.y * height + (entryHeight - line) / 2,
                            x + cInfo.x * width + (entryWidth + line) / 2,
                            y + info.y * height + (entryHeight + line) / 2, 0xFF808080);
                } else {
                    DrawableHelper.fill(matrices,
                            x + cInfo.x * width + (entryWidth - line) / 2,
                            y + info.y * height + (entryHeight - line) / 2,
                            x + info.x * width,
                            y + info.y * height + (entryHeight + line) / 2, 0xFF808080);
                }
            }
            //縦棒
            DrawableHelper.fill(matrices,
                    x + cInfo.x * width + (entryWidth - line) / 2,
                    y + cInfo.y * height,
                    x + cInfo.x * width + (entryWidth + line) / 2,
                    y + info.y * height + (entryHeight - line) / 2, 0xFF808080);
        });
        entry.children.forEach(cEntry -> renderLineRecursion(matrices, textRenderer, x, y, cEntry));
    }

    protected void renderEntryRecursion(MatrixStack matrices, TextRenderer textRenderer, int x, int y, ClientSkillTree.Entry entry) {
        int width = entryWidth + entryIntervalWidth;
        int height = entryHeight + entryIntervalHeight;
        RenderInfo info = map.get(entry);

        DrawableHelper.fill(matrices,
                x + info.x * width, y + info.y * height,
                x + info.x * width + entryWidth, y + info.y * height + entryHeight, 0xFFFFFF00);

        textRenderer.drawWithShadow(matrices, entry.skill.getName(),
                x + info.x * width, y + info.y * height + textRenderer.fontHeight, 0xFFFFFF);
        if (entry.parent != null) {
            textRenderer.drawWithShadow(matrices, entry.parent.skill.getName(),
                    x + info.x * width, y + info.y * height, 0xFFFFFF);
        }
        entry.children.forEach(cEntry -> renderEntryRecursion(matrices, textRenderer, x, y, cEntry));
    }

    public record RenderInfo(int x, int y, int width, int height) {
    }

}
