package net.sistr.flexiblesomething.client.screen.skill;

import net.minecraft.client.gui.screen.Screen;
import net.sistr.flexiblesomething.client.skill.ClientSkill;
import net.sistr.flexiblesomething.client.skill.ClientSkillTree;

import java.util.List;
import java.util.Map;

public class SkillTreeUIAlt extends AbstractSkillTreeUI {

    public SkillTreeUIAlt(Screen parent, ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        super(parent, skillTree, entryWidth, entryHeight, entryIntervalWidth, entryIntervalHeight);
    }

    @Override
    protected void calcXYRecursion(int layer, ClientSkill clientSkill,
                                   List<Integer> layerCounts, Map<ClientSkill, RenderInfo> map) {
        clientSkill.children.forEach(cEntry -> calcXYRecursion(layer + 1, cEntry, layerCounts, map));
        //現在レイヤーと一つ上のレイヤーをチェック
        int size = clientSkill.getTreeSize();
        int layerCount = layerCounts.get(layer);
        int upLayerCount = layer != 0 ? layerCounts.get(layer - 1) : 0;
        int max = Math.max(layerCount, upLayerCount);
        map.put(clientSkill, new RenderInfo(max + size / 2, layer, entryWidth, entryHeight));
        layerCounts.set(layer, layerCount + size);
    }

}
