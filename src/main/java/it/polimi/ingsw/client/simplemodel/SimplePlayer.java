package it.polimi.ingsw.client.simplemodel;


import it.polimi.ingsw.client.json.Deserializator;
import it.polimi.ingsw.network.assets.LeaderCardAsset;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimplePlayer {

    private List<LeaderCardAsset> playerLeaders;

    private Map<UUID, LeaderCardAsset> leaderCardAssetMap;

    public SimplePlayer(){
        this.leaderCardAssetMap = Deserializator.leaderCardsAssetsMapDeserialization();
    }

    public void setPlayerLeaders(Map<UUID, Boolean> playerLeadersMap){

      playerLeaders =  playerLeadersMap
              .keySet()
              .stream()
              .map(leaderId -> leaderCardAssetMap.get(leaderId)).collect(Collectors.toList());

      for(LeaderCardAsset leader : playerLeaders){
          UUID leaderId = leader.getCardId();
          leader.getNetworkLeaderCard().setLeaderState(playerLeadersMap.get(leaderId));
      }

    }

    public List<LeaderCardAsset> getPlayerLeaders(){
        return playerLeaders;
    }
}
