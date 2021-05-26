package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.LeaderCardAsset;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimplePlayerLeaders extends SimpleModelElement{

    private List<LeaderCardAsset> playerLeaders;
    private Map<UUID, Boolean> playerLeadersMap;

    public SimplePlayerLeaders(){}

    public SimplePlayerLeaders(Map<UUID, Boolean> playerLeadersMap){
        this.playerLeadersMap = playerLeadersMap;
    }

    public SimplePlayerLeaders(List<UUID> leaders){
        playerLeaders = leaders.stream().map(Cards::getLeaderCardAsset).collect(Collectors.toList());
    }

    @Override
    public void update(SimpleModelElement element){

        SimplePlayerLeaders serverSimplePlayerLeaders = (SimplePlayerLeaders) element;

        this.playerLeadersMap = serverSimplePlayerLeaders.playerLeadersMap;
        Map<UUID, Boolean> map = serverSimplePlayerLeaders.playerLeadersMap;

        this.playerLeaders = (map).keySet()
                .stream()
                .map(Cards::getLeaderCardAsset).collect(Collectors.toList());

        for(LeaderCardAsset leader : playerLeaders){
            UUID leaderId = leader.getCardId();
            leader.getNetworkLeaderCard().setLeaderState(playerLeadersMap.get(leaderId));
        }
    }

}
