package net.sistr.flexiblesomething.client.screen.skill;

import net.minecraft.client.gui.screen.Screen;
import net.sistr.flexiblesomething.client.skill.ClientSkill;
import net.sistr.flexiblesomething.client.skill.ClientSkillTree;

import java.util.List;
import java.util.Map;

public class SkillTreeUIReverse extends AbstractSkillTreeUI {

    public SkillTreeUIReverse(Screen parent, ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        super(parent, skillTree, entryWidth, entryHeight, entryIntervalWidth, entryIntervalHeight);
    }

    @Override
    protected void calcXYRecursion(int layer, ClientSkill clientSkill,
                                   List<Integer> layerCounts, Map<ClientSkill, RenderInfo> map) {
        int layerCount = layerCounts.get(layer);
        map.put(clientSkill, new RenderInfo(layerCount + clientSkill.getTreeSize() / 2, layer, entryWidth, entryHeight));
        layerCounts.set(layer, layerCount + clientSkill.getTreeSize());
        if (layer + 1 < layerCounts.size()) {
            int downLayerCount = layerCounts.get(layer + 1);
            layerCounts.set(layer + 1, Math.max(downLayerCount, layerCount - clientSkill.children.size() / 2));
        }
        clientSkill.children.forEach(cEntry -> calcXYRecursion(layer + 1, cEntry, layerCounts, map));
    }

}
