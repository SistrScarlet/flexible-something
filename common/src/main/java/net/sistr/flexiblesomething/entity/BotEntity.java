package net.sistr.flexiblesomething.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class BotEntity extends HostileEntity {

    public BotEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createBotAttribute() {
        return createHostileAttributes();
    }

}
