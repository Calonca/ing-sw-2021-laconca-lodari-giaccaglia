package it.polimi.ingsw.network.assets.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.utils.Deserializator;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Enum class for resources. Resources order is the same as in Box.
 * New resources are to be added after STONE.
 * For each TOCHOOSE the player will perform a choice, selecting a basic resource.
 * Each BADFAITH point will be consumed and all other players will advance on the FaithTrack.
 */

public enum ResourceAsset
{
    GOLD(0, Paths.get("src/main/resources/assets/resources/GOLD.png")),
    SERVANT(1, Paths.get("src/main/resources/assets/resources/SERVANT.png")),
    SHIELD(2, Paths.get("src/main/resources/assets/resources/SHIELD.png")),
    STONE(3, Paths.get("src/main/resources/assets/resources/STONE.png")),
    FAITH(4, Paths.get("src/main/resources/assets/resources/FAITH.png")),
    TOCHOOSE(6, Paths.get("src/main/resources/assets/resources/DUMMY_PATH")),
    EMPTY(7, Paths.get("src/main/resources/assets/resources/EMPTY.png"));

    private int resourceNumber;

    private Path resourcePath;

    /**
     * Indicated the number of "physical" resources
     */
    public static final int nRes = 4;

    /**
     * Array containing "physical" resources, used to get resource from it's number in the ordering.
     */
    private static final ResourceAsset[] vals = ResourceAsset.values();


    ResourceAsset(final int resourceNumber, final Path resourcePath)
    {
        this.resourceNumber= resourceNumber;
        this.resourcePath = resourcePath;
    }

    public void setResourceNumber(int resourceNumber){
        this.resourceNumber = resourceNumber;
    }

    public void setResourcePath(Path resourcePath){
        this.resourcePath = resourcePath;
    }

    /**
     * Return the  {@link ResourceAsset} corresponding to given value in the {@link ResourceAsset} ordering,
     * returns {@link ResourceAsset#EMPTY} if the given value is outside the array
     * @param rNum int representing the {@link ResourceAsset} ordering
     * @return a {@link ResourceAsset}
     */
    public static ResourceAsset fromInt(int rNum){
        return rNum>vals.length||rNum<0 ? EMPTY: vals[rNum];
    }

    public int getResourceNumber()
    {
        return this.resourceNumber;
    }

    public Path getResourcePath() {
        return this.resourcePath;
    }

    public static void initializeResourcesFromConfig(String path){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter()).create();
        Type type = new TypeToken<Map<ResourceAsset, Pair<Integer, Path>>>(){}.getType();

        Map<ResourceAsset, Pair<Integer, Path>> resourcesMap = Deserializator.deserialize(path, type, gson);
        resourcesMap.forEach((key, pair) -> {
            key.setResourceNumber(pair.getKey());
            key.setResourcePath(pair.getValue());
        });
    }

}