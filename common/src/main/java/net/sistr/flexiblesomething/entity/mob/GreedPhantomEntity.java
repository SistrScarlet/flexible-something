package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class GreedPhantomEntity extends PhantomEntity implements GreedEntity {
    private final GreedEntity.Impl impl = new Impl();

    public GreedPhantomEntity(EntityType<? extends PhantomEntity> entityType, World world) {
        super(entityType, world);
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
