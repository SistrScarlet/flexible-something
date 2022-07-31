package net.sistr.flexiblesomething.entity;

import com.google.common.collect.ImmutableList;
import dev.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.entity.mob.GreedEntity;
import net.sistr.flexiblesomething.setup.Registration;
import net.sistr.flexiblesomething.util.Raidable;

import java.util.List;

//零れ落ち、溜まる
//変形し、スポーンする
public class GreedChunkEntity extends Entity {
    private static final List<EntityType<? extends GreedEntity>> GREED_MONSTERS =
            ImmutableList.of(
                    Registration.G_PHANTOM.get(),
                    Registration.G_SPIDER.get(),
                    Registration.G_ZOMBIE.get()
            );
    private final BlockState state = Registration.GREED_CHUNK_BLOCK.get().getDefaultState();
    private static final TrackedData<Float> SPREAD = DataTracker.registerData(GreedChunkEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private float prevSpread;
    private PlayerEntity targetPlayer;

    public GreedChunkEntity(EntityType<? extends GreedChunkEntity> type, World world) {
        super(type, world);
    }

    public GreedChunkEntity(World world) {
        super(Registration.GREED_CHUNK_ENTITY.get(), world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SPREAD, 0f);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 10.0;
        if (Double.isNaN(d)) {
            d = 1.0;
        }
        return distance < (d *= 64.0 * PersistentProjectileEntity.getRenderDistanceMultiplier()) * d;
    }

    @Override
    public void tick() {
        if (!this.world.isClient && this.targetPlayer == null) {
            discard();
        }
        super.tick();

        float drug = 0.98f;
        if (this.onGround) {
            drug = 0.5f;
        }
        this.setVelocity(this.getVelocity().multiply(drug));

        this.move(MovementType.SELF, this.getVelocity());

        if (!this.hasNoGravity()) {
            var vec = getVelocity();
            this.setVelocity(vec.x, vec.y - 0.05, vec.z + 0);
        }

        if (!this.world.isClient) {
            this.world.getEntitiesByClass(GreedChunkEntity.class, calculateBoundingBox(), p -> true)
                    .forEach(this::pushAwayFrom);
        }

        float spread = getSpread();
        this.prevSpread = spread;
        if (!this.world.isClient) {
            spread *= 0.9f;
            if (this.onGround) {
                spread = Math.min(spread + 0.2f, 0.95f);
                if (spread == 0.95f && 200 < this.age) {
                    this.discard();
                    var greedMonster = GREED_MONSTERS
                            .get(this.random.nextInt(GREED_MONSTERS.size()))
                            .create(this.world);
                    if (greedMonster != null) {
                        greedMonster.setPosition(this.getX(), this.getY(), this.getZ());
                        greedMonster.setGreedTarget(this.targetPlayer);
                        this.world.spawnEntity(greedMonster);
                        var raidable = (Raidable) this.targetPlayer;
                        raidable.addRaidEntity(greedMonster);
                        this.world.playSound(null, this.getX(), this.getY(), this.getZ(),
                                SoundEvents.ENTITY_ZOMBIE_CONVERTED_TO_DROWNED, SoundCategory.HOSTILE,
                                1f, 0f);
                    }
                }
            } else {
                spread = Math.max(spread * 0.9f - 0.2f, -0.28f);
            }
            if (this.prevSpread != spread) {
                setSpread(spread);
            }
        }
    }

    @Override
    protected Box calculateBoundingBox() {
        float width = 0.5f + getSpread() * 0.5f;
        float height = 0.125f / (width * width);
        return new Box(-width / 2, 0, -width / 2,
                width / 2, height, width / 2)
                .offset(this.getPos());
    }

    public BlockState getBlockState() {
        return this.state;
    }

    public float getSpread(float delta) {
        return MathHelper.lerp(delta, prevSpread, getSpread());
    }

    public float getSpread() {
        return this.dataTracker.get(SPREAD);
    }

    public void setSpread(float spread) {
        this.dataTracker.set(SPREAD, spread);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return NetworkManager.createAddEntityPacket(this);
    }

    public PlayerEntity getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(PlayerEntity targetPlayer) {
        this.targetPlayer = targetPlayer;
    }
}
