package it.polimi.ingsw.network.simplemodel;

import java.util.Map;

public class PlayersInfo extends SimpleModelElement{

    Map<Integer, SimplePlayerInfo> simplePlayerInfoMap;

    public PlayersInfo(){}

    public PlayersInfo(Map<Integer, SimplePlayerInfo> simplePlayerInfoMap){
        this.simplePlayerInfoMap = simplePlayerInfoMap;
    }

    @Override
    public void update(SimpleModelElement element) {

        PlayersInfo serverElement = ((PlayersInfo) element);
        simplePlayerInfoMap = serverElement.simplePlayerInfoMap;

    }

    public Map<Integer, SimplePlayerInfo> getSimplePlayerInfoMap(){
        return this.simplePlayerInfoMap;
    }


    public static class SimplePlayerInfo{

        private final int currentVictoryPoints;
        private final int currentPosition;
        private final int currentLorenzoPosition;
        private final boolean isOnline;
        private final int playerIndex;

        public SimplePlayerInfo(int currentVictoryPoints, int currentPosition, int currentLorenzoPosition, boolean isOnline, int playerIndex) {
            this.currentVictoryPoints = currentVictoryPoints;
            this.currentPosition = currentPosition;
            this.currentLorenzoPosition = currentLorenzoPosition;
            this.isOnline = isOnline;
            this.playerIndex = playerIndex;
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

    }
}
