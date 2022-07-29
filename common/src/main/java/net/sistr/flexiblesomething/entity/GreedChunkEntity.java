package net.sistr.flexiblesomething.entity;

import com.google.common.collect.ImmutableList;
import dev.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.setup.Registration;
import net.sistr.flexiblesomething.util.Raidable;

import java.util.List;

//零れ落ち、溜まる
//変形し、スポーンする
public class GreedChunkEntity extends Entity {
    private static final List<EntityType<?>> GREED_MONSTERS =
            ImmutableList.of(
                    Registration.G_ENDERMAN.get(),
                    Registration.G_PHANTOM.get(),
                    Registration.G_SKELETON.get(),
                    Registration.G_SPIDER.get(),
                    Registration.G_ZOMBIE.get()
            );
    private final BlockState state = Registration.GREED_CHUNK_BLOCK.get().getDefaultState();
    private float spread;
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

        this.setVelocity(this.getVelocity().multiply(0.98));

        this.move(MovementType.SELF, this.getVelocity());

        if (!this.hasNoGravity()) {
            var vec = getVelocity();
            this.setVelocity(vec.x, vec.y - 0.2, vec.z + 0);
        }

        prevSpread = spread;
        if (this.onGround) {
            this.spread = Math.min(this.spread + 0.2f, 0.95f);
            if (!this.world.isClient && this.spread == 0.95f && 200 < this.age) {
                this.discard();
                var greedMonster = GREED_MONSTERS
                        .get(this.random.nextInt(GREED_MONSTERS.size()))
                        .create(this.world);
                if (greedMonster != null) {
                    greedMonster.setPosition(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(greedMonster);
                    var raidable = (Raidable) this.targetPlayer;
                    raidable.addRaidEntity(greedMonster);
                }
            }
        } else {
            this.spread = Math.min(this.spread - 0.05f, -0.95f);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        if (this.targetPlayer != null) {
            Raidable raidable = (Raidable) this.targetPlayer;
            raidable.removeRaidEntity(this);
        }
        super.remove(reason);
    }

    @Override
    protected Box calculateBoundingBox() {
        float spread = this.getSpread(1);
        return new Box(
                -0.25f - spread * 0.25f,
                0,
                -0.25f - spread * 0.25f,
                0.25f + spread * 0.25f,
                0.5f - spread * 0.5f,
                0.25f + spread * 0.25f
        ).offset(this.getPos());
    }

    public BlockState getBlockState() {
        return this.state;
    }

    public float getSpread(float delta) {
        return MathHelper.lerp(delta, prevSpread, spread);
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
