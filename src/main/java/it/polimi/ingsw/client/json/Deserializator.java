package it.polimi.ingsw.client.json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.leaders.NetworkLeaderCard;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.jsonUtils.UUIDTypeAdapter;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Deserializator extends JsonUtility {

    public static final String clientConfigPathString = "/clientconfig/";

    /**
     *  @return a Leader array from json of lenght 16
     */
    public static List<NetworkLeaderCard> leaderCardsDeserialization() {

        NetworkLeaderCard[] leaders = deserializeFromSourceRoot(
                clientConfigPathString + "LeadersConfig.json", NetworkLeaderCard[].class,
                customGsonBuilder.registerTypeAdapterFactory(CommonGsonAdapters.gsonNetworkLeaderAdapter)
                        .registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create());

        return (Arrays.asList(leaders));
    }
    /**
     *  @return A DevelopmentCard array from json of lenght 48
     */
    public static List<NetworkDevelopmentCard> devCardsListDeserialization() {
        NetworkDevelopmentCard[] cardsArray = deserializeFromSourceRoot(
                clientConfigPathString + "DevelopmentCardConfig.json", NetworkDevelopmentCard[].class,
                 customGsonBuilder.registerTypeAdapterFactory(CommonGsonAdapters.gsonNetworkLeaderAdapter)
                .registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create());

        return (Arrays.asList(cardsArray));
    }


    /**
     *
     * @return the mapping of DevelopmentCards to UUIDs
     */
    public static Map<UUID, NetworkDevelopmentCard> devCardsMap() {
        return devCardsListDeserialization().stream().collect(Collectors.toMap(x -> UUID.randomUUID(), Function.identity()));
    }


    /**
     *
     * @return the mapping of DevelopmentCard to UUIDs
     */
    public static Map<UUID, DevelopmentCardAsset> networkDevCardsAssetsDeserialization() {

        Type type = new TypeToken<Map<UUID, DevelopmentCardAsset> >(){}.getType();
        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        return deserializeFromSourceRoot(clientConfigPathString + "ClientDevCardsAssetsMapConfig.json", type ,customGson);

    }

    /**
     *
     * @return the mapping of Leader to UUIDs
     */
    public static Map<UUID, LeaderCardAsset> networkLeaderCardsAssetsMapDeserialization() {
        Gson gson = customGsonBuilder.registerTypeAdapterFactory(CommonGsonAdapters.gsonNetworkLeaderAdapter).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
        Type type = new TypeToken<Map<UUID, LeaderCardAsset> >(){}.getType();
        return deserializeFromSourceRoot(clientConfigPathString + "ClientNetLeaderCardsAssetsMap.json", type ,gson);

    }
    /**
     * Initializes the standard Marble resources
     */
    public static void initializeMarblesFromConfig(){
        Type type = new TypeToken <Map<MarbleAsset, Path>>(){}.getType();
        Map<MarbleAsset, Path> marblesMap = JsonUtility.deserializeFromSourceRoot(clientConfigPathString + "MarblesAssetsConfig.json", type, customGson);
        marblesMap.forEach(MarbleAsset::setPath);
    }


    /**
     * Initializes the standard action tokens
     */
    public static void initializeActionTokenFromConfig(){
        Type type = new TypeToken<Map<ActionTokenAsset, Pair<Path, String>>>(){}.getType();
        Map<ActionTokenAsset, Pair<Path, String>> tokensMap = Deserializator.deserializeFromSourceRoot(clientConfigPathString + "TokenAssetsConfig.json", type, customGson);

        tokensMap.forEach((asset, value) -> {
            asset.setFrontPath(value.getKey());
            asset.setEffectDescription(value.getValue());
        });
    }

}

