package it.polimi.ingsw.client.json;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.client.messages.servertoclient.ClientMessage;
import it.polimi.ingsw.network.assets.CardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.leaders.*;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.jsonUtils.UUIDTypeAdapter;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.model.player.leaders.Leader;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Deserializator extends JsonUtility {

    public static final String clientConfigPathString = "/clientconfig/";

    //helper method to initialize gameModel list of 16 cards.leaders
    public static List<NetworkLeaderCard> leaderCardsDeserialization() {
        NetworkLeaderCard[] leaders = deserialize(clientConfigPathString + "LeadersConfig.json", NetworkLeaderCard[].class, new Gson());
        return (Arrays.asList(leaders));
    }
    //helper method to load a 48 devcards array from json
    public static List<NetworkDevelopmentCard> devCardsListDeserialization() {
        NetworkDevelopmentCard[] cardsArray = deserialize(clientConfigPathString + "DevelopmentCardConfig.json", NetworkDevelopmentCard[].class);
        return (Arrays.asList(cardsArray));
    }

    public static Map<UUID, NetworkDevelopmentCard> devCardsMap() {
        return devCardsListDeserialization().stream().collect(Collectors.toMap(x -> UUID.randomUUID(), Function.identity()));
    }

    public static Map<UUID, DevelopmentCardAsset> networkDevCardsAssetsDeserialization() {

        Type type = new TypeToken<Map<UUID, DevelopmentCardAsset> >(){}.getType();
        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        return deserialize(clientConfigPathString + "DevCardsAssetsMapConfig.json", type ,customGson);

    }

    public static Map<UUID, LeaderCardAsset> networkLeaderCardsAssetsMapDeserialization() {

        RuntimeTypeAdapterFactory<NetworkLeaderCard> jsonToNetworkLeaderListAdapter = RuntimeTypeAdapterFactory.of(NetworkLeaderCard.class);

        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkDepositLeaderCard.class);
        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkMarketLeaderCard.class);
        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkProductionLeaderCard.class);
        jsonToNetworkLeaderListAdapter.registerSubtype(NetworkDevelopmentDiscountLeaderCard.class);



        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapterFactory(jsonToNetworkLeaderListAdapter).registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        Type type = new TypeToken<Map<UUID, LeaderCardAsset> >(){}.getType();
        return deserialize(clientConfigPathString + "NetLeaderCardsAssetsMap.json", type ,customGson);

    }

}

