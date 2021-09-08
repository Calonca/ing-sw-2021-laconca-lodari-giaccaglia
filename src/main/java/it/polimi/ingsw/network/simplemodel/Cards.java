package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.client.json.Deserializator;
import it.polimi.ingsw.network.assets.CardAsset;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Cards {

    private Cards(){}

    private static final Map<UUID, LeaderCardAsset> leaderCardAssetsMap = Deserializator.networkLeaderCardsAssetsMapDeserialization();
    private static final Map<UUID, DevelopmentCardAsset> developmentCardAssetsMap = Deserializator.networkDevCardsAssetsDeserialization();

    public static Optional<DevelopmentCardAsset> getDevelopmentCardAsset(UUID devCardId){
        return developmentCardAssetsMap.containsKey(devCardId) ? Optional.of((new DevelopmentCardAsset(developmentCardAssetsMap.get(devCardId))))
                : Optional.empty();
    }

    public static Optional<CardAsset> getCardAsset(UUID cardId){

        return developmentCardAssetsMap.containsKey(cardId) ? Optional.of((new DevelopmentCardAsset(developmentCardAssetsMap.get(cardId))))

                :

                (leaderCardAssetsMap.containsKey(cardId) ? Optional.of(new LeaderCardAsset(leaderCardAssetsMap.get(cardId))) : Optional.empty());

    }

}
