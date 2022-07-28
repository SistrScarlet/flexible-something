package net.sistr.flexiblesomething.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.entity.GreedChunkEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {
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
            var rand = this.getRandom();
            int x = (int) (this.getX() + (rand.nextFloat() * 2 - 1) * 64);
            int y = this.world.getHeight();
            int z = (int) (this.getZ() + (rand.nextFloat() * 2 - 1) * 64);
            var greedChunk = new GreedChunkEntity(this.world);
            greedChunk.setPosition(x, y, z);
            this.world.spawnEntity(greedChunk);
            if (this.world.isClient) {
                for (int i = 0; i < 500; i++) {
                    this.world.addParticle(
                            new DustParticleEffect(new Vec3f(0, 0, 0), 1f), true,
                            this.getX() + (rand.nextFloat() * 2 - 1) * 64,
                            y,
                            this.getZ() + (rand.nextFloat() * 2 - 1) * 64,
                            0, 0, 0);
                }
            }
        }
    }

}
