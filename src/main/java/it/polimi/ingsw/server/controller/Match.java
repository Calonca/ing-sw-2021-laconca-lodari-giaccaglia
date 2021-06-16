package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
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
    private final List<ClientHandler> onlineUsers = new ArrayList<>();
    private final List<ClientHandler> offlineUsers = new ArrayList<>();
    private GameModel game;
    private final int maxPlayers;
    private final Date createdTime;
    private String reasonOfGameEnd;

    public void restoreMatch(){

        setAllUsersOffline();
        setUserMatchesAfterServerRecovery();
        setGameMatch();

    }

    private void setUserMatchesAfterServerRecovery(){
        offlineUsers.forEach(user -> user.setMatch(this));
    }

    private void setAllUsersOffline(){
        offlineUsers.addAll(onlineUsers);
        onlineUsers.clear();
    }

    private void setGameMatch(){

        game.setMatch(this);
        offlineUsers.forEach(user ->
        {
            Player player = game.getPlayer(user.getNickname()).get();
            game.setOfflinePlayer(player);
        });

    }

    public String getPlayerNicknameFromHandler(ClientHandler clientHandler){

        return onlineUsers.stream()
                .filter(user -> user == clientHandler)
                .findFirst().get()
                .getNickname();

    }

    public Match(int maxPlayers){
        matchId = UUID.randomUUID();
        this.maxPlayers = maxPlayers;
        createdTime = new Date(System.currentTimeMillis());
        reasonOfGameEnd = "";
    }

    public void setOfflineUser(ClientHandler clientHandler){

        Optional<ClientHandler> optUser = onlineUsers
                .stream()
                .filter(user -> user.equals(clientHandler))
                .findFirst();

        if(optUser.isPresent()){
            ClientHandler user = optUser.get();
            onlineUsers.remove(user);
            offlineUsers.add(user);

            Player player = game.getPlayer(user.getNickname()).get();
            game.setOfflinePlayer(player);
        }
    }

    public boolean isPlayerOffline(String nickname){
        return offlineUsers.stream().anyMatch(user -> user.getNickname().equals(nickname));
    }

    //called when reconnecting
    public void setOnlineUser(String nickName, ClientHandler clientHandler){

        Optional<ClientHandler> optUser = offlineUsers.stream()
                .filter(user -> user.getNickname().equals(nickName))
                .findFirst();

        if(optUser.isPresent()){

            ClientHandler user = optUser.get();
            offlineUsers.remove(user);
            onlineUsers.add(clientHandler);
            Player player = game.getPlayer(nickName).get();
            game.setOnlinePlayer(player);

        }

    }

    boolean canAddPlayer(){
        return (onlineUsers.size() + offlineUsers.size())<maxPlayers;
    }

    public boolean isNicknameAvailable(String nickname){
        return onlineUsers.stream()
                .noneMatch(user-> user.getNickname().equals(nickname));
    }

    public boolean wasClientInMatch(String nickname){
        return offlineUsers.stream()
                .anyMatch(user-> user.getNickname().equals(nickname));
    }

    void addPlayer(String nickname, ClientHandler clientHandler){
        if(offlineUsers.stream().anyMatch(user -> user.getNickname().equals(nickname)))
            setOnlineUser(nickname, clientHandler);
        else
            onlineUsers.add(clientHandler);
    }

    public void startGame() {
        this.game = new GameModel(onlineUsers.stream().map(u->u.getNickname()).collect(Collectors.toList()), onlineUsers.size()==1,this);
        game.start();
        game.getCurrentPlayer().setCurrentState(State.SETUP_PHASE);
        List<Element> elements = new ArrayList<>(Arrays.asList(Element.values()));
        notifyStateToAllPlayers(elements, game.getCurrentPlayer());
    }

    public boolean isGameActive(){
        return Objects.nonNull(game) && game.isGameActive();
    }

    public GameModel getGame(){
        return game;
    }

    public void stopGame(){
        game.stop();
    }

    private Optional<ClientHandler> currentUser(){

        String nickname = game.getCurrentPlayer().getNickName();
        return onlineUsers.stream().filter(user->user.getNickname().equals(nickname)).findFirst();
    }

    boolean isSetupPhase(){
        return game.getOnlinePlayers()
                .values()
                .stream()
                .map(Player::getCurrentState)
                .anyMatch(state -> state.equals(State.SETUP_PHASE));
    }

    public UUID getMatchId() {
        return matchId;
    }

    public String[] getOnlinePlayers(){

        return Stream.concat(
                onlineUsers.stream().map(ClientHandler::getNickname),
                Stream.generate(()-> null)).limit(maxPlayers).toArray(String[]::new);

    }

    public String[] getOfflinePlayers(){

        return Stream.concat(
                offlineUsers.stream().map(ClientHandler::getNickname),
                Stream.generate(()-> null)).limit(maxPlayers).toArray(String[]::new);

    }



    public boolean sameID(UUID uuid) {return uuid.equals(matchId);}

    /**
     * Displays matches that the client can join or the current match
     * @param current the current match.
     */
    public boolean shouldDisplay(Match current){return canAddPlayer() || sameID(current.matchId);}

    public Stream<ClientHandler> clientsStream(){
        return onlineUsers.stream();
    }

    public void validateEvent(Validable event) throws EventValidationFailedException {
        if (!event.validate(game))
            throw new EventValidationFailedException();
    }

    public void transitionToNextState(Validable event) throws EventValidationFailedException {

        GameStrategy gameStrategy = game.getCurrentPlayer().getStatesTransitionTable().getStrategy(game.getCurrentPlayer().getCurrentState(), event);

        if (Objects.isNull(gameStrategy))
            throw new EventValidationFailedException();

        Pair<State, List<Element>> data = gameStrategy.execute(game, event);

        String nicknameOfPlayerSendingEvent =((Event) event).getPlayerNickname();

        Player playerSendingEvent = game.getPlayer(nicknameOfPlayerSendingEvent).get();
        data.getValue().add(Element.PlayersInfo);

        playerSendingEvent.setCurrentState(data.getKey());

        notifyStateToAllPlayers(data.getValue(), playerSendingEvent);

        //Todo check for better way
        if (playerSendingEvent.getCurrentState().equals(State.IDLE)) {

            if(game.getCurrentPlayer().equals(playerSendingEvent)) {

                game.setCurrentPlayer(game.getNextPlayer());

                if (game.getCurrentPlayer().getCurrentState().equals(State.SETUP_PHASE)) {

                    List<Element> elems = new ArrayList<>(Arrays.asList(Element.values()));

                   // nicknameOfPlayerSendingEvent = ((Event) event).getPlayerNickname();
                    playerSendingEvent = game.getCurrentPlayer();

                    notifyStateToAllPlayers(elems, playerSendingEvent);

                } else {
                    IDLE IDLEStrategy = new IDLE();
                    Pair<State, List<Element>> data2 = IDLEStrategy.execute(game, null);
                    game.getCurrentPlayer().setCurrentState(data2.getKey());

                    nicknameOfPlayerSendingEvent = ((Event) event).getPlayerNickname();
                    playerSendingEvent = game.getCurrentPlayer();
                    notifyStateToAllPlayers(data.getValue(), playerSendingEvent);
                }
            }


        }

    }

    public void notifyStateToAllPlayers(List<Element> elems, Player playerNotifying){

        State state = playerNotifying.getCurrentState();

        clientsStream().forEach(clientHandler -> {

            StateInNetwork stateInNetwork = state.toStateMessage(getGame(), elems, game.getPlayerIndex(playerNotifying));

            try {
                clientHandler.sendAnswerMessage(stateInNetwork);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    public String getSaveName(){
        return onlineUsers
                .stream()
                .map(ClientHandler::getNickname)
                .collect(Collectors.joining("," , "[","]"))
                .concat("|")
                .concat(matchId.toString());
    }

    public int getLastPos() {
        return onlineUsers.size()-1;
    }

    public String getReasonOfGameEnd(){
        return reasonOfGameEnd;
    }

    public void setReasonOfGameEnd(String reasonOfGameEnd){
        this.reasonOfGameEnd = reasonOfGameEnd;
    }

    public Date getCreatedTime(){
        return createdTime;
    }

    public boolean areAllPlayersOffline(){
        return onlineUsers.isEmpty();
    }




}
