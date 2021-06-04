package it.polimi.ingsw.network.assets.marbles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.utils.Deserializator;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 *  <p>Enum class for <em>Market Marbles</em> of MarketBoard<br>
 *  Each Marble but White is mapped to a different {@link ResourceAsset Resource}.
 *  <ul>
 *  <li>{@link #WHITE}
 *  <li>{@link #BLUE}
 *  <li>{@link #GRAY}
 *  <li>{@link #YELLOW}
 *  <li>{@link #PURPLE}
 *  <li>{@link #RED}
 *  </ul>
 */
public enum MarbleAsset {
    /**
     * According to the official rulebook this <em>Marble</em> hasn't a mapped {@link ResourceAsset Resource}. <br>
     * Any possible mapping to a resource due to dedicated leader effect is done in the MarketBoard
     */
    WHITE(Paths.get("/assets/marbles/Masters of Renaissance_Marbles_WHITE.png")),
    /**
     * This <em>Marble</em> is mapped to the {@link ResourceAsset#SHIELD SHIELD} Resource
     */
    BLUE((Paths.get("/assets/marbles/Masters of Renaissance_Marbles_BLUE.png"))),

    /**
     * This <em>Marble</em> is mapped to the {@link ResourceAsset#STONE STONE} Resource
     */
    GRAY((Paths.get("/assets/marbles/Masters_of_Renaissance_Marbles_GRAY.png"))),

    /**
     * This <em>Marble</em> is mapped to the {@link ResourceAsset#GOLD GOLD} Resource
     */
    YELLOW((Paths.get("/assets/marbles/Masters of Renaissance_Marbles_YELLOW.png"))),

    /**
     * This <em>Marble</em> is mapped to the {@link ResourceAsset#SERVANT SERVANT} Resource
     */
    PURPLE((Paths.get("/assets/marbles/Masters of Renaissance_Marbles_PURPLE.png"))),

    /**
     * This <em>Marble</em> is mapped to the {@link ResourceAsset#FAITH FAITH} Resource
     */
    RED((Paths.get("/assets/marbles/Masters of Renaissance_Marbles_RED.png"))),

    INVALID((Paths.get("/assets/marbles/dummy.png")));


    private Path marbleAssetPath;
    private static final MarbleAsset[] vals = MarbleAsset.values();

    MarbleAsset(){}

    MarbleAsset(final Path marbleAssetPath) {
        this.marbleAssetPath = marbleAssetPath;
        }

    public Path getPath() {
        return this.marbleAssetPath;
    }

    public static MarbleAsset fromInt(int marbleNum){
        return marbleNum>vals.length|| marbleNum<0 ? INVALID: vals[marbleNum];
    }

    public static MarbleAsset fromUUID(UUID marbleId){
       return Arrays.stream(vals)
               .filter(marble -> UUID.nameUUIDFromBytes(marble.getName().getBytes(StandardCharsets.UTF_8)).equals(marbleId))
               .findFirst()
               .orElse(INVALID);
    }

    public String getName() {
        return this.name();
    }


    public void setPath(Path path){
        this.marbleAssetPath = path;
    }

    public static void initializeMarblesFromConfig(String path){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter()).create();
        Type type = new TypeToken <Map<MarbleAsset, Path>>(){}.getType();
        Map<MarbleAsset, Path> marblesMap = Deserializator.deserialize(path, type, gson);
        marblesMap.forEach(MarbleAsset::setPath);
    }

}
