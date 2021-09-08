package it.polimi.ingsw.network.simplemodel;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayersInfo extends SimpleModelElement{

    private Map<Integer, SimplePlayerInfo> simplePlayerInfoMap;
    private Set<Integer> farthestPlayers;
    private int lorenzoPosition;

    public PlayersInfo(){}

    public PlayersInfo(Map<Integer, SimplePlayerInfo> simplePlayerInfoMap, int lorenzoPosition){
        this.simplePlayerInfoMap = simplePlayerInfoMap;
        this.lorenzoPosition = lorenzoPosition;
    }

    @Override
    public void update(SimpleModelElement element) {

        PlayersInfo serverElement = ((PlayersInfo) element);
        simplePlayerInfoMap = serverElement.simplePlayerInfoMap;
        lorenzoPosition = serverElement.lorenzoPosition;
        calculateFarthestPlayers();

    }

    public Map<Integer, SimplePlayerInfo> getSimplePlayerInfoMap(){
        return this.simplePlayerInfoMap;
    }

    public Set<Integer> getFarthestPlayer(){
        return farthestPlayers;
    }

    public int getLorenzoPosition(){
        return lorenzoPosition;
    }

    private void calculateFarthestPlayers(){

        final int farthestPosition =  simplePlayerInfoMap.values().stream().mapToInt(SimplePlayerInfo::getCurrentPosition).max().orElse(0);

        farthestPlayers = simplePlayerInfoMap.entrySet()
                .stream()
                .filter(p -> p.getValue().getCurrentPosition()== farthestPosition)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }


}
