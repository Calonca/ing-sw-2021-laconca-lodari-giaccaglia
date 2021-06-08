package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkLeaderCard;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class SimplePlayerLeaders extends SimpleModelElement{

    private  List<LeaderCardAsset> playerLeaders;


                        //  state    isPlayable
    private  Map<UUID, Pair<Boolean, Boolean>>  playerLeadersMap;

    public List<LeaderCardAsset> getPlayerLeaders() {
        return playerLeaders;
    }

    public SimplePlayerLeaders(){
        playerLeadersMap = new HashMap<>();
        playerLeaders = new ArrayList<>();
    }

    public SimplePlayerLeaders( Map<UUID, Pair<Boolean, Boolean>>  playerLeadersMap){
        this.playerLeadersMap = playerLeadersMap;
    }

    public SimplePlayerLeaders(List<UUID> leaders){
        playerLeaders = leaders.stream().map(leaderId -> (LeaderCardAsset) Cards.getCardAsset(leaderId).orElseThrow()).collect(Collectors.toList());
        playerLeadersMap = new HashMap<>();
    }

    @Override
    public void update(SimpleModelElement element){

        SimplePlayerLeaders serverSimplePlayerLeaders = (SimplePlayerLeaders) element;

        Map<UUID, Pair<Boolean, Boolean>>  map = serverSimplePlayerLeaders.playerLeadersMap;

        if (map!=null&&!map.isEmpty()) {
            playerLeadersMap = map;
            this.playerLeaders = (map).keySet()
                    .stream()
                    .map(leaderId -> (LeaderCardAsset) Cards.getCardAsset(leaderId).orElseThrow()).collect(Collectors.toList());

            for (LeaderCardAsset leader : playerLeaders) {

                UUID leaderId = leader.getCardId();
                NetworkLeaderCard leaderCard = leader.getNetworkLeaderCard();
                Pair<Boolean, Boolean> leaderPair = map.get(leaderId);

                leaderCard.setLeaderState(leaderPair.getKey());
                leaderCard.setPlayableLeader(leaderPair.getValue());

            }
        } else this.playerLeaders = serverSimplePlayerLeaders.playerLeaders;
    }

}
