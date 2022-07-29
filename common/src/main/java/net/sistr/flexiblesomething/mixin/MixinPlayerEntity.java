package net.sistr.flexiblesomething.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.entity.GreedRaid;
import net.sistr.flexiblesomething.util.Raidable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements Raidable {
    private GreedRaid greedRaid;

    @Shadow
    public abstract StackReference getStackReference(int mappedIndex);

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci) {
        greedRaid = new GreedRaid(world, (PlayerEntity) (Object) this);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void onWrite(NbtCompound nbt, CallbackInfo ci) {
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void onRead(NbtCompound nbt, CallbackInfo ci) {
    }

    @Override
    public void onKilledOther(ServerWorld world, LivingEntity other) {
        super.onKilledOther(world, other);
        greedRaid.killRaidEntity(other);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (this.world.isClient) {
            return;
        }
        greedRaid.tick();
    }

    @Override
    public void addRaidEntity(Entity entity) {
        this.greedRaid.addRaidEntity(entity);
    }

    @Override
    public void removeRaidEntity(Entity entity) {
        this.greedRaid.removeRaidEntity(entity);
    }

    @Override
    public boolean isRaiding() {
        return greedRaid.isRaiding();
    }

    @Override
    public int getRaidKills() {
        return greedRaid.getRaidKills();
    }
}
