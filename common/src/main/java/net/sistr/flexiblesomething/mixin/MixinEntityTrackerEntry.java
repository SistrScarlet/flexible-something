package net.sistr.flexiblesomething.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.sistr.flexiblesomething.entity.projectile.BulletEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EntityTrackerEntry.class)
public class MixinEntityTrackerEntry {

    @Mutable
    @Shadow
    @Final
    private Consumer<Packet<?>> receiver;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ServerWorld world, Entity entity,
                        int tickInterval, boolean alwaysUpdateVelocity,
                        Consumer<Packet<?>> receiver, CallbackInfo ci) {
        if (entity instanceof BulletEntity) {
            this.receiver = (p -> {
                if (!(p instanceof EntityVelocityUpdateS2CPacket)) {
                    return;
                }
                receiver.accept(p);
            });
        }
    }

}
