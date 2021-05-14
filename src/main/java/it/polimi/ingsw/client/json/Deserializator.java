package it.polimi.ingsw.client.json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.server.utils.UUIDTypeAdapter;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkLeaderCard;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Deserializator extends JsonUtility {

    public static final String clientConfigPathString = "src/main/resources/clientconfig/";

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

    public static Map<UUID, DevelopmentCardAsset> devCardsAssetsDeserialization() {
        Type type = new TypeToken<Map<UUID, DevelopmentCardAsset> >(){}.getType();
        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        return deserialize(clientConfigPathString + "DevCardsAssetsMapConfig.json", type ,customGson);

    }

    public static Map<UUID, LeaderCardAsset> leaderCardsAssetsMapDeserialization() {
        Gson customGson = new GsonBuilder().enableComplexMapKeySerialization().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).registerTypeHierarchyAdapter(Path.class, new PathConverter()).setPrettyPrinting().create();
        Type type = new TypeToken<Map<UUID, LeaderCardAsset> >(){}.getType();
        return deserialize(clientConfigPathString + "leaderCardsAssetsMap.json", type ,customGson);
    }

}
