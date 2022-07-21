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

    public Entry getRoot() {
        return root;
    }

    public static class Entry {
        @Nullable
        public final Entry parent;
        public final Skill skill;
        public List<Entry> children = Lists.newArrayList();
        public int size = -1;

        public Entry(@Nullable Entry parent, Skill skill) {
            this.parent = parent;
            this.skill = skill;
        }
    }
}
