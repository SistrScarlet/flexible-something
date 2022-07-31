package net.sistr.flexiblesomething.entity.projectile;

import dev.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.setup.Registration;
import net.sistr.flexiblesomething.util.SoundData;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BulletEntity extends ProjectileEntity {
    private int decay = 100;
    private float damage = 4;
    private final List<Effect> hitEffects = Lists.newArrayList();
    private final List<Effect> killEffects = Lists.newArrayList();

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    protected BulletEntity(EntityType<? extends BulletEntity> type, double x, double y, double z, World world) {
        this(type, world);
        this.setPosition(x, y, z);
    }

    protected BulletEntity(EntityType<? extends BulletEntity> type, Entity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - (double) 0.1f, owner.getZ(), world);
        this.setOwner(owner);
    }

    public BulletEntity(double x, double y, double z, World world) {
        this(Registration.BASIC_BULLET.get(), x, y, z, world);
    }

    public BulletEntity(Entity owner, World world) {
        this(Registration.BASIC_BULLET.get(), owner, world);
    }

    @Override
    protected void initDataTracker() {

    }

    @Override
    public void tick() {
        super.tick();
        tryDespawn();

        Vec3d vec3d2;
        BlockState blockState;
        super.tick();
        boolean bl = this.isNoClip();
        Vec3d vec3d = this.getVelocity();
        if (this.prevPitch == 0.0f && this.prevYaw == 0.0f) {
            double d = vec3d.horizontalLength();
            this.setYaw((float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
            this.setPitch((float) (MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }
        blockState = this.world.getBlockState(this.getBlockPos());
        if (this.isTouchingWaterOrRain() || blockState.isOf(Blocks.POWDER_SNOW)) {
            this.extinguish();
        }
        Vec3d vec3d3 = this.getPos();
        HitResult hitResult = this.world.raycast(new RaycastContext(vec3d3, vec3d2 = vec3d3.add(vec3d),
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d2 = hitResult.getPos();
        }
        EntityHitResult entityHitResult = this.getEntityCollision(vec3d3, vec3d2);
        if (entityHitResult != null) {
            hitResult = entityHitResult;
        }
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hitResult).getEntity();
            Entity entity2 = this.getOwner();
            if (entity instanceof PlayerEntity && entity2 instanceof PlayerEntity && !((PlayerEntity) entity2).shouldDamagePlayer((PlayerEntity) entity)) {
                hitResult = null;
            }
        }
        if (hitResult != null && !bl) {
            this.onCollision(hitResult);
            this.velocityDirty = true;
        }
        vec3d = this.getVelocity();
        double e = vec3d.x;
        double f = vec3d.y;
        double g = vec3d.z;
        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;
        double l = vec3d.horizontalLength();
        if (bl) {
            this.setYaw((float) (MathHelper.atan2(-e, -g) * 57.2957763671875));
        } else {
            this.setYaw((float) (MathHelper.atan2(e, g) * 57.2957763671875));
        }
        this.setPitch((float) (MathHelper.atan2(f, l) * 57.2957763671875));
        this.setPitch(PersistentProjectileEntity.updateRotation(this.prevPitch, this.getPitch()));
        this.setYaw(PersistentProjectileEntity.updateRotation(this.prevYaw, this.getYaw()));
        float m = getDragInAir();
        if (this.isTouchingWater()) {
            for (int o = 0; o < 4; ++o) {
                float p = 0.25f;
                this.world.addParticle(ParticleTypes.BUBBLE, h - e * p, j - f * p, k - g * p, e, f, g);
            }
            m = this.getDragInWater();
        }
        this.setVelocity(vec3d.multiply(m));
        /*if (!this.hasNoGravity() && !bl) {
            Vec3d vec3d4 = this.getVelocity();
            this.setVelocity(vec3d4.x, vec3d4.y - getGravity(), vec3d4.z);
        }*/

        showParticle(this.getPos(), new Vec3d(h, j, k), 0.5f);

        this.setPosition(h, j, k);
        this.checkBlockCollision();
    }

    public void showParticle(Vec3d now, Vec3d next, float interval) {
        Vec3d toVec = next.subtract(now);
        Vec3d toVecN = toVec.normalize();
        double length = toVec.length();
        var loop = 0;
        while (0 < length - loop * interval) {
            Vec3d point = now.add(toVecN.multiply(loop * interval));
            world.addParticle(
                    new DustParticleEffect(new Vec3f(0.9f, 0.9f, 0.9f), 0.25f),
                    point.getX(), point.getY(), point.getZ(),
                    0.0, 0.0, 0.0
            );
            loop++;
        }
    }

    public static DamageSource bullet(BulletEntity bullet, @Nullable Entity attacker) {
        return new ProjectileDamageSource("bullet", bullet, attacker).setProjectile();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        float damage = this.damage;
        DamageSource damageSource;
        Entity owner;
        super.onEntityHit(entityHitResult);
        Entity target = entityHitResult.getEntity();
        SoundData hitSound = SoundData.of(SoundEvents.ENTITY_GENERIC_HURT, 2.0f, 0.5f);
        if (target instanceof LivingEntity && isHeadShot(entityHitResult, (LivingEntity) target)) {
            damage *= 1.5f;
            hitSound = SoundData.of(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 2.0f, 0.5f);
        }
        if ((owner = this.getOwner()) == null) {
            damageSource = bullet(this, this);
        } else {
            damageSource = bullet(this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity) owner).onAttacking(target);
            }
        }
        boolean bl = target.getType() == EntityType.ENDERMAN;
        int j = target.getFireTicks();
        if (this.isOnFire() && !bl) {
            target.setOnFireFor(5);
        }
        target.timeUntilRegen = 0;
        float targetHealth = 0;
        if (target instanceof LivingEntity) {
            targetHealth = ((LivingEntity) target).getHealth();
        }
        if (target.damage(damageSource, damage)) {
            if (bl) {
                return;
            }

            boolean shouldPlayHitSound = true;

            if (target instanceof LivingEntity livingTarget && livingTarget.getHealth() <= 0) {
                if (targetHealth == 0) {
                    shouldPlayHitSound = false;
                } else {
                    if (owner instanceof LivingEntity livingOwner) {
                        this.killEffects.forEach(e -> e.event(livingOwner, this, livingTarget));
                    }
                    hitSound = SoundData.of(SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0f, 0.5f);
                }
            }

            if (shouldPlayHitSound && !this.world.isClient) {
                if (owner instanceof ServerPlayerEntity player) {
                    SoundData.playSoundIdToPlayer(player, player.getX(), player.getY(), player.getZ(), hitSound, SoundCategory.PLAYERS);
                    SoundData.playSoundId(player, (ServerWorld) world, target.getX(), target.getY(), target.getZ(),
                            hitSound, SoundCategory.PLAYERS);
                } else {
                    SoundData.playSoundId(null, (ServerWorld) world, target.getX(), target.getY(), target.getZ(),
                            hitSound, SoundCategory.PLAYERS);
                }
            }

            if (target instanceof LivingEntity livingTarget) {
                if (!this.world.isClient && owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingTarget, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingTarget);
                }
                this.onHit(livingTarget);
                if (owner != null && livingTarget != owner && livingTarget instanceof PlayerEntity && owner instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) owner).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
            }
            this.playSound(this.getHitSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
            //if (this.getPierceLevel() <= 0) {
            this.discard();
            //}
        } else {
            target.setFireTicks(j);
            this.setVelocity(this.getVelocity().multiply(-0.1));
            this.setYaw(this.getYaw() + 180.0f);
            this.prevYaw += 180.0f;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                this.discard();
            }
        }
    }

    private boolean isHeadShot(EntityHitResult e, LivingEntity target) {
        var eye = target.getEyePos();
        var size = Math.min(target.getHeight(), target.getWidth()) / 2f;
        var box = new Box(eye.x - size, eye.y - size, eye.z - size,
                eye.x + size, eye.y + size, eye.z + size);
        var pos = this.getPos();
        return box.raycast(pos, pos.add(getVelocity())).isPresent();
    }

    private void onHit(LivingEntity livingEntity) {
        var owner = this.getOwner();
        if (owner instanceof LivingEntity lOwner) {
            this.hitEffects.forEach(e -> e.event(lOwner, this, livingEntity));
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        Vec3d vec3d = blockHitResult.getPos().subtract(this.getX(), this.getY(), this.getZ());
        this.setVelocity(vec3d);
        Vec3d vec3d2 = vec3d.normalize().multiply(0.05f);
        this.setPos(this.getX() - vec3d2.x, this.getY() - vec3d2.y, this.getZ() - vec3d2.z);
        this.playSound(this.getHitSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.discard();
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_ARROW_HIT;
    }

    private EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return ProjectileUtil.getEntityCollision(this.world, this, currentPosition, nextPosition,
                this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), this::canHit);
    }

    private boolean isNoClip() {
        return false;
    }

    public float getGravity() {
        return 0.05f;
    }

    public float getDragInAir() {
        return 0.99f;
    }

    public float getDragInWater() {
        return 0.9f;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void tryDespawn() {
        if (decay < this.age) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.NONE;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.13f;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    public void addHitEffect(Effect hitEffect) {
        this.hitEffects.add(hitEffect);
    }

    public void addKillEffect(Effect killEffect) {
        this.killEffects.add(killEffect);
    }

    public interface Effect {
        void event(LivingEntity user, BulletEntity bullet, LivingEntity target);
    }
}
