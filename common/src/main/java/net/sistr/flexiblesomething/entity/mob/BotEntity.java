package net.sistr.flexiblesomething.entity.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.entity.goal.SimpleShootGoal;
import net.sistr.flexiblesomething.setup.Registration;
import org.jetbrains.annotations.Nullable;

public class BotEntity extends HostileEntity {

    public BotEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createBotAttribute() {
        return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24);
    }

    @Override
    protected void initGoals() {
        int priority = -1;
        this.goalSelector.add(++priority, new SwimGoal(this));
        this.goalSelector.add(++priority, new SimpleShootGoal(this));
        this.goalSelector.add(++priority, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(++priority, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(++priority, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(++priority, new LookAroundGoal(this));

        priority = -1;
        this.targetSelector.add(++priority, new RevengeGoal(this));
        this.targetSelector.add(++priority, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        this.initEquipment(difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Registration.TEST_GUN.get()));
    }
}
