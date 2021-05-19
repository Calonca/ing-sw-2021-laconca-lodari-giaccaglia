package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.network.assets.leaders.NetworkLeaderCard;
import javafx.util.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class LeaderCardAsset extends CardAsset{

    private final NetworkLeaderCard networkLeaderCard;

    public LeaderCardAsset(NetworkLeaderCard networkLeaderCard, String frontInactive, String backInactive, String frontActive, String backActive, UUID cardId){
        super(frontActive, backActive, frontInactive, backInactive,  cardId);
        this.networkLeaderCard = networkLeaderCard;
    }

    public NetworkLeaderCard getDevelopmentCard() {
        return networkLeaderCard;
    }

    @Override
    public Pair<Path, Path> getCardPaths() {
       return networkLeaderCard.isLeaderActive() ? getActiveCardPaths() : getInactiveCardPaths();
    }
}
