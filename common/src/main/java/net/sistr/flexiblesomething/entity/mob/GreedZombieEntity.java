package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class GreedZombieEntity extends ZombieEntity implements GreedEntity {
    private final GreedEntity.Impl impl = new Impl();

    public GreedZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public GreedZombieEntity(World world) {
        super(world);
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
