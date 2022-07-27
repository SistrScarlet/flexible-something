package net.sistr.flexiblesomething.util;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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
}
