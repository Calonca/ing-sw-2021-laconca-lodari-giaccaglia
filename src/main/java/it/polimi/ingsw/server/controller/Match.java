package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.network.messages.servertoclient.state.ElementsInNetwork;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import it.polimi.ingsw.network.util.Util;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.IDLE;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Match {

    private final UUID matchId;
    private final Map<Integer, ClientHandler> onlineUsers = new HashMap<>();
    private final Map<Integer, ClientHandler> offlineUsers = new HashMap<>();
    private GameModel game;
    private int currentPlayersAmount = 0;
    private final int maxPlayers;
    private final Date createdTime;
    private String reasonOfGameEnd;

    public void restoreMatch(){

        setAllUsersOffline();
        setUserMatchesAfterServerRecovery();
        setGameMatch();

    }

    private void setUserMatchesAfterServerRecovery(){
        offlineUsers.values().forEach(user -> user.setMatch(this));
    }

    private void setAllUsersOffline(){

        offlineUsers.putAll(onlineUsers);
        onlineUsers.clear();
    }

    private void setGameMatch(){

        if(Objects.nonNull(game))
            game.setMatch(this);

        offlineUsers.values().forEach(user ->
        {
            if(Objects.nonNull(game) && game.getPlayer(user.getNickname()).isPresent()) {
                Player player = game.getPlayer(user.getNickname()).get();
                game.setOfflinePlayer(player);
            }
        });

    }

    public String getPlayerNicknameFromHandler(ClientHandler clientHandler){

        return onlineUsers.values().stream()
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

        Optional<ClientHandler> optUser = onlineUsers.values()
                .stream()
                .filter(user -> user.equals(clientHandler))
                .findFirst();

        if(optUser.isPresent()){
            ClientHandler user = optUser.get();
            int userIndex = Util.getKeyByValue(onlineUsers, user).get();
            onlineUsers.remove(userIndex);
            offlineUsers.put(userIndex, user);

           if(Objects.nonNull(game)) {  // game was created/existed before disconnection
               Player player = game.getPlayer(user.getNickname()).get();
               game.setOfflinePlayer(player);
           }
        }
    }

    public boolean isPlayerOffline(String nickname){
        return offlineUsers.values().stream().anyMatch(user -> user.getNickname().equals(nickname));
    }

    //called when reconnecting
    public void setOnlineUser(String nickName, ClientHandler clientHandler){

        Optional<ClientHandler> optUser = offlineUsers.values().stream()
                .filter(user -> user.getNickname().equals(nickName))
                .findFirst();

        if(optUser.isPresent()){

            ClientHandler user = optUser.get();
            int userIndex = Util.getKeyByValue(offlineUsers, user).get();
            offlineUsers.remove(userIndex);
            onlineUsers.put(userIndex, clientHandler);

            if(Objects.nonNull(game)) {  // if game has been created set player online after reconnecting
                Player player = game.getPlayer(nickName).get();
                game.setOnlinePlayer(player);
                if (game.getOnlinePlayers().size() == 1)
                    game.setCurrentPlayer(player);
            }

        }

    }

    boolean canAddPlayer(){
        return (onlineUsers.size() + offlineUsers.size())<maxPlayers;
    }

    public boolean isNicknameAvailable(String nickname){
        return onlineUsers.values().stream()
                .noneMatch(user-> user.getNickname().equals(nickname));
    }

    public boolean wasClientInMatch(String nickname){
        return offlineUsers.values().stream()
                .anyMatch(user-> user.getNickname().equals(nickname));
    }

    public int addPlayer(String nickname, ClientHandler clientHandler){
        if(offlineUsers.values().stream().anyMatch(user -> user.getNickname().equals(nickname))) {
            setOnlineUser(nickname, clientHandler);
            return Util.getKeyByValue(onlineUsers, clientHandler).get();
        }
        else {
            onlineUsers.put(currentPlayersAmount, clientHandler);
            int playerIndex = currentPlayersAmount;
            currentPlayersAmount = currentPlayersAmount+1;
            return playerIndex ;
        }
    }

    public void startGame() {

        Map<Integer, String> playersMap =  Stream
                .concat(onlineUsers.entrySet().stream(), offlineUsers.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getNickname()));

        List<Integer> onlineUsersIndexes = new ArrayList<>(onlineUsers.keySet());

        this.game = new GameModel(playersMap, playersMap.size()==1, this, onlineUsersIndexes);

        game.start();
        game.getCurrentPlayer().setCurrentState(State.SETUP_PHASE);
        List<Element> elements = new ArrayList<>(Arrays.asList(Element.values()));
        notifyStateToAllPlayers(elements, game.getCurrentPlayer().getNickname());
    }

    public boolean isGameActive(){
        return Objects.nonNull(game) && game.isGameActive();
    }

    public Optional<GameModel> getGameIfPresent(){
        return Optional.ofNullable(game);
    }

    public void stopGameIfPresent(){
        if(Objects.nonNull(game))
            game.stop();
    }

    public UUID getMatchId() {
        return matchId;
    }

    public String[] getOnlinePlayers(){

        if(onlineUsers.isEmpty())
            return Stream.generate(() -> "available slot").limit(((long)maxPlayers - offlineUsers.size())).toArray(String[]::new);

        return Stream.concat(onlineUsers.values().stream().map(ClientHandler::getNickname),
                Stream.generate(()-> "available slot")).limit((long)maxPlayers - offlineUsers.size()).toArray(String[]::new);

    }

    public String[] getOfflinePlayers(){

        return offlineUsers.values().stream().map(ClientHandler::getNickname).toArray(String[]::new);

    }

    public Stream<ClientHandler> clientsStream(){
        return onlineUsers.values().stream();
    }

    public void validateEvent(Validable event) throws EventValidationFailedException {
        if (!event.validate(game))
            throw new EventValidationFailedException();
    }

    public void transitionToNextState(Validable event) throws EventValidationFailedException {

        String nicknameOfPlayerSendingEvent = ((Event)event).getPlayerNickname();

        Optional<Player> optionalPlayer =game.getPlayer(nicknameOfPlayerSendingEvent);

        if(optionalPlayer.isEmpty())
            throw new EventValidationFailedException();

        Player playerSendingEvent = optionalPlayer.get();
        GameStrategy gameStrategy =  playerSendingEvent.getStatesTransitionTable().getStrategy(playerSendingEvent.getCurrentState(), event);

        if (Objects.isNull(gameStrategy))
            throw new EventValidationFailedException();

        Pair<State, List<Element>> data = gameStrategy.execute(game, event);
        data.getValue().add(Element.PLAYERS_INFO);

      if(data.getValue().contains(Element.SIMPLE_FAITH_TRACK)) {
            List<Element> elements = new ArrayList<>();
            elements.add(Element.SIMPLE_FAITH_TRACK);
            updateElementsForAllPlayers(elements);
        }

        playerSendingEvent.setCurrentState(data.getKey());
        notifyStateToAllPlayers(data.getValue(), nicknameOfPlayerSendingEvent);


        if ((playerSendingEvent.getCurrentState().equals(State.IDLE)
                        && playerSendingEvent.equals(game.getCurrentPlayer())) && game.getCurrentPlayer().equals(playerSendingEvent)){

                game.setCurrentPlayer(game.getNextPlayer().get());

                if (game.getCurrentPlayer().getCurrentState().equals(State.SETUP_PHASE)) {

                    List<Element> elems = new ArrayList<>(Arrays.asList(Element.values()));
                    playerSendingEvent = game.getCurrentPlayer();
                    notifyStateToAllPlayers(elems, playerSendingEvent.getNickname());

                } else {

                    playerSendingEvent = game.getCurrentPlayer();
                    IDLE idleStrategy = new IDLE();
                    Pair<State, List<Element>> data2 = idleStrategy.execute(game, null);
                    playerSendingEvent.setCurrentState(data2.getKey());


                    if(playerSendingEvent.getCurrentState().equals(State.END_PHASE) && game.getMacroGamePhase().equals(GameModel.MacroGamePhase.GAME_ENDED) && !game.isSinglePlayer()){

                        setMatchPlayersOutcomes();
                        notifyStateToAllPlayers(data2.getValue(), playerSendingEvent.getNickname());

                        for(Player player : game.getMatchPlayers().values()){

                            if(!player.equals(playerSendingEvent)){
                                List<Element> elementsToUpdate = new ArrayList<>();
                                elementsToUpdate.add(Element.END_GAME_INFO);
                                player.setCurrentState(State.END_PHASE);
                                game.setCurrentPlayer(player);

                                notifyStateToAllPlayers(elementsToUpdate, player.getNickname());
                            }
                        }
                    }

                    else
                        notifyStateToAllPlayers(data2.getValue(), playerSendingEvent.getNickname());

                }

        }

    }

    public void transitionToNextStateAfterDisconnection(String playerNickname){
        if(game.isSinglePlayer())
            return;

        Player playerDisconnected = game.getPlayer(playerNickname).get();

        //if player disconnecting was the one with the inkwell, inkwell is given to the previous player
        //in counterclockwise order

        if(game.getPlayerIndex(playerDisconnected) == game.getIndexOfPlayerWithInkWell())
            game.updateIndexOfPlayerWithInkWell();


        if(game.getCurrentPlayer().equals(playerDisconnected) && game.getNextPlayer().isPresent()){

                game.setCurrentPlayer(game.getNextPlayer().get());

                Player newCurrentPlayer = game.getCurrentPlayer();

                if (newCurrentPlayer.getCurrentState().equals(State.IDLE)) {

                    IDLE idleStrategy = new IDLE();
                    Pair<State, List<Element>> data = idleStrategy.execute(game, null);

                    newCurrentPlayer.setCurrentState(data.getKey());
                    notifyStateToAllPlayers(data.getValue(), newCurrentPlayer.getNickname());
                }

                else if(newCurrentPlayer.getCurrentState().equals(State.SETUP_PHASE)){

                    List<Element> elements = new ArrayList<>(Arrays.asList(Element.values()));

                    newCurrentPlayer.setCurrentState(State.SETUP_PHASE);
                    notifyStateToAllPlayers(elements, newCurrentPlayer.getNickname());
                }

        }


    }

    public void notifyStateToAllPlayers(List<Element> elements, String playerNotifying){

        Player player = game.getPlayer(playerNotifying).get();

       State state = player.getCurrentState();

        clientsStream().forEach(clientHandler -> {

            StateInNetwork stateInNetwork = state.toStateMessage(game, elements, game.getPlayerIndex(player));

            clientHandler.sendAnswerMessage(stateInNetwork);
        });

    }

    public void updateOtherPlayersCachesMessage(String playerJoining){

        Player player = game.getPlayer(playerJoining).get();
        int playerIndex = game.getPlayerIndex(player);

        Map<Integer, List<SimpleModelElement>> playersElems = IntStream.range(0, maxPlayers).boxed().collect(Collectors.toMap(
                index -> index,
                index -> {
                    List<Element> elementsToUpdate = Element.getAllPlayerElementsAsList();
                    return Element.buildPlayerSimpleModelElements(game, elementsToUpdate, index);
                }
        ));

        List<SimpleModelElement> commonElements = Element.buildCommonSimpleModelElements(game, Element.getAllCommonElementsAsList(), playerIndex);


        ElementsInNetwork elementsInNetwork = new ElementsInNetwork(commonElements, playersElems);

        clientsStream().forEach(clientHandler -> clientHandler.sendAnswerMessage(elementsInNetwork));
    }

    public void updateElementsForAllPlayers(List<Element> elements){


        Map<Integer, List<SimpleModelElement>> playersElems = IntStream.range(0, maxPlayers).boxed().collect(Collectors.toMap(

                index -> index,
                index -> Element.buildPlayerSimpleModelElements(game, elements, index)
        ));


        clientsStream().forEach(clientHandler -> {

            Player player = game.getPlayer(clientHandler.getNickname()).get();
            List<SimpleModelElement> commonElements = Element.buildCommonSimpleModelElements(game, elements, game.getPlayerIndex(player));

            ElementsInNetwork elementsInNetwork = new ElementsInNetwork(commonElements, playersElems);

            clientHandler.sendAnswerMessage(elementsInNetwork);

        });

    }

    public String getReasonOfGameEnd(){
        return reasonOfGameEnd;
    }

    public void setReasonOfGameEnd(String reasonOfGameEnd){
        this.reasonOfGameEnd = reasonOfGameEnd;
    }

    private void setMatchPlayersOutcomes(){

       int highestScore = game.getMatchPlayers().values().stream().map(Player::getCurrentGamePoints).mapToInt(score -> score).max().orElse(0);
       game.getMatchPlayers()
               .keySet()
               .forEach(index -> {
                   Player player = game.getMatchPlayers().get(index);
                   boolean hasWon = player.getCurrentGamePoints() == highestScore;
                  player.setMatchOutcome(hasWon);

               });

    }

    public Date getCreatedTime(){
        return createdTime;
    }

    public boolean areAllPlayersOffline(){
        return onlineUsers.isEmpty();
    }

    public void notifyPlayerDisconnection(String playerNickname){

        List<Element> elements = new ArrayList<>();
        elements.add(Element.PLAYERS_INFO);

        if(getGameIfPresent().isPresent()) { // if game is present, notify players

            sendUpdatedMatchInfo();
            notifyStateToAllPlayers(elements, playerNickname);
            transitionToNextStateAfterDisconnection(playerNickname);
        }


    }

    private void sendUpdatedMatchInfo(){

        onlineUsers.values().forEach(client -> client.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(client))));
    }

    public void joinMatchAndNotifyStateIfPossible(String playerNickname){

        List<Element> elements = Element.getAsList();

        updateOtherPlayersCachesMessage(playerNickname);
        sendUpdatedMatchInfo();

        if(Objects.nonNull(game) && !game.isSinglePlayer() && game.getPlayer(playerNickname).isPresent()){

                Player player = game.getPlayer(playerNickname).get();
                if(!player.getCurrentState().equals(State.SETUP_PHASE)) { //if player disconnected not during/before setup

                    if(game.getOnlinePlayers().size()==1){
                        State state = player.anyLeaderPlayable() ? State.INITIAL_PHASE : State.MIDDLE_PHASE;
                        player.setCurrentState(state);
                    }
                    else
                        player.setCurrentState(State.IDLE); // if multiplayer, when player joins goes to IDLE phase, but only if he finished setup_phase

                }

                player.getPersonalBoard().removeSelectedResources(); // selected resources are deselected
                player.getPersonalBoard().resetAllSelectedProductionsAndChoices(); //selected productions and related selected productions are deselected
        }

        notifyStateToAllPlayers(elements, playerNickname);

    }

}
