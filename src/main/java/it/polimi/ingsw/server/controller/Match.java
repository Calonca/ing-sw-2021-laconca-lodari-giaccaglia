package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;

import java.util.*;

public class Match {
    private UUID gameID;
    private List<String> registeredUsers = new ArrayList<>();
    private transient List<ClientHandler> clientHandlers = new ArrayList<>();
    private GameModel game;
    private int maxPlayers;
    private Date createdTime;


    public Match(int maxPlayers){
        gameID = UUID.randomUUID();
        this.maxPlayers = maxPlayers;
    }

    public boolean canAddPlayer(){
        return registeredUsers.size()<maxPlayers;
    }

    public void addPlayer(String nickname, ClientHandler clientHandler){
        registeredUsers.add(nickname);
        clientHandlers.add(clientHandler);
    }

    public void startGame() {
        this.game = new GameModel(registeredUsers,registeredUsers.size()==1);
    }

    public GameModel getModel(){
        return game;
    }
    
    public void reconnectPlayer(String nickname){
        game.getOfflinePlayers().values().stream().filter(player -> player.getNickName().equals(nickname)).findFirst()
                .ifPresent((p)->
                        game.setOnlinePlayer(p)
                        );
    }
    public void validateEvent(Validable event) throws EventValidationFailedException {
        if (true)
            throw new EventValidationFailedException();
    }

    public State transitionToNextState(Validable event) {
        GameStrategy gameStrategy = game.getCurrentPlayer().getStatesTransitionTable().getStrategy(game.getCurrentPlayer().getCurrentState(), event);
        return gameStrategy.execute(game);
    }

    public String getSaveName(){
        return registeredUsers.stream().reduce("", String::concat).concat("|").concat(gameID.toString());
    }

}
