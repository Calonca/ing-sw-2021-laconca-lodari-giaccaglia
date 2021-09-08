package it.polimi.ingsw.network.simplemodel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndGameInfo extends SimpleModelElement{

    Map<Integer, Boolean> matchOutcomeMap;

    List<Integer> playersEndingTheGame;

    String causeOfEndWithNames = null;
    String causeOfEndReason = null;


    public EndGameInfo(){}

    public EndGameInfo(List<Integer> playersEndingTheGame, String causeOfEndString, Map<Integer, Boolean> matchOutcomeMap){

        this.matchOutcomeMap = matchOutcomeMap;
        this.playersEndingTheGame = playersEndingTheGame;
        this.causeOfEndReason= causeOfEndString;

    }

    @Override
    public void update(SimpleModelElement element) {

        EndGameInfo serverElement = ((EndGameInfo) element);
        playersEndingTheGame = serverElement.playersEndingTheGame;
        causeOfEndReason= serverElement.causeOfEndReason;
        this.matchOutcomeMap = serverElement.matchOutcomeMap;

    }


    public void handleCauseOfEndString(Map<Integer, SimplePlayerInfo> playerInfoMap){

        if(playerInfoMap.size() == 1)
            causeOfEndWithNames = causeOfEndReason;

        else{

            causeOfEndWithNames =  playersEndingTheGame.stream().map(playerInfoMap::get).map(SimplePlayerInfo::getNickname).collect((Collectors.joining(", "))) + causeOfEndReason;
        }


    }

    public Map<Integer, Boolean> getMatchOutcomeMap(){
        return matchOutcomeMap;
    }

    public String getCauseOfEndStringWithNames(){
        return causeOfEndWithNames;
    }

    public boolean isCauseOfEndBeenAnnounced(){
        return !causeOfEndReason.isBlank();
    }



}
