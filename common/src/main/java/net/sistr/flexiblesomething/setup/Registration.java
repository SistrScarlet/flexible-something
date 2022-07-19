package net.sistr.flexiblesomething.setup;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.mob.BotEntity;
import net.sistr.flexiblesomething.entity.projectile.BulletEntity;
import net.sistr.flexiblesomething.item.gun.GunItem;

public class Registration {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(FlexibleSomethingMod.MOD_ID, Registry.ITEM_KEY);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(FlexibleSomethingMod.MOD_ID, Registry.ENTITY_TYPE_KEY);

    public static void init() {
        ITEMS.register();
        ENTITIES.register();
    }

    public static final RegistrySupplier<GunItem> TEST_GUN = ITEMS.register("test_gun", () ->
            new GunItem(new Item.Settings(),
                    new GunItem.BasicGunSettings(1.5f, 5.0f, 3.0f, 6.0f,
                            1),
                    new GunItem.ReloadSettings(30, 60, 0)));
    public static final RegistrySupplier<EntityType<BulletEntity>> BASIC_BULLET = ENTITIES.register("basic_bullet", () ->
            EntityType.Builder.<BulletEntity>create(BulletEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.5f, 0.5f)
                    .build("basic_bullet"));
    public static final RegistrySupplier<EntityType<BotEntity>> BOT_ENTITY = ENTITIES.register("bot", () ->
            EntityType.Builder.<BotEntity>create(BotEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 1.8f)
                    .build("bot"));
}
