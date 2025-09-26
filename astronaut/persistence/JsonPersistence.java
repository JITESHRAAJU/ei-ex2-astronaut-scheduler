package com.jitesh.astronaut.persistence;

import com.google.gson.*;
import com.jitesh.astronaut.model.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.List;

public class JsonPersistence {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new JsonSerializer<LocalTime>() {
                @Override
                public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                @Override
                public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    return LocalTime.parse(json.getAsString());
                }
            })
            .setPrettyPrinting()
            .create();

    public static void save(List<Task> tasks, File file) throws IOException {
        try (Writer w = new FileWriter(file)) {
            GSON.toJson(tasks, w);
        }
    }

    public static Task[] load(File file) throws IOException {
        try (Reader r = new FileReader(file)) {
            return GSON.fromJson(r, Task[].class);
        }
    }
}