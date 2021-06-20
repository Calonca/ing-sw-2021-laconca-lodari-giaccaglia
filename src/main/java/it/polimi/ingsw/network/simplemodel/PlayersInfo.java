package it.polimi.ingsw.network.simplemodel;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayersInfo extends SimpleModelElement{

    private Map<Integer, SimplePlayerInfo> simplePlayerInfoMap;
    private Set<Integer> farthestPlayers;


    public PlayersInfo(){}

    public PlayersInfo(Map<Integer, SimplePlayerInfo> simplePlayerInfoMap){
        this.simplePlayerInfoMap = simplePlayerInfoMap;
    }

    @Override
    public void update(SimpleModelElement element) {

        PlayersInfo serverElement = ((PlayersInfo) element);
        simplePlayerInfoMap = serverElement.simplePlayerInfoMap;
        calculateFarthestPlayers();

    }

    public Map<Integer, SimplePlayerInfo> getSimplePlayerInfoMap(){
        return this.simplePlayerInfoMap;
    }

    public Set<Integer> getFarthestPlayer(){
        return farthestPlayers;
    }

    private void calculateFarthestPlayers(){

        final int farthestPosition =  simplePlayerInfoMap.values().stream().mapToInt(SimplePlayerInfo::getCurrentPosition).max().orElse(0);

        farthestPlayers = simplePlayerInfoMap.entrySet()
                .stream()
                .filter(p -> p.getValue().currentPosition== farthestPosition)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    public static class SimplePlayerInfo{

        private final int currentVictoryPoints;
        private final int currentPosition;
        private final int currentLorenzoPosition;
        private final boolean isOnline;
        private final int playerIndex;
        private final String nickname;

        public SimplePlayerInfo(int currentVictoryPoints, int currentPosition, int currentLorenzoPosition, boolean isOnline, int playerIndex, String nickname) {
            this.currentVictoryPoints = currentVictoryPoints;
            this.currentPosition = currentPosition;
            this.currentLorenzoPosition = currentLorenzoPosition;
            this.isOnline = isOnline;
            this.playerIndex = playerIndex;
            this.nickname = nickname;
        }

        public int getCurrentVictoryPoints() {
            return currentVictoryPoints;
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public int getLorenzoPosition(){
            return currentLorenzoPosition;
        }

        public boolean isOnline() {
            return isOnline;
        }

        public int getPlayerIndex() {
            return playerIndex;
        }

        public String getNickname(){
            return nickname;
        }


    }
}
