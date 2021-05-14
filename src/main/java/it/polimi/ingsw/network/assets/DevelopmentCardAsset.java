package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.network.assets.devcards.DevelopmentCard;

import java.util.UUID;

public class DevelopmentCardAsset extends CardAsset{

    private DevelopmentCard developmentCard;

    public DevelopmentCardAsset(){
    }

    public DevelopmentCardAsset(DevelopmentCard developmentCard, String front, String back, UUID cardId){
        super(front, back, cardId);
        this.developmentCard = developmentCard;
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

}
