package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.server.model.player.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndGameInfo extends SimpleModelElement{

    Map<Integer, PlayerInfo> playerInfoMap;

    List<Integer> playersEndingTheGame;

    List<String> causeOfEnd;


    public EndGameInfo(){}

    public EndGameInfo(Map<Integer, PlayerInfo> playerInfoMap, List<Integer> playersEndingTheGame, String causeOfEnd){

        this.playerInfoMap = playerInfoMap;
        this.playersEndingTheGame = playersEndingTheGame;


    }

    @Override
    public void update(SimpleModelElement element) {

        EndGameInfo serverElement = ((EndGameInfo) element);
        playerInfoMap = serverElement.playerInfoMap;
        playersEndingTheGame = serverElement.playersEndingTheGame;


    }


    private void handleCauseOfEndString(String causeOfEnd){

        String causeOfEndString;

        if(playerInfoMap.size() == 1) {
            causeOfEndString = playerInfoMap.get(0).playerNickname + causeOfEnd;
        }

        else{

            causeOfEndString = playerInfoMap.values().stream().map(playerInfo -> playerInfo.playerNickname).collect((Collectors.joining(", "))) + causeOfEnd;
        }

        this.causeOfEnd.add(causeOfEndString);

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
        private final String playerNickname;

        public PlayerInfo(int victoryPoints, boolean outcome, int faithTrackPosition, int lorenzoTrackPosition, String playerNickname){

            this.victoryPoints = victoryPoints;
            this.outcome = outcome;
            this.faithTrackPosition = faithTrackPosition;
            this.lorenzoTrackPosition = lorenzoTrackPosition;
            this.playerNickname = playerNickname;

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

        public String getPlayerNickname(){
            return playerNickname;
        }

    }

}
