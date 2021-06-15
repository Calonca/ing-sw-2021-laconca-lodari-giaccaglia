package it.polimi.ingsw.client.json;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.jsonUtils.CommonGsonAdapters;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.leaders.*;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.jsonUtils.UUIDTypeAdapter;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import javafx.util.Pair;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Deserializator extends JsonUtility {

    public static final String clientConfigPathString = "/clientconfig/";

    //helper method to initialize gameModel list of 16 cards.leaders
    public static List<NetworkLeaderCard> leaderCardsDeserialization() {
        NetworkLeaderCard[] leaders = deserialize(clientConfigPathString + "LeadersConfig.json", NetworkLeaderCard[].class, true);
        return (Arrays.asList(leaders));
    }
    //helper method to load a 48 devcards array from json
    public static List<NetworkDevelopmentCard> devCardsListDeserialization() {
        NetworkDevelopmentCard[] cardsArray = deserialize(clientConfigPathString + "DevelopmentCardConfig.json", NetworkDevelopmentCard[].class, true);
        return (Arrays.asList(cardsArray));
    }

    public static Map<UUID, NetworkDevelopmentCard> devCardsMap() {
        return devCardsListDeserialization().stream().collect(Collectors.toMap(x -> UUID.randomUUID(), Function.identity()));
    }

    public static Map<UUID, DevelopmentCardAsset> networkDevCardsAssetsDeserialization() {

        Type type = new TypeToken<Map<UUID, DevelopmentCardAsset> >(){}.getType();
        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        return deserializeFromSourceRoot(clientConfigPathString + "ClientDevCardsAssetsMapConfig.json", type ,customGson);

    }

    public static Map<UUID, LeaderCardAsset> networkLeaderCardsAssetsMapDeserialization() {

        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapterFactory(CommonGsonAdapters.gsonNetworkLeaderAdapter).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        Type type = new TypeToken<Map<UUID, LeaderCardAsset> >(){}.getType();
        return deserializeFromSourceRoot(clientConfigPathString + "ClientNetLeaderCardsAssetsMap.json", type ,customGson);

    }

    public static void initializeMarblesFromConfig(){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter()).create();
        Type type = new TypeToken <Map<MarbleAsset, Path>>(){}.getType();
        Map<MarbleAsset, Path> marblesMap = it.polimi.ingsw.server.utils.Deserializator.deserializeFromSourceRoot(clientConfigPathString + "MarblesAssetsConfig.json", type, gson);
        marblesMap.forEach(MarbleAsset::setPath);
    }

    public static void initializeActionTokenFromConfig(){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter()).create();
        Type type = new TypeToken<Map<ActionTokenAsset, Pair<Path, String>>>(){}.getType();
        Map<ActionTokenAsset, Pair<Path, String>> tokensMap = it.polimi.ingsw.client.json.Deserializator.deserializeFromSourceRoot(clientConfigPathString + "TokenAssetsConfig.json", type, gson);
        tokensMap.forEach((asset, value) -> {
            asset.setFrontPath(value.getKey());
            asset.setEffectDescription(value.getValue());
        });
    }

}

