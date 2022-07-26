package net.sistr.flexiblesomething.client.key;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.HasInput;
import net.sistr.flexiblesomething.network.InputPacket;
import org.lwjgl.glfw.GLFW;

//todo データ駆動化
public class FlexibleKeys {
    public static final KeyBinding RELOAD = new KeyBinding(FlexibleSomethingMod.MOD_ID + "reload", GLFW.GLFW_KEY_R,
            "key.categories.flexiblesomething");

    public static void init() {
        KeyMappingRegistry.register(RELOAD);
        ClientTickEvent.CLIENT_PRE.register(c -> {
            var player = c.player;
            if (player != null) {
                HasInput hasInput = (HasInput)player;
                boolean input = RELOAD.isPressed();
                if (input != hasInput.isInput("reload")) {
                    hasInput.setInput("reload", input);
                    InputPacket.sendC2S("reload", input);
                }
            }
        });
    }

}
