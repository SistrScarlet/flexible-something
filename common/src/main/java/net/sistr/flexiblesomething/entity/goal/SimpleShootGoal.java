package net.sistr.flexiblesomething.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.sistr.flexiblesomething.item.Shootable;

import java.util.EnumSet;
import java.util.Optional;

public class SimpleShootGoal extends Goal {
    private MobEntity mob;
    private LivingEntity target;
    private ItemStack shootableStack;
    private Shootable shootable;

    public SimpleShootGoal(MobEntity mob) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = mob.getTarget();
        if (target == null || !isShootable(Hand.MAIN_HAND) || !canSee(target)) {
            return false;
        }
        this.target = target;
        this.shootable = getShootable(Hand.MAIN_HAND).get();
        this.shootableStack = mob.getStackInHand(Hand.MAIN_HAND);
        return true;
    }

    public boolean isShootable(Hand hand) {
        return getShootable(hand).isPresent();
    }

    public Optional<Shootable> getShootable(Hand hand) {
        ItemStack stack = mob.getStackInHand(hand);
        Item item = stack.getItem();
        if (item instanceof Shootable) {
            return Optional.of((Shootable) item);
        }
        return Optional.empty();
    }

    public boolean canSee(LivingEntity target) {
        return mob.getVisibilityCache().canSee(target);
    }

    @Override
    public void tick() {
        this.shootable.tryShoot(this.mob.world, this.shootableStack, this.mob);
        this.shootable.tickShootable(this.mob.world, this.shootableStack, this.mob);
        this.mob.getLookControl().lookAt(this.target, 30, 30);
        this.mob.lookAtEntity(this.target, 30, 30);
    }
}
