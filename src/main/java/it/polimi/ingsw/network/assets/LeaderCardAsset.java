package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.server.model.player.leaders.Leader;

import java.util.UUID;

public class LeaderCardAsset extends CardAsset{

    private final Leader leaderCard;

    public LeaderCardAsset(Leader leaderCard, String front, String back, UUID cardId){
        super(front, back, cardId);
        this.leaderCard = leaderCard;
    }

    public Leader getDevelopmentCard() {
        return leaderCard;
    }

}
