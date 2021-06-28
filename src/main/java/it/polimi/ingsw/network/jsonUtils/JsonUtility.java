package it.polimi.ingsw.network.jsonUtils;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;

public class JsonUtility {

    public static final String readConfigPathString = "/config/";
    public static final String writeConfigPathString = "src/main/resources/config/";
    public static final Gson customGson = customGson();
    public static final GsonBuilder customGsonBuilder = customGsonBuilder();

   private static Gson customGson(){
       return customGsonBuilder().create();
   }

   private static GsonBuilder customGsonBuilder(){

       return new GsonBuilder()
               .enableComplexMapKeySerialization()
               .registerTypeHierarchyAdapter(Path.class, new PathConverter())
               .setDateFormat(DateFormat.FULL, DateFormat.FULL)
               .setPrettyPrinting();
   }


    public static <T> T deserialize(String jsonPath, Class<T> destinationClass, boolean isPathFromSourceRoot){
        if(isPathFromSourceRoot)
            return deserializeFromSourceRoot(jsonPath,destinationClass, customGson);

        else
            return deserializeFromAbsolutePath(jsonPath, destinationClass, customGson);

    }

    public static <T> T deserialize(JsonElement jsonElement, Type destinationType) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        return gson.fromJson(jsonElement, destinationType);
    }

    public static <T> T deserializeFromSourceRoot(String jsonPath, Type destinationType, Gson customGson){

        InputStreamReader reader = new InputStreamReader(JsonUtility.class.getResourceAsStream(jsonPath));

        String jsonString = null;
        try {
            jsonString = IOUtils.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customGson.fromJson(jsonString, destinationType);
    }

    public static <T> T deserializeFromSourceRoot(String jsonPath, Class<T> destinationClass, Gson customGson) {

        InputStreamReader reader = new InputStreamReader(JsonUtility.class.getResourceAsStream(jsonPath));

        String jsonString = null;
        try {
            jsonString = IOUtils.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deserializeFromString(jsonString, destinationClass, customGson);
    }

    public static <T> T deserializeFromAbsolutePath(String name, Class<T> destinationClass, Gson customGson) {

        String jsonString = null;
        try {
            jsonString = Files.readString(Path.of(name), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return deserializeFromString(jsonString, destinationClass, customGson);
    }

    public static <T> T deserializeFromString(String jsonString, Class<T> destinationClass, Gson customGson){
        return customGson.fromJson(jsonString, destinationClass);
    }

    public static <T> String serialize(T Object){
        return customGson.toJson(Object);
    }

    public static <T> String serialize(T Object, Class<T> classToSerialize, Gson customGson){
        return customGson.toJson(Object, classToSerialize);
    }

    public static <T> String serialize(T Object, Type destinationType, Gson customGson){
        return customGson.toJson(Object, destinationType);
    }

    public static <T> void serialize(String jsonPath, T Object , Class<T> classToSerialize){
        serialize(jsonPath, Object, classToSerialize, customGson);
    }

    public static <T> void serialize(String jsonPath, T Object , Type destinationType, Gson customGson) {

        String jsonString = serialize(Object, destinationType, customGson);

        try {
            Writer writer = new FileWriter(jsonPath);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static <T> void serializeBigObject(String jsonPath, T object, Class<T> classToSerialize, Gson customGson) throws IOException {

        FileWriter writer = new FileWriter(jsonPath);
        customGson.toJson(object, writer);
        writer.flush();
        writer.close();

    }

    public static String toPrettyFormat(String jsonString)
    {
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        return customGson.toJson(json);
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

