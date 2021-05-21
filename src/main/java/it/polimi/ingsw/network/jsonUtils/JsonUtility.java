package it.polimi.ingsw.network.jsonUtils;

import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class JsonUtility {

 //   private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final String configPathString = "src/main/resources/config/";
    public static String serializeVarArgs(Object... o) {
        return serialize(Arrays.stream(o).collect(Collectors.toList()));
    }

    public static <T> T deserialize(String jsonPath, Class<T> destinationClass){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return deserialize(jsonPath,destinationClass,gson);
    }

    public static <T> T deserialize(JsonElement jsonElement, Type destinationType) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(jsonElement, destinationType);
    }

    public static <T> T deserialize(String jsonPath, Type destinationType, Gson customGson){
        String jsonString = null;
        try {
            jsonString = Files.readString(Path.of(jsonPath), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customGson.fromJson(jsonString, destinationType);
    }

    public static <T> T deserialize(String jsonPath, Class<T> destinationClass, Gson customGson) {
        String jsonString = null;
        try {
            jsonString = Files.readString(Path.of(jsonPath), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deserializeFromString(jsonString, destinationClass, customGson);
    }

    public static <T> T deserializeFromString(String jsonString, Class<T> destinationClass, Gson customGson){
        return customGson.fromJson(jsonString, destinationClass);
    }

    public static <T> String serialize(T Object){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(Object);
    }

    public static <T> String serialize(T Object, Class<T> classToSerialize, Gson customGson){
        return customGson.toJson(Object, classToSerialize);
    }

    public static <T> void serialize(String jsonPath, T Object , Class<T> classToSerialize){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        serialize(jsonPath, Object, classToSerialize, gson);
    }

    public static <T> void serialize(String jsonPath, T Object , Class<T> classToSerialize, Gson customGson) {
        String jsonString = serialize(Object, classToSerialize, customGson);
        try {
            Writer writer = new FileWriter(jsonPath);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String toPrettyFormat(String jsonString)
    {
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
    }

    public static class PathConverter implements JsonDeserializer<Path>, JsonSerializer<Path> {
        @Override
        public Path deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return Paths.get(jsonElement.getAsString());
        }

        @Override
        public JsonElement serialize(Path path, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(path.toString());
        }
    }

}

