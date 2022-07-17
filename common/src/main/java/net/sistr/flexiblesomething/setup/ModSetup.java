package net.sistr.flexiblesomething.setup;

import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;
import dev.architectury.platform.Platform;
import net.minecraft.util.Identifier;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
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
    }

}
