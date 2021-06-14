package it.polimi.ingsw.server.utils;

import com.google.gson.*;

import java.lang.reflect.Type;

public class InterfaceAdapter <T> implements JsonSerializer<T>, JsonDeserializer<T>{

    private static final String CLASSNAME = "CLASSNAME";
    private static final String DATA = "DATA";

    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = primitive.getAsString();
        Class objectClass = getObjectClass(className);
        return jsonDeserializationContext.deserialize(jsonObject.get(DATA), objectClass);
    }

    public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASSNAME, jsonElement.getClass().getName());
        jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
        return jsonObject;
    }

    /****** Helper method to get the className of the object to be deserialized *****/
    public Class getObjectClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
    }
}

