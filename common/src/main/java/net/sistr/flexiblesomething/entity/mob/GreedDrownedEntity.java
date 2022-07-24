package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.world.World;

public class GreedDrownedEntity extends DrownedEntity implements GreedEntity {
    public GreedDrownedEntity(EntityType<? extends DrownedEntity> entityType, World world) {
        super(entityType, world);
    }
}
