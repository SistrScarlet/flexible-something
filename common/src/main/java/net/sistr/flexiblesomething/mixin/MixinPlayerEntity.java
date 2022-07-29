package net.sistr.flexiblesomething.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.entity.GreedChunkEntity;
import net.sistr.flexiblesomething.util.Raidable;
import org.apache.commons.compress.utils.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Raidable {
    private final List<Entity> raidingGreed = Lists.newArrayList();

    @Shadow
    public abstract StackReference getStackReference(int mappedIndex);

    private boolean isRaidedByGreed;

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void onWrite(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("isRaidedByGreed", isRaidedByGreed);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void onRead(NbtCompound nbt, CallbackInfo ci) {
        isRaidedByGreed = nbt.getBoolean("isRaidedByGreed");
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        this.isRaidedByGreed = this.getOffHandStack().getItem() == Items.CLOCK;
        if (isRaidedByGreed) {
            raidingGreed.removeIf(Entity::isRemoved);
            if (raidingGreed.size() < 300) {
                var rand = this.getRandom();
                int x = (int) (this.getX() + (rand.nextFloat() * 2 - 1) * 64);
                int y = this.world.getHeight();
                int z = (int) (this.getZ() + (rand.nextFloat() * 2 - 1) * 64);
                var greedChunk = new GreedChunkEntity(this.world);
                greedChunk.setTargetPlayer((PlayerEntity) (Object) this);
                greedChunk.setPosition(x, y, z);
                this.world.spawnEntity(greedChunk);
                this.raidingGreed.add(greedChunk);
            }
        }
    }

    @Override
    public void addRaidEntity(Entity entity) {
        this.raidingGreed.add(entity);
    }

    @Override
    public void removeRaidEntity(Entity entity) {
        this.raidingGreed.remove(entity);
    }

    @Override
    public boolean isRaiding() {
        return this.isRaidedByGreed;
    }
}
