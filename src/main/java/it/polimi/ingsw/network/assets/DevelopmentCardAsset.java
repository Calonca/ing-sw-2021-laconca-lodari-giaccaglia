package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.UUID;

public class DevelopmentCardAsset extends CardAsset{

    private NetworkDevelopmentCard networkDevelopmentCard;

    public DevelopmentCardAsset(){
    }

    public DevelopmentCardAsset(NetworkDevelopmentCard networkDevelopmentCard, String frontPurchasable, String backPurchasable, String frontNotPurchasable, String backNotPurchasable, UUID cardId){
        super(frontPurchasable, backPurchasable, frontNotPurchasable, backNotPurchasable, cardId);
        this.networkDevelopmentCard = networkDevelopmentCard;
    }

    public DevelopmentCardAsset(DevelopmentCardAsset toClone){
        super(
                toClone.getActiveCardPaths().getKey().toString(),
                toClone.getActiveCardPaths().getValue().toString(),
                toClone.getInactiveCardPaths().getKey().toString(),
                toClone.getInactiveCardPaths().getValue().toString(),
                toClone.getCardId()
        );

        this.networkDevelopmentCard = toClone.getDevelopmentCard();
    }

    public NetworkDevelopmentCard getDevelopmentCard() {
        return networkDevelopmentCard;
    }

    @Override
    public Pair<Path, Path> getCardPaths() {
        return networkDevelopmentCard.isSelectable() ? getActiveCardPaths() : getInactiveCardPaths();
    }

}
