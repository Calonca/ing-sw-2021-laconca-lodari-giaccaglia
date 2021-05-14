package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Match {
    private UUID matchId;
    private boolean hasStarted=false;
    private List<String> onlineUsers = new ArrayList<>();
    private List<String> offlineUsers = new ArrayList<>();
    private transient List<ClientHandler> clientHandlers = new ArrayList<>();
    private GameModel game;
    private int maxPlayers;
    private Date createdTime;


    public Match(int maxPlayers){
        matchId = UUID.randomUUID();
        this.maxPlayers = maxPlayers;
        createdTime = new Date(System.currentTimeMillis());
    }

    boolean canAddPlayer(){
        return onlineUsers.size()<maxPlayers;
    }

    void addPlayer(String nickname, ClientHandler clientHandler){
        onlineUsers.add(nickname);
        clientHandlers.add(clientHandler);
    }


    public void startGame() {
        hasStarted=true;
        this.game = new GameModel(onlineUsers,onlineUsers.size()==1,this);
        game.getCurrentPlayer().setCurrentState(State.SETUP_PHASE);
    }

    public ClientHandler currentPlayerClientHandler(){
        String nickname = game.getCurrentPlayer().getNickName();
        int toReturn = IntStream.range(0,onlineUsers.size()).filter(i->onlineUsers.get(i).equals(nickname)).findFirst().orElse(0);
        return clientHandlers.get(toReturn);
    }

    public UUID getMatchId() {
        return matchId;
    }

    public String[] getOnlinePlayers(){
        return Stream.concat(
                onlineUsers.stream(),
                Stream.generate(()-> null)).limit(maxPlayers).toArray(String[]::new);
    }

    public boolean hasStarted(){return hasStarted;}
    public boolean hasNotStarted(){return !hasStarted;}

    public boolean sameID(UUID uuid) {return uuid.equals(matchId);}

    /**
     * Displays matches that the client can join or the current match
     * @param current the current match.
     */
    public boolean shouldDisplay(Match current){return canAddPlayer()||sameID(current.matchId);}

    public Stream<ClientHandler> clientsStream(){
        return clientHandlers.stream();
    }

    public GameModel getGame(){
        return game;
    }
    
    public void reconnectPlayer(String nickname){
        game.getOfflinePlayers().values().stream().filter(player -> player.getNickName().equals(nickname)).findFirst()
                .ifPresent((p)->
                        game.setOnlinePlayer(p)
                        );
    }
    public void validateEvent(Validable event) throws EventValidationFailedException {
        if (!event.validate(game))
            throw new EventValidationFailedException();
    }

    public State transitionToNextState(Validable event) throws EventValidationFailedException {
        GameStrategy gameStrategy = game.getCurrentPlayer().getStatesTransitionTable().getStrategy(game.getCurrentPlayer().getCurrentState(), event);
        return gameStrategy.execute(game,event);
    }

    public String getSaveName(){
        return onlineUsers.stream().reduce("", String::concat).concat("|").concat(matchId.toString());
    }

    public int getLastPos() {
        return onlineUsers.size()-1;
    }
}
