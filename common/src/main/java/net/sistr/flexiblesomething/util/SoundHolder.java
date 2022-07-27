package net.sistr.flexiblesomething.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;

public class SoundHolder {
    private final int length;
    private final Int2ObjectOpenHashMap<List<SoundData>> sounds;

    public SoundHolder(Map<Integer, List<SoundData>> sounds) {
        this.sounds = new Int2ObjectOpenHashMap<>();
        sounds.entrySet().stream()
                .peek(e -> e.setValue(ImmutableList.copyOf(e.getValue())))
                .forEach(e -> this.sounds.put(e.getKey(), e.getValue()));
        this.length = this.sounds.keySet().intStream().max().orElse(0);
    }

    public List<SoundData> getSound(int time) {
        var soundData = sounds.get(time);
        return soundData != null ? soundData : ImmutableList.of();
    }

    public int getLength() {
        return length;
    }

    public static class Builder {
        private final Map<Integer, List<SoundData>> sounds = Maps.newHashMap();

        protected Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder addSound(int time, SoundData soundData) {
            var soundList = sounds.computeIfAbsent(time, t -> Lists.newArrayList());
            soundList.add(soundData);
            return this;
        }

        public SoundHolder build() {
            return new SoundHolder(sounds);
        }
    }
}
