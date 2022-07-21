package net.sistr.flexiblesomething.client.screen;

import java.util.List;
import java.util.Map;

public class SkillTreeUIAlt extends AbstractSkillTreeUI {

    public SkillTreeUIAlt(ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        super(skillTree, entryWidth, entryHeight, entryIntervalWidth, entryIntervalHeight);
    }

    @Override
    protected void calcXYRecursion(int layer, ClientSkillTree.Entry entry,
                                   List<Integer> layerCounts, Map<ClientSkillTree.Entry, RenderInfo> map) {
        entry.children.forEach(cEntry -> calcXYRecursion(layer + 1, cEntry, layerCounts, map));
        //現在レイヤーと一つ上のレイヤーをチェック
        int size = entry.size;
        int layerCount = layerCounts.get(layer);
        int upLayerCount = layer != 0 ? layerCounts.get(layer - 1) : 0;
        int max = Math.max(layerCount, upLayerCount);
        map.put(entry, new RenderInfo(max + size / 2, layer, entryWidth, entryHeight));
        layerCounts.set(layer, layerCount + size);
    }

}
