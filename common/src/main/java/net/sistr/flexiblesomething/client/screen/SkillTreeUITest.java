package net.sistr.flexiblesomething.client.screen;

import java.util.List;
import java.util.Map;

public class SkillTreeUITest extends AbstractSkillTreeUI {

    public SkillTreeUITest(ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        super(skillTree, entryWidth, entryHeight, entryIntervalWidth, entryIntervalHeight);
    }

    @Override
    protected void calcXYRecursion(int layer, ClientSkillTree.Entry entry,
                                   List<Integer> layerCounts, Map<ClientSkillTree.Entry, RenderInfo> map) {
        int layerCount = layerCounts.get(layer);
        map.put(entry, new RenderInfo(layerCount + entry.size / 2, layer, entryWidth, entryHeight));
        layerCounts.set(layer, layerCount + entry.size);
        if (layer + 1 < layerCounts.size()) {
            int downLayerCount = layerCounts.get(layer + 1);
            layerCounts.set(layer + 1, Math.max(downLayerCount, layerCount - entry.children.size() / 2));
        }
        entry.children.forEach(cEntry -> calcXYRecursion(layer + 1, cEntry, layerCounts, map));
    }

}
