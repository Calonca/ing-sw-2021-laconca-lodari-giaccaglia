package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.assets.LeaderCardAsset;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimplePlayer {

    private List<LeaderCardAsset> playerLeaders;

    private SimplePersonalBoard personalBoard;

    public SimplePlayer(){}

    public void updatePlayerLeaders(Map<UUID, Boolean> playerLeadersMap){
      playerLeaders =  playerLeadersMap
              .keySet()
              .stream()
              .map(Cards::getLeaderCardAsset).collect(Collectors.toList());

      for(LeaderCardAsset leader : playerLeaders){
          UUID leaderId = leader.getCardId();
          leader.getNetworkLeaderCard().setLeaderState(playerLeadersMap.get(leaderId));
      }
    }

    public List<LeaderCardAsset> getPlayerLeaders(){
        return playerLeaders;
    }

    public SimplePersonalBoard getPersonalBoard(){
        return personalBoard;
    }

}
