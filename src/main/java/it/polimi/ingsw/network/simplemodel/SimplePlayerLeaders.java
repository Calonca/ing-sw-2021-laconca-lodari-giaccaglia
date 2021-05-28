package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.LeaderCardAsset;

import java.util.*;
import java.util.stream.Collectors;

public class SimplePlayerLeaders extends SimpleModelElement{

    private List<LeaderCardAsset> playerLeaders;
    private Map<UUID, Boolean> playerLeadersMap;

    public List<LeaderCardAsset> getPlayerLeaders() {
        return playerLeaders;
    }

    public SimplePlayerLeaders(){
        playerLeadersMap = new HashMap<>();
        playerLeaders = new ArrayList<>();
    }

    public SimplePlayerLeaders(Map<UUID, Boolean> playerLeadersMap){
        this.playerLeadersMap = playerLeadersMap;
    }

    public SimplePlayerLeaders(List<UUID> leaders){
        playerLeaders = leaders.stream().map(Cards::getLeaderCardAsset).collect(Collectors.toList());
        playerLeadersMap = new HashMap<>();
    }

    @Override
    public void update(SimpleModelElement element){

        SimplePlayerLeaders serverSimplePlayerLeaders = (SimplePlayerLeaders) element;

        Map<UUID, Boolean> map = serverSimplePlayerLeaders.playerLeadersMap;

        if (map!=null&&!map.isEmpty()) {
            playerLeadersMap = map;
            this.playerLeaders = (map).keySet()
                    .stream()
                    .map(Cards::getLeaderCardAsset).collect(Collectors.toList());

            for (LeaderCardAsset leader : playerLeaders) {
                UUID leaderId = leader.getCardId();
                leader.getNetworkLeaderCard().setLeaderState(map.get(leaderId));
            }
        } else this.playerLeaders = serverSimplePlayerLeaders.playerLeaders;
    }

}
