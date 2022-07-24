package net.sistr.flexiblesomething.setup;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.mob.*;
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
                    new GunItem.BasicGunSettings(1.5f, 5.0f, 3.0f, 6.0f, 1),
                    new GunItem.ReloadSettings(30, 60, 0)));
    public static final RegistrySupplier<GunItem> GUN_PUNISHER = ITEMS.register("punisher", () ->
            new GunItem(new Item.Settings(),
                    new GunItem.BasicGunSettings(1.0f, 5.0f, 3.0f, 4.0f, 1),
                    new GunItem.ReloadSettings(50, 50, 0)));

    public static final RegistrySupplier<EntityType<BulletEntity>> BASIC_BULLET = ENTITIES.register("basic_bullet", () ->
            EntityType.Builder.<BulletEntity>create(BulletEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.5f, 0.5f)
                    .maxTrackingRange(4)
                    .trackingTickInterval(1)
                    .build("basic_bullet"));

    public static final RegistrySupplier<EntityType<BotEntity>> BOT_ENTITY = ENTITIES.register("bot", () ->
            EntityType.Builder.<BotEntity>create(BotEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 1.8f)
                    .build("bot"));

    public static final RegistrySupplier<EntityType<GreedDrownedEntity>> G_DROWNED = ENTITIES.register("greed_drowned", () ->
            EntityType.Builder.<GreedDrownedEntity>create(GreedDrownedEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 1.95f).maxTrackingRange(8)
                    .build("greed_drowned"));
    public static final RegistrySupplier<EntityType<GreedEndermanEntity>> G_ENDERMAN = ENTITIES.register("greed_enderman", () ->
            EntityType.Builder.<GreedEndermanEntity>create(GreedEndermanEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 2.9f).maxTrackingRange(8)
                    .build("greed_enderman"));
    public static final RegistrySupplier<EntityType<GreedPhantomEntity>> G_PHANTOM = ENTITIES.register("greed_phantom", () ->
            EntityType.Builder.<GreedPhantomEntity>create(GreedPhantomEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.9f, 0.5f).maxTrackingRange(8)
                    .build("greed_phantom"));
    public static final RegistrySupplier<EntityType<GreedSkeletonEntity>> G_SKELETON = ENTITIES.register("greed_skeleton", () ->
            EntityType.Builder.<GreedSkeletonEntity>create(GreedSkeletonEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 1.99f).maxTrackingRange(8)
                    .build("greed_skeleton"));
    public static final RegistrySupplier<EntityType<GreedSpiderEntity>> G_SPIDER = ENTITIES.register("greed_spider", () ->
            EntityType.Builder.<GreedSpiderEntity>create(GreedSpiderEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(1.4f, 0.9f).maxTrackingRange(8)
                    .build("greed_spider"));
    public static final RegistrySupplier<EntityType<GreedWitchEntity>> G_WITCH = ENTITIES.register("greed_witch", () ->
            EntityType.Builder.<GreedWitchEntity>create(GreedWitchEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 1.95f).maxTrackingRange(8)
                    .build("greed_witch"));
    public static final RegistrySupplier<EntityType<GreedZombieEntity>> G_ZOMBIE = ENTITIES.register("greed_zombie", () ->
            EntityType.Builder.<GreedZombieEntity>create(GreedZombieEntity::new, SpawnGroup.MONSTER)
                    .setDimensions(0.6f, 1.95f).maxTrackingRange(8)
                    .build("greed_zombie"));
}
