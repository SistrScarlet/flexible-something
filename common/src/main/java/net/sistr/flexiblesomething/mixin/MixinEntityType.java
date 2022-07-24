package net.sistr.flexiblesomething.mixin;

import net.minecraft.entity.EntityType;
import net.sistr.flexiblesomething.setup.Registration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public class MixinEntityType {

    @Inject(method = "alwaysUpdateVelocity", at = @At("HEAD"), cancellable = true)
    private void onAlways(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this == Registration.BASIC_BULLET.get()) {
            cir.setReturnValue(false);
        }
    }
}
