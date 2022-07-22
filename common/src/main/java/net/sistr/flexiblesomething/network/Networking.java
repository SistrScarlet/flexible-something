package net.sistr.flexiblesomething.network;

import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

public class Networking {

    public static void init() {
        comInit();
        if (Platform.getEnv() == EnvType.CLIENT) {
            clInit();
        }
    }

    public static void comInit() {

    }

    public static void clInit() {

    }

}
