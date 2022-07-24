package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.world.World;

public class GreedWitchEntity extends WitchEntity implements GreedEntity {
    public GreedWitchEntity(EntityType<? extends WitchEntity> entityType, World world) {
        super(entityType, world);
    }


}
