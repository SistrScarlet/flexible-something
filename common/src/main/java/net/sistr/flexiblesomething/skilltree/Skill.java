package net.sistr.flexiblesomething.skilltree;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Skill {
    private final ImmutableList<Skill> children;
    private final String name;
    private final Predicate<PlayerEntity> unlockPredicate;
    private final Consumer<PlayerEntity> unlockConsumer;

    public Skill(String name, List<Skill> children, Predicate<PlayerEntity> unlockPredicate, Consumer<PlayerEntity> unlockConsumer) {
        this.name = name;
        this.children = ImmutableList.copyOf(children);
        this.unlockPredicate = unlockPredicate;
        this.unlockConsumer = unlockConsumer;
    }

    public int size() {
        return children.size();
    }

    public Skill getChild(int index) {
        return children.get(index);
    }

    public ImmutableList<Skill> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public boolean canUnlock(PlayerEntity player) {
        return unlockPredicate.test(player);
    }

    public void unlock(PlayerEntity player) {
        unlockConsumer.accept(player);
    }

    public static class Builder {
        private final List<Builder> children = Lists.newArrayList();
        private final String name;
        private Predicate<PlayerEntity> unlockPredicate = null;
        private Consumer<PlayerEntity> unlockConsumer = null;

        public Builder(String name) {
            this.name = name;
        }

        public Builder addEntry(Builder entry) {
            children.add(entry);
            return this;
        }

        public Builder setUnlockPredicate(Predicate<PlayerEntity> unlockPredicate) {
            this.unlockPredicate = unlockPredicate;
            return this;
        }

        public Builder setUnlockConsumer(Consumer<PlayerEntity> unlockConsumer) {
            this.unlockConsumer = unlockConsumer;
            return this;
        }

        public Skill build() {
            if (unlockPredicate == null) {
                unlockPredicate = p -> true;
            }
            if (unlockConsumer == null) {
                unlockConsumer = p -> {
                };
            }
            return new Skill(name, children.stream()
                    .map(Builder::build)
                    .toList(), unlockPredicate, unlockConsumer);
        }

    }
}
