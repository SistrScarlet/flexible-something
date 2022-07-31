package net.sistr.flexiblesomething.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundIdS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;

public class SoundData {
    private final Identifier id;
    private final FloatSupplier pitch;
    private final FloatSupplier volume;

    private SoundData(Identifier id, FloatSupplier pitch, FloatSupplier volume) {
        this.id = id;
        this.pitch = pitch;
        this.volume = volume;
    }

    public static SoundData of(Identifier id, FloatSupplier pitch, FloatSupplier volume) {
        return new SoundData(id, pitch, volume);
    }

    public static SoundData of(Identifier id, float pitch, float volume) {
        return new SoundData(id, () -> pitch, () -> volume);
    }

    public static SoundData of(SoundEvent soundEvent, float pitch, float volume) {
        return new SoundData(soundEvent.getId(), () -> pitch, () -> volume);
    }

    public Identifier getId() {
        return id;
    }

    public float getPitch() {
        return pitch.get();
    }

    public float getVolume() {
        return volume.get();
    }

    public static void playSoundId(@Nullable PlayerEntity player, ServerWorld world, double x, double y, double z,
                                   SoundData soundData, SoundCategory soundCategory) {
        float volume = soundData.getVolume();
        float pitch = soundData.getPitch();
        world.getServer().getPlayerManager()
                .sendToAround(player, x, y, z,
                        volume > 1.0f ? 16.0f * volume : 16.0,
                        world.getRegistryKey(),
                        new PlaySoundIdS2CPacket(soundData.getId(), soundCategory, new Vec3d(x, y, z), volume, pitch));
    }

    public static void playSoundIdToPlayer(ServerPlayerEntity player, double x, double y, double z,
                                           SoundData soundData, SoundCategory soundCategory) {
        float volume = soundData.getVolume();
        float pitch = soundData.getPitch();
        double distLimit = Math.pow(volume > 1.0f ? (double) (volume * 16.0f) : 16.0, 2.0);
        double e = x - player.getX();
        double f = y - player.getY();
        double g = z - player.getZ();
        double dist = e * e + f * f + g * g;
        if (distLimit < dist) {
            return;
        }
        player.networkHandler.sendPacket(new PlaySoundIdS2CPacket(soundData.getId(), soundCategory, new Vec3d(x, y, z), volume, pitch));
    }
}
