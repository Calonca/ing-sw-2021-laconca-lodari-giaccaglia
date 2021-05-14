package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.network.assets.leaders.LeaderCard;

import java.util.UUID;

public class LeaderCardAsset extends CardAsset{

    private final LeaderCard leaderCard;

    public LeaderCardAsset(LeaderCard leaderCard, String front, String back, UUID cardId){
        super(front, back, cardId);
        this.leaderCard = leaderCard;
    }

    public LeaderCard getDevelopmentCard() {
        return leaderCard;
    }

}
