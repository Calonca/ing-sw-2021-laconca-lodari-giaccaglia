package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.network.assets.leaders.NetworkLeaderCard;

import java.util.UUID;

public class LeaderCardAsset extends CardAsset{

    private final NetworkLeaderCard networkLeaderCard;

    public LeaderCardAsset(NetworkLeaderCard networkLeaderCard, String front, String back, UUID cardId){
        super(front, back, cardId);
        this.networkLeaderCard = networkLeaderCard;
    }

    public NetworkLeaderCard getDevelopmentCard() {
        return networkLeaderCard;
    }

}
