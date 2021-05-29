package it.polimi.ingsw.network.assets;

import com.rits.cloning.Cloner;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CardAssetsContainer {

    private static Map<UUID, DevelopmentCardAsset> cardAssets;
    private static final Cloner cardAssetCloner = new Cloner();

    public static void setCardAssetsContainer(Map<UUID, DevelopmentCardAsset> cardAssetsMap){
        cardAssets = cardAssetsMap;
    }

    public static Optional<CardAsset> getCardAsset(UUID cardId){
        return cardAssets.containsKey(cardId) ? Optional.of((cardAssets.get(cardId)))
                : Optional.empty();
    }





}
