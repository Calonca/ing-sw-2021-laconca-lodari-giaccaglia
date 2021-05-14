package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;

import java.util.UUID;

public class DevelopmentCardAsset extends CardAsset{

    private NetworkDevelopmentCard networkDevelopmentCard;

    public DevelopmentCardAsset(){
    }

    public DevelopmentCardAsset(NetworkDevelopmentCard networkDevelopmentCard, String front, String back, UUID cardId){
        super(front, back, cardId);
        this.networkDevelopmentCard = networkDevelopmentCard;
    }

    public NetworkDevelopmentCard getDevelopmentCard() {
        return networkDevelopmentCard;
    }

}
