package net.sistr.flexiblesomething.entity.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.sistr.flexiblesomething.entity.mob.GreedEntity;

import java.util.EnumSet;

public class GreedTargetGoal<T extends MobEntity & GreedEntity> extends Goal {
    private final T greed;

    public GreedTargetGoal(T greed) {
        this.greed = greed;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        return this.greed.getGreedTarget().isPresent();
    }

    @Override
    public void start() {
        this.greed.setTarget(this.greed.getGreedTarget().orElse(null));
    }

}
