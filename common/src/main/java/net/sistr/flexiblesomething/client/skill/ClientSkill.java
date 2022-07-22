package net.sistr.flexiblesomething.client.skill;

import net.sistr.flexiblesomething.skill.UnlockState;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.Nullable;
import java.util.List;

public class ClientSkill {
    public final String name;
    @Nullable
    public ClientSkill parent;
    public final List<ClientSkill> children = Lists.newArrayList();
    private int treeSize = -1;
    private UnlockState unlockState = UnlockState.LOCKED;

    public ClientSkill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUnlockState(UnlockState unlockState) {
        this.unlockState = unlockState;
    }

    public UnlockState getUnlockState() {
        return unlockState;
    }

    public int getTreeSize() {
        return treeSize;
    }

    public void setTreeSize(int treeSize) {
        this.treeSize = treeSize;
    }
}
