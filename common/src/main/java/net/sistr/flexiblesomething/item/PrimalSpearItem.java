package net.sistr.flexiblesomething.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PrimalSpearItem extends Item {
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public PrimalSpearItem() {
        super(new Settings().maxDamage(Integer.MAX_VALUE));
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID,
                "Weapon modifier", Integer.MAX_VALUE, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID,
                "Weapon modifier", Integer.MAX_VALUE, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            var nbt = stack.getOrCreateNbt();
            if (true) {
                var chargeNbt = nbt.get("charge");
                float toX = nbt.getFloat("toX");
                float toY = nbt.getFloat("toY");
                float toZ = nbt.getFloat("toZ");
                Vec3d vec = new Vec3d(toX, toY, toZ);
                player.setVelocity(vec);

            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        NbtCompound nbt = stack.getOrCreateNbt();
        if (user.isOnGround()) {//周囲の敵全員キル

        } else {//カッ飛んで道行く敵全員キル
            if (!nbt.contains("charge")) {
                nbt.put("charge", new NbtCompound());
            }
            var chargeNbt = nbt.getCompound("charge");
            if (0 < chargeNbt.getInt("timer")) {
                return TypedActionResult.pass(stack);
            }
            chargeNbt.putInt("timer", 10);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
}
