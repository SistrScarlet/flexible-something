package net.sistr.flexiblesomething.item;

import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Function;

public class FlexibleJsonReader<T> {
    public final Map<Identifier, Function<FlexibleArguments, T>> factoryMap = Maps.newHashMap();

    public static <T> FlexibleJsonReader<T> create() {
        return new FlexibleJsonReader<>();
    }

    public JsonElement write(Identifier id, FlexibleArguments arg) {
        var jsonObject = new JsonObject();
        jsonObject.add("id", new JsonPrimitive(id.toString()));
        jsonObject.add("data", new Gson().toJsonTree(arg));
        return jsonObject;
    }

    public T read(JsonElement element) throws IllegalStateException, IllegalArgumentException {
        var jsonObject = element.getAsJsonObject();
        var idElement = jsonObject.get("id");
        if (idElement == null || !idElement.isJsonPrimitive()) {
            throw new IllegalStateException("idが正しく書かれていません。");
        }
        String idStr = idElement.getAsString();
        Identifier id = new Identifier(idStr);
        if (!factoryMap.containsKey(id)) {
            throw new IllegalArgumentException(id + "は登録されてないアイテムです。");
        }
        var data = jsonObject.get("data");
        var arg = new Gson().fromJson(data, FlexibleArguments.class);

        var factory = factoryMap.get(id);
        return factory.apply(arg);
    }

    public void register(Identifier id, Function<FlexibleArguments, T> factory) {
        factoryMap.put(id, factory);
    }

    public static class InstanceFromJsonGetter<T> {
        private final String id;
        private final Class<T> clazz;

        public InstanceFromJsonGetter(String id, Class<T> clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        public T get(JsonObject root) throws IllegalArgumentException {
            var data = root.get(id);
            if (data == null) {
                throw new IllegalArgumentException();
            }
            var gson = new Gson();
            return gson.fromJson(data, clazz);
        }
    }

}
