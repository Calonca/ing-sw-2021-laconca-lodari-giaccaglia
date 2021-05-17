package it.polimi.ingsw.network.assets;

import com.rits.cloning.Cloner;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class CardAssetsContainer {

    private static Map<UUID, CardAsset> cardAssets;
    private static final Cloner cardAssetCloner = new Cloner();

    public CardAssetsContainer(Map<UUID, CardAsset> cardAssetsMap){
        cardAssets = cardAssetsMap;
    }

    public static Optional<CardAsset> getCardAsset(UUID cardId){
        return cardAssets.containsKey(cardId) ? Optional.of(cardAssetCloner.deepClone(cardAssets.get(cardId)))
                : Optional.empty();
    }





}
