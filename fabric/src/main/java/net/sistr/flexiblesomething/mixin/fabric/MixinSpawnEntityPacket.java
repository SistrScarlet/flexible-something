package net.sistr.flexiblesomething.mixin.fabric;

import dev.architectury.extensions.network.EntitySpawnExtension;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.fabric.SpawnEntityPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEntityPacket.class)
public class MixinSpawnEntityPacket {
    private static final Identifier PACKET_ID = new Identifier("architectury", "spawn_entity_packet");

    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private static void onCreate(Entity entity, CallbackInfoReturnable<Packet<?>> cir) {
        if (entity.world.isClient()) {
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        }
        var buffer = PacketByteBufs.create();
        buffer.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
        buffer.writeUuid(entity.getUuid());
        buffer.writeVarInt(entity.getId());
        var position = entity.getPos();
        buffer.writeDouble(position.x);
        buffer.writeDouble(position.y);
        buffer.writeDouble(position.z);
        buffer.writeFloat(entity.getYaw());//todo 直ったら消す
        buffer.writeFloat(entity.getPitch());
        buffer.writeFloat(entity.getHeadYaw());
        var deltaMovement = entity.getVelocity();
        buffer.writeDouble(deltaMovement.x);
        buffer.writeDouble(deltaMovement.y);
        buffer.writeDouble(deltaMovement.z);
        if (entity instanceof EntitySpawnExtension ext) {
            ext.saveAdditionalSpawnData(buffer);
        }
        cir.setReturnValue(NetworkManager.toPacket(NetworkManager.s2c(), PACKET_ID, buffer));
    }
}
