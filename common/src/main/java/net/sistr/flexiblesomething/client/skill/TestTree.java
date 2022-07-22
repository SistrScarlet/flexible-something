package net.sistr.flexiblesomething.client.skill;

import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.sistr.flexiblesomething.network.S2CSkillTreeConverter;
import net.sistr.flexiblesomething.skill.SkillTree;

public class TestTree {

    public static ClientSkillTree conv(SkillTree skillTree) {
        var buf = new PacketByteBuf(Unpooled.buffer());
        S2CSkillTreeConverter.write(skillTree, MinecraftClient.getInstance().player, buf);
        return S2CSkillTreeConverter.read(buf);
    }

    public static SkillTree buildTree() {
        var builder = SkillTree.Builder.createBuilder();

        var root = SkillTree.Builder.createSkillBuilder("root");
        builder.setRoot(root);

        var l1e0 = SkillTree.Builder.createSkillBuilder("l1e0");
        root.addEntry(l1e0);
        var l2e0 = SkillTree.Builder.createSkillBuilder("l2e0");
        l1e0.addEntry(l2e0);

        var l2e1 = SkillTree.Builder.createSkillBuilder("l2e1");
        l1e0.addEntry(l2e1);

        var l3e0 = SkillTree.Builder.createSkillBuilder("l3e0");
        l2e1.addEntry(l3e0);
        var l4e0 = SkillTree.Builder.createSkillBuilder("l4e0");
        l3e0.addEntry(l4e0);
        var l4e1 = SkillTree.Builder.createSkillBuilder("l4e1");
        l3e0.addEntry(l4e1);

        var l3e1 = SkillTree.Builder.createSkillBuilder("l3e1");
        l2e1.addEntry(l3e1);
        var l4e2 = SkillTree.Builder.createSkillBuilder("l4e2");
        l3e1.addEntry(l4e2);

        var l1e1 = SkillTree.Builder.createSkillBuilder("l1e1");
        root.addEntry(l1e1);

        var l1e2 = SkillTree.Builder.createSkillBuilder("l1e2");
        root.addEntry(l1e2);

        var l2e2 = SkillTree.Builder.createSkillBuilder("l2e2");
        l1e2.addEntry(l2e2);
        var l3e2 = SkillTree.Builder.createSkillBuilder("l3e2");
        l2e2.addEntry(l3e2);

        var l3e3 = SkillTree.Builder.createSkillBuilder("l3e3");
        l2e2.addEntry(l3e3);
        var l4e3 = SkillTree.Builder.createSkillBuilder("l4e3");
        l3e3.addEntry(l4e3);
        var l5e0 = SkillTree.Builder.createSkillBuilder("l5e0");
        l4e3.addEntry(l5e0);
        var l6e0 = SkillTree.Builder.createSkillBuilder("l6e0");
        l5e0.addEntry(l6e0);
        var l6e1 = SkillTree.Builder.createSkillBuilder("l6e1");
        l5e0.addEntry(l6e1);
        var l7e0 = SkillTree.Builder.createSkillBuilder("l7e0");
        l6e1.addEntry(l7e0);

        var l3e4 = SkillTree.Builder.createSkillBuilder("l3e4");
        l2e2.addEntry(l3e4);
        var l4e4 = SkillTree.Builder.createSkillBuilder("l4e4");
        l3e4.addEntry(l4e4);
        var l4e5 = SkillTree.Builder.createSkillBuilder("l4e5");
        l3e4.addEntry(l4e5);
        var l4e6 = SkillTree.Builder.createSkillBuilder("l4e6");
        l3e4.addEntry(l4e6);

        var l3e5 = SkillTree.Builder.createSkillBuilder("l3e5");
        l2e2.addEntry(l3e5);

        var l2e3 = SkillTree.Builder.createSkillBuilder("l2e3");
        l1e2.addEntry(l2e3);
        var l2e4 = SkillTree.Builder.createSkillBuilder("l2e4");
        l1e2.addEntry(l2e4);

        return builder.build();
    }

}
