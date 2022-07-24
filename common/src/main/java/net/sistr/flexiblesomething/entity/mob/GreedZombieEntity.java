package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

public class GreedZombieEntity extends ZombieEntity implements GreedEntity {
    public GreedZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public GreedZombieEntity(World world) {
        super(world);
    }
}
