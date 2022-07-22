package net.sistr.flexiblesomething.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.sistr.flexiblesomething.client.skill.ClientSkill;
import net.sistr.flexiblesomething.client.skill.ClientSkillTree;
import net.sistr.flexiblesomething.skill.Skill;
import net.sistr.flexiblesomething.skill.SkillTree;
import net.sistr.flexiblesomething.skill.UnlockState;

import javax.annotation.Nullable;

public class S2CSkillTreeConverter {

    public static void write(SkillTree skillTree, PlayerEntity player, PacketByteBuf buf) {
        var root = skillTree.getRoot();
        writeSkill(root, null, player, buf);
        writeSkillTree(root, player, buf);
    }

    private static void writeSkillTree(Skill parent, PlayerEntity player, PacketByteBuf buf) {
        int childNum = parent.getChildren().size();
        buf.writeByte(childNum);
        parent.getChildren().forEach(s -> writeSkill(s, parent, player, buf));
        parent.getChildren().forEach(s -> writeSkillTree(s, player, buf));
    }

    private static void writeSkill(Skill skill, @Nullable Skill parent, PlayerEntity player, PacketByteBuf buf) {
        buf.writeString(skill.getName());
        UnlockState unlockState;
        if (skill.isUnlocked()) {
            unlockState = UnlockState.UNLOCKED;
        } else if ((parent == null || parent.isUnlocked()) && skill.canUnlock(player)) {
            unlockState = UnlockState.CAN_UNLOCK;
        } else {
            unlockState = UnlockState.LOCKED;
        }
        buf.writeEnumConstant(unlockState);
    }

    public static ClientSkillTree read(PacketByteBuf buf) {
        var parent = readSkill(buf);
        readSkillTree(parent, buf);
        calcSize(parent);
        return new ClientSkillTree(parent);
    }

    private static void readSkillTree(ClientSkill parent, PacketByteBuf buf) {
        int childNum = buf.readByte();
        for (int i = 0; i < childNum; i++) {
            var skill = readSkill(buf);
            skill.parent = parent;
            parent.children.add(skill);
        }
        parent.children.forEach(skill -> readSkillTree(skill, buf));
    }

    private static ClientSkill readSkill(PacketByteBuf buf) {
        var skill = new ClientSkill(buf.readString());
        skill.setUnlockState(buf.readEnumConstant(UnlockState.class));
        return skill;
    }

    private static void calcSize(ClientSkill parent) {
        parent.children.forEach(S2CSkillTreeConverter::calcSize);
        int size = parent.children.stream().mapToInt(ClientSkill::getTreeSize).sum();
        if (size <= 0) {
            size = 1;
        }
        parent.setTreeSize(size);
    }

}
