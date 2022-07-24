package net.sistr.flexiblesomething.setup;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;
import dev.architectury.platform.Platform;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.mob.*;
import net.sistr.flexiblesomething.item.FlexibleArguments;
import net.sistr.flexiblesomething.item.SimpleItem;
import net.sistr.flexiblesomething.item.gun.GunItem;

import java.nio.file.Files;
import java.nio.file.Path;

public class ModSetup {

    public static void init() {
        var reader = FlexibleSomethingMod.ITEM_READER;
        reader.register(new Identifier(FlexibleSomethingMod.MOD_ID, "simple"), SimpleItem::new);
        reader.register(new Identifier(FlexibleSomethingMod.MOD_ID, "gun"), GunItem::new);

        var json = reader.write(new Identifier(FlexibleSomethingMod.MOD_ID, "gun"),
                new FlexibleArguments.Builder()
                        .add(Registration.TEST_GUN.get().basicGunSettings)
                        .add(Registration.TEST_GUN.get().reloadSettings)
                        .build());
        Path path = Platform.getConfigFolder().resolve(FlexibleSomethingMod.MOD_ID);
        try {
            if (Files.notExists(path)) {
                Files.createDirectory(path);
            }
        } catch (Exception ignore) {

        }
        try (var bw = Files.newBufferedWriter(path.resolve("test.json"))) {
            var writer = new GsonBuilder().setPrettyPrinting().create().newJsonWriter(bw);
            Streams.write(json, writer);
        } catch (Exception ignore) {

        }

        EntityAttributeRegistry.register(Registration.BOT_ENTITY, BotEntity::createBotAttribute);

        EntityAttributeRegistry.register(Registration.G_DROWNED, GreedDrownedEntity::createZombieAttributes);
        EntityAttributeRegistry.register(Registration.G_ENDERMAN, GreedEndermanEntity::createEndermanAttributes);
        EntityAttributeRegistry.register(Registration.G_PHANTOM, HostileEntity::createHostileAttributes);
        EntityAttributeRegistry.register(Registration.G_SKELETON, GreedSkeletonEntity::createAbstractSkeletonAttributes);
        EntityAttributeRegistry.register(Registration.G_SPIDER, GreedSpiderEntity::createSpiderAttributes);
        EntityAttributeRegistry.register(Registration.G_WITCH, GreedWitchEntity::createWitchAttributes);
        EntityAttributeRegistry.register(Registration.G_ZOMBIE, GreedZombieEntity::createZombieAttributes);

        /*BiomeModifications.addProperties(ctx -> {
            var prop = ctx.getProperties();
            var category = prop.getCategory();
            if (category == Biome.Category.THEEND || category == Biome.Category.NETHER || category == Biome.Category.NONE) {
                return false;
            }
            var cost = prop.getSpawnProperties().getMobSpawnCosts();
            if (cost != null && cost.get(EntityType.ZOMBIE) != null) {
                return true;
            }
            *//*var spawners = prop.getSpawnProperties().getSpawners();
            if (spawners == null) {
                return false;
            }
            var map = spawners.get(SpawnGroup.MONSTER);
            if (map.stream().map(entry -> entry.type).anyMatch(type -> type == EntityType.ZOMBIE)) {
                return true;
            }*//*
            return false;
        }, (ctx, mut) -> {
            var spawn = mut.getSpawnProperties();
            spawn.addSpawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(Registration.BOT_ENTITY.get(), 3, 1, 4));
        });*/
    }

}
