package net.sistr.flexiblesomething.client.screen.skill;

import net.minecraft.client.gui.screen.Screen;
import net.sistr.flexiblesomething.client.skill.ClientSkill;
import net.sistr.flexiblesomething.client.skill.ClientSkillTree;

import java.util.List;
import java.util.Map;

public class SkillTreeUI extends AbstractSkillTreeUI {

    public SkillTreeUI(Screen parent, ClientSkillTree skillTree, int entryWidth, int entryHeight, int entryIntervalWidth, int entryIntervalHeight) {
        super(parent, skillTree, entryWidth, entryHeight, entryIntervalWidth, entryIntervalHeight);
    }

    @Override
    protected void calcXYRecursion(int layer, ClientSkill clientSkill,
                                   List<Integer> layerCounts, Map<ClientSkill, RenderInfo> map) {
        clientSkill.children.forEach(cEntry -> calcXYRecursion(layer + 1, cEntry, layerCounts, map));
        //x座標を出すために深さ優先で項目を選んでレイヤーごとにxを加算
        //レイヤーに加算する数は項目数*2-1
        //レイヤー+項目数-1がx座標
        //todo 加算する数は-1のアリナシで変わる、けどもっと詰める設定も欲しい
        int size = clientSkill.getTreeSize();
        int layerCount = layerCounts.get(layer);
        map.put(clientSkill, new RenderInfo(layerCount + size - 1, layer, entryWidth, entryHeight));
        layerCounts.set(layer, layerCount + size * 2 - 1);
        //下位レイヤーは常に現在レイヤー以上でなければならない
        //下位レイヤーが現在レイヤー未満の場合、現在レイヤーの値に書き換える
        for (int i = layer; i < (layerCounts.size() - 1); i++) {
            int count = layerCounts.get(i);
            if (layerCounts.get(i + 1) < count) {
                layerCounts.set(i + 1, count);
            }
        }
    }

}
