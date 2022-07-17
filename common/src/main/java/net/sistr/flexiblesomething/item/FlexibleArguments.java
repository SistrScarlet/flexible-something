package net.sistr.flexiblesomething.item;

import org.apache.commons.compress.utils.Lists;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class FlexibleArguments {
    private final Object[] objects;

    public FlexibleArguments(Collection<Object> objects) {
        this.objects = objects.toArray();
    }

    public <T> T orElseThrow(Class<T> clazz) throws IllegalArgumentException {
        var optional = optional(clazz);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException(clazz.getTypeName() + "は存在しません。");
        }
        return optional.get();
    }

    public <T> T orNull(Class<T> clazz) throws IllegalArgumentException {
        var optional = optional(clazz);
        return optional.orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> optional(Class<T> clazz) throws IllegalArgumentException {
        var optional = Arrays.stream(objects)
                .filter(o -> o.getClass().equals(clazz))
                .findAny();
        return (Optional<T>) optional;
    }

    public static class Builder {
        private final List<Object> objects = Lists.newArrayList();

        public Builder add(Object o) {
            this.objects.add(o);
            return this;
        }

        public FlexibleArguments build() {
            return new FlexibleArguments(this.objects);
        }
    }

}
