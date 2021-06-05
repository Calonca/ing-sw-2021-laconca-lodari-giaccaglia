package it.polimi.ingsw.network.simplemodel;

import java.util.List;
import java.util.Map;

public class EndGameInfo extends SimpleModelElement{

    Map<Integer, PlayerInfo> playerInfoMap;

    List<Integer> playersEndingTheGame;

    public EndGameInfo(){}

    public EndGameInfo(Map<Integer, PlayerInfo> playerInfoMap, List<Integer> playersEndingTheGame){

        this.playerInfoMap = playerInfoMap;
        this.playersEndingTheGame = playersEndingTheGame;

    }

    @Override
    public void update(SimpleModelElement element) {

        EndGameInfo serverElement = ((EndGameInfo) element);
        playerInfoMap = serverElement.playerInfoMap;
        playersEndingTheGame = serverElement.playersEndingTheGame;

    }

    public PlayerInfo getPlayerInfo(int playerIndex){
        return playerInfoMap.get(playerIndex);
    }

    public List<Integer> getPlayersEndingTheGame(){
        return playersEndingTheGame;
    }

    public static class PlayerInfo{

        private final int victoryPoints;
        private final boolean outcome;
        private final int faithTrackPosition;
        private final int lorenzoTrackPosition;

        public PlayerInfo(int victoryPoints, boolean outcome, int faithTrackPosition, int lorenzoTrackPosition){

            this.victoryPoints = victoryPoints;
            this.outcome = outcome;
            this.faithTrackPosition = faithTrackPosition;
            this.lorenzoTrackPosition = lorenzoTrackPosition;

        }

        public int getVictoryPoints(){
            return victoryPoints;
        }

        public boolean isOutcome() {
            return outcome;
        }

        public int getFaithTrackPosition() {
            return faithTrackPosition;
        }

        public int getLorenzoTrackPosition() {
            return lorenzoTrackPosition;
        }

    }

}
