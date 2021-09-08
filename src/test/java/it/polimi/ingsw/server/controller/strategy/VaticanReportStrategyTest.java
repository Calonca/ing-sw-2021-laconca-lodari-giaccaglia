package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.EndGameReason;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class VaticanReportStrategyTest {
    @Test
    public void executeSingleTripleVaticanAndLastTurn() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());


        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(25);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        assertEquals(VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>()).getKey(), State.END_PHASE);
        assertEquals(gamemodel.getMacroGamePhase(), GameModel.MacroGamePhase.LAST_TURN);

    }


    @Test
    public void executeSingleDoubleVatican() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(20);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        assertEquals(VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>()).getKey(), State.FINAL_PHASE);

    }

    @Test
    public void negateTrackStatus() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());


        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(21);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        assertEquals(VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>()).getKey(), State.FINAL_PHASE);

    }



    @Test
    public void negateIsActiveGame() {


        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(25);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.LAST_TURN);
        assertEquals(VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>()).getKey(), State.FINAL_PHASE);

    }


    @Test
    public void executeMultiplayerMultipleWinners() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, false ,new Match(2), onlineUsers);

        for(int i=0;i<40;i++)
        gamemodel.getPlayer(1).get().moveOnePosition();
        for(int i=0;i<40;i++)
            gamemodel.getPlayer(2).get().moveOnePosition();


        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(1);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>());



        assertEquals(gamemodel.getThisMatch().getReasonOfGameEnd(),  EndGameReason.MULTIPLE_TRACK_END.getEndGameReason());

        assertEquals(gamemodel.getMacroGamePhase(), GameModel.MacroGamePhase.LAST_TURN);

    }


    @Test
    public void executeMultiplayerSingleWinnerOtherPlayers() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, false ,new Match(2), onlineUsers);

        for(int i=0;i<40;i++)
            gamemodel.getPlayer(1).get().moveOnePosition();



        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(1);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>());



        assertEquals(gamemodel.getThisMatch().getReasonOfGameEnd(),  EndGameReason.TRACK_END.getEndGameReason());

        assertEquals(gamemodel.getMacroGamePhase(), GameModel.MacroGamePhase.LAST_TURN);

    }

    @Test
    public void badFaithAddition() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, false ,new Match(2), onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(3);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>());

        assertEquals(0,gamemodel.getPlayer(0).orElseThrow().getPlayerPosition());
        assertEquals(3,gamemodel.getPlayer(1).orElseThrow().getPlayerPosition());
        assertEquals(3,gamemodel.getPlayer(2).orElseThrow().getPlayerPosition());


    }


    @Test
    public void executeMultiplayerLorenzoWinner() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);

        gamemodel.getCurrentPlayer().getPersonalBoard().setBadFaithToAdd(25);

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        assertEquals(VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>()).getKey(), State.END_PHASE);
        assertEquals(gamemodel.getThisMatch().getReasonOfGameEnd(),  EndGameReason.LORENZO_REACHED_END.getEndGameReason());

        assertEquals(gamemodel.getMacroGamePhase(), GameModel.MacroGamePhase.LAST_TURN);

    }


    @Test
    public void executeSingleplayerOneWinner() {



        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());


        GameModel gamemodel = new GameModel(players, true ,new Match(1), onlineUsers);


        gamemodel.getCurrentPlayer().getPersonalBoard().getDiscardBox().addResources(new int[]{0,0,0,0,30});
        gamemodel.getCurrentPlayer().getPersonalBoard().discardResources();

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        assertEquals(VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>()).getKey(), State.END_PHASE);
        assertEquals(gamemodel.getThisMatch().getReasonOfGameEnd(),  EndGameReason.TRACK_END_SOLO.getEndGameReason());

        assertEquals(gamemodel.getMacroGamePhase(), GameModel.MacroGamePhase.LAST_TURN);

    }


    @Test
    public void executeMultiplayerPlayersWins() {

        Map<Integer, String> players = new HashMap<>();
        players.put(0,"testPlayer1");
        players.put(1,"testPlayer2");
        players.put(2,"testPlayer3");
        List<Integer> onlineUsers = new ArrayList<>(players.keySet());

        GameModel gamemodel = new GameModel(players, false ,new Match(2), onlineUsers);

        for(int i=0;i<23;i++)
            gamemodel.getPlayer(1).get().moveOnePosition();



        gamemodel.getCurrentPlayer().getPersonalBoard().getDiscardBox().addResources(new int[]{0,0,0,0,24});
        gamemodel.getCurrentPlayer().getPersonalBoard().discardResources();

        gamemodel.setMacroGamePhase(GameModel.MacroGamePhase.ACTIVE_GAME);
        VaticanReportStrategy.addFaithPointsStrategy(gamemodel,new ArrayList<>());



        assertEquals(gamemodel.getThisMatch().getReasonOfGameEnd(),  EndGameReason.TRACK_END.getEndGameReason());

        assertEquals(gamemodel.getMacroGamePhase(), GameModel.MacroGamePhase.LAST_TURN);

    }


}