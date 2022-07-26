package net.sistr.flexiblesomething.network;

import dev.architectury.networking.NetworkManager;
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
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, InputPacket.ID, InputPacket::receiveC2S);
    }

    public static void clInit() {

    }

}
