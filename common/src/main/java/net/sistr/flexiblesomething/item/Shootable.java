package net.sistr.flexiblesomething.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface Shootable {

    void tryShoot(World world, ItemStack stack, LivingEntity user);

    void tryReload(World world, ItemStack stack, LivingEntity user);

    void tickShootable(World world, ItemStack stack, LivingEntity user);
}
