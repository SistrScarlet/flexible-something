package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.sistr.flexiblesomething.entity.goal.GreedTargetGoal;

import javax.annotation.Nullable;
import java.util.Optional;

public interface GreedEntity {

    static <T extends PathAwareEntity & GreedEntity> void overrideTargetGoal(T mob, GoalSelector targetSelector) {
        targetSelector.clear();
        targetSelector.add(0, new RevengeGoal(mob, GreedEntity.class));
        targetSelector.add(1, new GreedTargetGoal<>(mob));
    }

    Optional<LivingEntity> getGreedTarget();

    void setGreedTarget(@Nullable LivingEntity target);

    class Impl implements GreedEntity {
        @Nullable
        private LivingEntity greedTarget;

        @Override
        public Optional<LivingEntity> getGreedTarget() {
            return Optional.ofNullable(this.greedTarget);
        }

        @Override
        public void setGreedTarget(@Nullable LivingEntity target) {
            this.greedTarget = target;
        }
    }

}
