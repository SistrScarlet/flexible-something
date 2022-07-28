package net.sistr.flexiblesomething.entity;

import dev.architectury.networking.NetworkManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.sistr.flexiblesomething.setup.Registration;

//零れ落ち、溜まる
//変形し、スポーンする
public class GreedChunkEntity extends Entity {
    private final BlockState state = Registration.GREED_CHUNK_BLOCK.get().getDefaultState();
    private float spread;
    private float prevSpread;

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
    public void tick() {
        super.tick();

        this.setVelocity(this.getVelocity().multiply(0.98));

        this.move(MovementType.SELF, this.getVelocity());

        if (!this.hasNoGravity()) {
            var vec = getVelocity();
            this.setVelocity(vec.x, vec.y - 0.02, vec.z + 0);
        }

        prevSpread = spread;
        if (this.onGround) {
            this.spread = Math.min(this.spread + 0.2f, 0.95f);
        } else {
            this.spread = Math.min(this.spread - 0.05f, -0.95f);
        }
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
}
