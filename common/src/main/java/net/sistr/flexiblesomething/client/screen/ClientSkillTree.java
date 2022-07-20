package net.sistr.flexiblesomething.client.screen;

import net.sistr.flexiblesomething.skilltree.Skill;
import net.sistr.flexiblesomething.skilltree.SkillTree;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.List;

public class ClientSkillTree {
    private final Entry root;

    public ClientSkillTree(SkillTree skillTree) {
        this.root = new Entry(null, skillTree.getRoot());
        init();
    }

    protected void init() {
        recursion(this.root);
        calcXY(this.root);
    }

    //描画用のツリーを形成
    protected void recursion(Entry entry) {
        entry.skill.getChildren().forEach(cSkill -> {
            var cEntry = new Entry(entry, cSkill);
            entry.children.add(cEntry);
            recursion(cEntry);
        });
        int size = entry.children.stream().mapToInt(e -> e.size).sum();
        if (size <= 0) {
            size = 1;
        }
        entry.size = size;
    }

    protected void calcXY(Entry entry) {
        List<Integer> layerCounts = Lists.newArrayList();
        int depth = 1;
        depth = countDepth(entry, depth);
        for (int i = 0; i < depth; i++) {
            layerCounts.add(0);
        }
        calcXYRecursion(0, entry, layerCounts);
    }

    protected int countDepth(Entry entry, int depth) {
        return entry.children.stream()
                .mapToInt(e -> countDepth(e, depth + 1))
                .max().orElse(depth);
    }

    protected void calcXYRecursion(int layer, Entry entry, List<Integer> layerCounts) {
        entry.children.forEach(cEntry -> calcXYRecursion(layer + 1, cEntry, layerCounts));
        //x座標を出すために深さ優先で項目を選んでレイヤーごとにxを加算
        //レイヤーに加算する数は項目数*2-1
        //レイヤー+項目数-1がx座標
        //todo 加算する数-1アリナシで変わる、けどもっと詰める設定も欲しい
        int size = entry.size;
        int layerCount = layerCounts.get(layer);
        entry.x = layerCount + size - 1;
        layerCounts.set(layer, layerCount + size * 2);
        //下位レイヤーは常に現在レイヤー以上でなければならない
        //下位レイヤーが現在レイヤー未満の場合、現在レイヤーの値に書き換える
        for (int i = layer; i < (layerCounts.size() - 1); i++) {
            int count = layerCounts.get(i);
            if (layerCounts.get(i + 1) < count) {
                layerCounts.set(i + 1, count);
            }
        }
        entry.y = layer;
    }

    public Entry getRoot() {
        return root;
    }

    public static class Entry {
        @Nullable
        public final Entry parent;
        public final Skill skill;
        public List<Entry> children = Lists.newArrayList();
        public int size = -1;
        public int x;
        public int y;

        public Entry(@Nullable Entry parent, Skill skill) {
            this.parent = parent;
            this.skill = skill;
        }
    }
}
