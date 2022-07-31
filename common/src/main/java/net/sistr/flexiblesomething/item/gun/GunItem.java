package net.sistr.flexiblesomething.item.gun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.entity.HasInput;
import net.sistr.flexiblesomething.entity.projectile.BulletEntity;
import net.sistr.flexiblesomething.item.FlexibleArguments;
import net.sistr.flexiblesomething.item.Shootable;
import net.sistr.flexiblesomething.util.SoundData;
import net.sistr.flexiblesomething.util.SoundHolder;

import javax.annotation.Nullable;
import java.util.List;

//機能を細かく分ける試みは、あまりにも複雑すぎて無理となった
public class GunItem extends Item implements Shootable {
    public final BasicGunSettings basicGunSettings;
    @Nullable
    public final ReloadSettings reloadSettings;
    private final SoundHolder soundHolder = SoundHolder.Builder.create()
            .addSound(0, SoundData.of(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1f, 1f))
            .addSound(2, SoundData.of(SoundEvents.BLOCK_WOODEN_DOOR_OPEN, 2f, 1f))
            .addSound(25, SoundData.of(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1f, 1f))
            .addSound(26, SoundData.of(SoundEvents.ENTITY_GENERIC_HURT, 0f, 1f))
            .addSound(27, SoundData.of(SoundEvents.BLOCK_WOODEN_DOOR_OPEN, 2f, 1f))
            .addSound(38, SoundData.of(SoundEvents.BLOCK_PISTON_CONTRACT, 2f, 1f))
            .addSound(40, SoundData.of(SoundEvents.BLOCK_PISTON_CONTRACT, 2f, 1f))
            .build();
    private final BulletEntity.Effect healEffect = (user, bullet, target) -> {
        if (!user.world.isClient) {
            user.heal(2);
            float size = 0.5f;
            ((ServerWorld) user.world).spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                    user.getX(), user.getY() + user.getHeight() / 2f, user.getZ(),
                    1, size, size, size, 0.0125f);
            SoundData.playSoundId(null, ((ServerWorld) user.world),
                    user.getX(), user.getY(), user.getZ(),
                    SoundData.of(SoundEvents.ENTITY_ARROW_HIT_PLAYER, 2.0f, 1.0f), SoundCategory.PLAYERS);
        }
    };
    private final BulletEntity.Effect boomEffect = (user, bullet, target) -> {
        if (!user.world.isClient && user.getRandom().nextFloat() < 0.5f) {
            target.damage(DamageSource.mob(user), 6f);
            float size = 0.5f;
            ((ServerWorld) user.world).spawnParticles(ParticleTypes.FLAME,
                    target.getX(), target.getY() + target.getHeight() / 2f, target.getZ(),
                    10, size, size, size, 0.125f);
            SoundData.playSoundId(null, ((ServerWorld) user.world),
                    user.getX(), user.getY(), user.getZ(),
                    SoundData.of(SoundEvents.ITEM_FIRECHARGE_USE, 2.0f, 1.0f),
                    SoundCategory.PLAYERS);
        }
    };
    private final List<BulletEntity.Effect> hitEffects = List.of(healEffect, boomEffect);

    public GunItem(Settings settings, BasicGunSettings basicGunSettings, @Nullable ReloadSettings reloadSettings) {
        super(settings);
        this.basicGunSettings = basicGunSettings;
        this.reloadSettings = reloadSettings;
    }

    public GunItem(FlexibleArguments arg) {
        this(new Item.Settings().maxCount(1),
                arg.orElseThrow(BasicGunSettings.class),
                arg.orNull(ReloadSettings.class));
    }

    //API

    @Override
    public void tryShoot(World world, ItemStack stack, LivingEntity user) {
        if (!world.isClient) {
            var gunState = getNBT(stack);
            setShootInput(gunState, 1);
        }
    }

    @Override
    public void tryReload(World world, ItemStack stack, LivingEntity user) {
        if (!world.isClient && reloadSettings != null) {
            var gunState = getNBT(stack);

            if (reloadSettings.ammoAmount <= getAmmoAmount(gunState)) {
                return;
            }

            int reloadTime = getReloadTime(gunState);
            if (reloadTime <= 0) {
                setReloadTime(reloadSettings, gunState, reloadSettings.length());
                playReloadSound(world, user, 0);
                //shootInputが残ったままだと、射撃が優先されてリロードがキャンセルされる
                setShootInput(gunState, 0);
            }
        }
    }

    //todo 両手、もしくはオフハンドに持った時の挙動が考慮外
    @Override
    public void tickShootable(World world, ItemStack stack, LivingEntity user) {
        if (world.isClient) {
            return;
        }

        HasInput hasInput = (HasInput) user;
        if (reloadSettings != null && hasInput.isInput("reload")) {
            tryReload(world, stack, user);
        }

        var gunState = getNBT(stack);

        Hand heldHand = Hand.MAIN_HAND;
        if (user.getMainHandStack() == stack) {
        } else if (user.getOffHandStack() == stack) {
            heldHand = Hand.OFF_HAND;
        } else {
            heldHand = null;
        }

        decreaseFireDelay(gunState);

        if (reloadSettings != null && isReloading(gunState)) {
            if (heldHand == null) {//todo ?
                setReloadTime(reloadSettings, gunState, 0);
            } else {
                int reloadTime = getReloadTime(gunState);
                if (reloadTime == 1) {
                    int reloadAmount = reloadSettings.reloadAmount;
                    if (reloadAmount <= 0) {
                        reloadAmount = reloadSettings.ammoAmount;
                    }
                    setAmmoAmount(reloadSettings, gunState, reloadAmount);
                }
                decreaseReloadTime(reloadSettings, gunState);
                playReloadSound(world, user, reloadSettings.length - reloadTime + 1);
            }
        }

        if (isInputShoot(gunState)) {
            decreaseShootInputTime(gunState);
            if (heldHand == null) {
                setShootInput(gunState, 0);
            } else {
                if (isReadyShoot(gunState)) {
                    setFireDelay(gunState, this.basicGunSettings.fireInterval);
                    if (reloadSettings != null) {
                        if (isReloading(gunState)) {
                            setReloadTime(reloadSettings, gunState, 0);
                        }
                        setAmmoAmount(reloadSettings, gunState, getAmmoAmount(gunState) - 1);
                    }
                    shoot(world, user);
                }
                if (reloadSettings != null && isNoAmmo(gunState) && !isReloading(gunState)) {
                    setReloadTime(reloadSettings, gunState, reloadSettings.length);
                    playReloadSound(world, user, 0);
                }
            }

        }
    }

    //use()は長押しだと4tick間隔でしか実行されない
    //そのため、一瞬クリックしただけでも5tickぶん長押ししたことにする
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = user.getStackInHand(hand);
            var gunState = getNBT(stack);
            setShootInput(gunState, 5);
        }
        return super.use(world, user, hand);
    }

    public NbtCompound getNBT(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        var gunState = nbt.getCompound("gunState");
        if (!nbt.contains("gunState")) {
            nbt.put("gunState", gunState);
        }
        return nbt;
    }

    //basic

    public int getShootInput(NbtCompound gunState) {
        return gunState.getInt("shootInput");
    }

    public void setShootInput(NbtCompound gunState, int shootTime) {
        gunState.putInt("shootInput", shootTime);
    }

    public boolean isInputShoot(NbtCompound gunState) {
        return 0 < getShootInput(gunState);
    }

    private void decreaseShootInputTime(NbtCompound gunState) {
        int inputShootTime = getShootInput(gunState);
        if (0 < inputShootTime) {
            setShootInput(gunState, inputShootTime - 1);
        }
    }

    public float getFireDelay(NbtCompound gunState) {
        return gunState.getFloat("fireDelay");
    }

    public void setFireDelay(NbtCompound gunState, float fireDelay) {
        gunState.putFloat("fireDelay", fireDelay);
    }

    public void decreaseFireDelay(NbtCompound gunState) {
        float fireDelay = getFireDelay(gunState);
        if (0 < fireDelay) {
            setFireDelay(gunState, fireDelay - 1);
        }
    }

    public boolean isReadyShoot(NbtCompound gunState) {
        return getFireDelay(gunState) <= 0
                && (reloadSettings == null || !isNoAmmo(gunState));
    }

    //reload

    public int getAmmoAmount(NbtCompound gunState) {
        return gunState.getInt("ammoAmount");
    }

    public void setAmmoAmount(ReloadSettings reload, NbtCompound gunState, int amount) {
        if (amount < 0) {
            amount = 0;
        } else if (reload.ammoAmount < amount) {
            amount = reload.ammoAmount;
        }
        gunState.putInt("ammoAmount", amount);
    }

    public boolean isNoAmmo(NbtCompound gunState) {
        return getAmmoAmount(gunState) <= 0;
    }

    public int getReloadTime(NbtCompound gunState) {
        return gunState.getInt("reloadTime");
    }

    public void setReloadTime(ReloadSettings reload, NbtCompound gunState, int reloadTime) {
        if (reload.length < reloadTime) {
            reloadTime = reload.length;
        } else if (reloadTime < 0) {
            reloadTime = 0;
        }
        gunState.putInt("reloadTime", reloadTime);
    }

    public void decreaseReloadTime(ReloadSettings reload, NbtCompound gunState) {
        int reloadTime = getReloadTime(gunState);
        if (0 < reloadTime) {
            setReloadTime(reload, gunState, reloadTime - 1);
        }
    }

    public boolean isReloading(NbtCompound gunState) {
        return 0 < gunState.getInt("reloadTime");
    }

    //logic

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity) {
            tickShootable(world, stack, (LivingEntity) entity);
        }
    }

    public void shoot(World world, LivingEntity shooter) {
        var bullet = createBullet(world, shooter);
        bullet.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0f,
                basicGunSettings.velocity, basicGunSettings.inAccuracy);
        world.spawnEntity(bullet);
        playGunSound(world, shooter);
    }

    protected BulletEntity createBullet(World world, LivingEntity shooter) {
        var bullet = new BulletEntity(shooter, world);
        bullet.setDamage(this.basicGunSettings.damage);
        this.hitEffects.forEach(bullet::addHitEffect);
        return bullet;
    }

    public void playGunSound(World world, LivingEntity shooter) {
        world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 2f, 2f);
    }

    public void playReloadSound(World world, LivingEntity shooter, int time) {
        var sounds = soundHolder.getSound(time);
        sounds.forEach(sound ->
                SoundData.playSoundId(null, (ServerWorld) world, shooter.getX(), shooter.getY(), shooter.getZ(),
                        sound, SoundCategory.PLAYERS));
    }

    public record BasicGunSettings(float fireInterval, float inAccuracy, float velocity, float damage,
                                   int shotsAmount) {
    }

    public record ReloadSettings(int ammoAmount, int length, int reloadAmount) {
    }

}
