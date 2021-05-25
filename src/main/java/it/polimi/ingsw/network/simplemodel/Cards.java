package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.client.json.Deserializator;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;

import java.util.Map;
import java.util.UUID;

public class Cards {

    private final static Map<UUID, LeaderCardAsset> leaderCardAssetsMap = Deserializator.leaderCardsAssetsMapDeserialization();
    private final static Map<UUID, DevelopmentCardAsset> developmentCardAssetsMap = Deserializator.devCardsAssetsDeserialization();

    public static LeaderCardAsset getLeaderCardAsset(UUID leaderCardId){
        return leaderCardAssetsMap.get(leaderCardId);
    }

    public static DevelopmentCardAsset getDevelopmentCardAsset(UUID devCardId){
        return developmentCardAssetsMap.get(devCardId);
    }

}
