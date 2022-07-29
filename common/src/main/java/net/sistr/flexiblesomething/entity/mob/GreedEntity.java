package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface GreedEntity {

    static void overrideTargetGoal(PathAwareEntity mob, GoalSelector targetSelector) {
        targetSelector.clear();
        targetSelector.add(0, new RevengeGoal(mob, GreedEntity.class));
        targetSelector.add(1, new ActiveTargetGoal<>(mob, PlayerEntity.class, false));
    }

}
