package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.client.json.Deserializator;
import it.polimi.ingsw.network.assets.CardAssetsContainer;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.IDLE;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Match {
    private final UUID matchId;
    private final List<User> onlineUsers = new ArrayList<>();
    private final List<String> offlineUsers = new ArrayList<>();
    private GameModel game;
    private final int maxPlayers;
    private Date createdTime;

    private class User{
        String nickname;
        transient ClientHandler clientHandler;

        public User(String nickname, ClientHandler clientHandler) {
            this.nickname = nickname;
            this.clientHandler = clientHandler;
        }


    }
    public Match(int maxPlayers){
        matchId = UUID.randomUUID();
        this.maxPlayers = maxPlayers;
        createdTime = new Date(System.currentTimeMillis());
    }

    boolean canAddPlayer(){
        return onlineUsers.size()<maxPlayers;
    }

    void addPlayer(String nickname, ClientHandler clientHandler){
        onlineUsers.add(new User(nickname,clientHandler));
    }


    public void startGame() {

        this.game = new GameModel(onlineUsers.stream().map(u->u.nickname).collect(Collectors.toList()), onlineUsers.size()==1,this);
        game.setGameStatus(true);
        CardAssetsContainer.setCardAssetsContainer(Deserializator.networkDevCardsAssetsDeserialization());
        game.getCurrentPlayer().setCurrentState(State.SETUP_PHASE);
        List<Element> elems = new ArrayList<>(Arrays.asList(Element.values()));
        notifyStateToAllPlayers(elems);
    }

    private Optional<User> currentUser(){
        String nickname = game.getCurrentPlayer().getNickName();
        return onlineUsers.stream().filter(u->u.nickname.equals(nickname)).findFirst();
    }

    public ClientHandler currentPlayerClientHandler(){
        return currentUser().map(u->u.clientHandler).orElse(onlineUsers.get(0).clientHandler);
    }

    boolean isSetupPhase(){
        return game.getOnlinePlayers().values().stream().map(Player::getCurrentState).anyMatch(s -> s.equals(State.SETUP_PHASE));
    }

    public UUID getMatchId() {
        return matchId;
    }

    public String[] getOnlinePlayers(){
        return Stream.concat(
                onlineUsers.stream().map(u->u.nickname),
                Stream.generate(()-> null)).limit(maxPlayers).toArray(String[]::new);
    }

    public boolean sameID(UUID uuid) {return uuid.equals(matchId);}

    /**
     * Displays matches that the client can join or the current match
     * @param current the current match.
     */
    public boolean shouldDisplay(Match current){return canAddPlayer()||sameID(current.matchId);}

    public Stream<ClientHandler> clientsStream(){
        return onlineUsers.stream().map(u->u.clientHandler);
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

    public void transitionToNextState(Validable event) throws EventValidationFailedException {
        GameStrategy gameStrategy = game.getCurrentPlayer().getStatesTransitionTable().getStrategy(game.getCurrentPlayer().getCurrentState(), event);
        if (gameStrategy==null)
            throw new EventValidationFailedException();
        Pair<State, List<Element>> data = gameStrategy.execute(game,event);
        game.getCurrentPlayer().setCurrentState(data.getKey());
        notifyStateToAllPlayers(data.getValue());

        //Todo check for better way
        if (game.getCurrentPlayer().getCurrentState().equals(State.IDLE)) {
            game.setCurrentPlayer(game.getNextPlayer());
            if (game.getCurrentPlayer().getCurrentState().equals(State.SETUP_PHASE)){
                List<Element> elems = new ArrayList<>(Arrays.asList(Element.values()));
                notifyStateToAllPlayers(elems);
            }
            else {
                IDLE IDLEStrategy = new IDLE();
                Pair<State, List<Element>> data2 = IDLEStrategy.execute(game, null);
                game.getCurrentPlayer().setCurrentState(data2.getKey());
                notifyStateToAllPlayers(data2.getValue());
            }
        }

    }

    public void notifyStateToAllPlayers(List<Element> elems){
        State state = getGame().getCurrentPlayer().getCurrentState();
        clientsStream().forEach(clientHandler -> {
            StateInNetwork stateInNetwork = state.toStateMessage(getGame(), elems);
            try {
                clientHandler.sendAnswerMessage(stateInNetwork);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public String getSaveName(){
        return onlineUsers.stream().map(u->u.nickname).reduce("", String::concat).concat("|").concat(matchId.toString());
    }

    public int getLastPos() {
        return onlineUsers.size()-1;
    }
}
