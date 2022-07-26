package net.sistr.flexiblesomething.mixin;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.sistr.flexiblesomething.entity.HasInput;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements HasInput {
    protected final Object2BooleanOpenHashMap<String> inputMap = new Object2BooleanOpenHashMap<>();


    @Override
    public boolean isInput(String id) {
        return inputMap.getBoolean(id);
    }

    @Override
    public void setInput(String id, boolean isInput) {
        this.inputMap.put(id, isInput);
    }
}
