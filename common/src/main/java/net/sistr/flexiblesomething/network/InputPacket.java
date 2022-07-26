package net.sistr.flexiblesomething.network;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.sistr.flexiblesomething.FlexibleSomethingMod;
import net.sistr.flexiblesomething.entity.HasInput;

public class InputPacket {
    public static final Identifier ID = new Identifier(FlexibleSomethingMod.MOD_ID, "input");

    public static void sendC2S(String id, boolean input) {
        var buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(id);
        buf.writeBoolean(input);
        NetworkManager.sendToServer(ID, buf);
    }

    public static void receiveC2S(PacketByteBuf buf, NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        String id = buf.readString();
        boolean input = buf.readBoolean();
        context.queue(() -> apply(player, id, input));
    }

    public static void apply(PlayerEntity player, String id, boolean input) {
        var hasInput = (HasInput) player;
        hasInput.setInput(id, input);
    }
}
