package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class GreedWitchEntity extends WitchEntity implements GreedEntity {
    private final GreedEntity.Impl impl = new Impl();

    public GreedWitchEntity(EntityType<? extends WitchEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        GreedEntity.overrideTargetGoal(this, this.targetSelector);
    }

    @Override
    public Optional<LivingEntity> getGreedTarget() {
        return impl.getGreedTarget();
    }

    @Override
    public void setGreedTarget(@Nullable LivingEntity target) {
        impl.setGreedTarget(target);
    }
}
