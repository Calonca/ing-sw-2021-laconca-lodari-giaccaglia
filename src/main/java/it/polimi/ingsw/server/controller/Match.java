package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.network.messages.clienttoserver.events.Event;
import it.polimi.ingsw.network.messages.servertoclient.state.ElementsInNetwork;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
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
import java.util.stream.IntStream;
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

        if(Objects.nonNull(game))
            game.setMatch(this);

        offlineUsers.forEach(user ->
        {
            if(Objects.nonNull(game) && game.getPlayer(user.getNickname()).isPresent()) {
                Player player = game.getPlayer(user.getNickname()).get();
                game.setOfflinePlayer(player);
            }
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

           if(Objects.nonNull(game)) {  // game was created/existed before disconnection
               Player player = game.getPlayer(user.getNickname()).get();
               game.setOfflinePlayer(player);
           }
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
            if(game.getCurrentPlayer().getNickname().equals(player.getNickname()))
                game.setCurrentPlayer(player);

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
        this.game = new GameModel(onlineUsers.stream().map(ClientHandler::getNickname).collect(Collectors.toList()), onlineUsers.size()==1,this);
        game.start();
        game.getCurrentPlayer().setCurrentState(State.SETUP_PHASE);
        List<Element> elements = new ArrayList<>(Arrays.asList(Element.values()));
        notifyStateToAllPlayers(elements, game.getCurrentPlayer().getNickname());
    }

    public boolean isGameActive(){
        return Objects.nonNull(game) && game.isGameActive();
    }

    public Optional<GameModel> getGame(){
        return Optional.ofNullable(game);
    }

    public void stopGameIfPresent(){
        if(Objects.nonNull(game))
            game.stop();
    }

    private Optional<ClientHandler> currentUser(){

        String nickname = game.getCurrentPlayer().getNickname();
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

        if(onlineUsers.isEmpty())
            return Stream.generate(() -> "available slot").limit(maxPlayers - offlineUsers.size()).toArray(String[]::new);

        return Stream.concat(onlineUsers.stream().map(ClientHandler::getNickname),
                Stream.generate(()-> "available slot")).limit(maxPlayers - offlineUsers.size()).toArray(String[]::new);

    }

    public String[] getOfflinePlayers(){

        return offlineUsers.stream().map(ClientHandler::getNickname).toArray(String[]::new);

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

        String nicknameOfPlayerSendingEvent = ((Event)event).getPlayerNickname();

        Optional<Player> optionalPlayer =game.getPlayer(nicknameOfPlayerSendingEvent);

        if(optionalPlayer.isEmpty())
            throw new EventValidationFailedException();

        Player playerSendingEvent = optionalPlayer.get();
        GameStrategy gameStrategy =  playerSendingEvent.getStatesTransitionTable().getStrategy(playerSendingEvent.getCurrentState(), event);

        if (Objects.isNull(gameStrategy))
            throw new EventValidationFailedException();

        Pair<State, List<Element>> data = gameStrategy.execute(game, event);
        data.getValue().add(Element.PlayersInfo);

      if(data.getValue().contains(Element.SimpleFaithTrack)) {
            List<Element> elements = new ArrayList<>();
            elements.add(Element.SimpleFaithTrack);
            updateElementsForAllPlayers(elements);
        }

        playerSendingEvent.setCurrentState(data.getKey());
        notifyStateToAllPlayers(data.getValue(), nicknameOfPlayerSendingEvent);


        if ((playerSendingEvent.getCurrentState().equals(State.IDLE)
                        && playerSendingEvent.equals(game.getCurrentPlayer()))){

            if(game.getCurrentPlayer().equals(playerSendingEvent)) {

                game.setCurrentPlayer(game.getNextPlayer().get());

                if (game.getCurrentPlayer().getCurrentState().equals(State.SETUP_PHASE)) {

                    List<Element> elems = new ArrayList<>(Arrays.asList(Element.values()));
                    playerSendingEvent = game.getCurrentPlayer();
                    notifyStateToAllPlayers(elems, playerSendingEvent.getNickname());

                } else {

                    playerSendingEvent = game.getCurrentPlayer();
                    IDLE IDLEStrategy = new IDLE();
                    Pair<State, List<Element>> data2 = IDLEStrategy.execute(game, null);
                    playerSendingEvent.setCurrentState(data2.getKey());


                    if(playerSendingEvent.getCurrentState().equals(State.END_PHASE) && game.getMacroGamePhase().equals(GameModel.MacroGamePhase.GameEnded) && !game.isSinglePlayer()){

                        setMatchPlayersOutcomes();
                        notifyStateToAllPlayers(data2.getValue(), playerSendingEvent.getNickname());

                        for(Player player : game.getMatchPlayers().values()){

                            if(!player.equals(playerSendingEvent)){
                                List<Element> elementsToUpdate = new ArrayList<>();
                                elementsToUpdate.add(Element.EndGameInfo);
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

    }

    public void transitionToNextStateAfterDisconnection(String playerNickname){
        if(game.isSinglePlayer())
            return;

        Player playerDisconnected = game.getPlayer(playerNickname).get();
        if(game.getCurrentPlayer().equals(playerDisconnected)){

            if(game.getNextPlayer().isPresent()) {
                game.setCurrentPlayer(game.getNextPlayer().get());

                Player newCurrentPlayer = game.getCurrentPlayer();

                if (newCurrentPlayer.getCurrentState().equals(State.IDLE)) {

                    IDLE IDLEStrategy = new IDLE();
                    Pair<State, List<Element>> data = IDLEStrategy.execute(game, null);
                    newCurrentPlayer.setCurrentState(data.getKey());
                    notifyStateToAllPlayers(data.getValue(), newCurrentPlayer.getNickname());
                }
            }

        }


    }

    public void notifyStateToAllPlayers(List<Element> elems, String playerNotifying){

        Player player = game.getPlayer(playerNotifying).get();
        State state = player.getCurrentState();

        clientsStream().forEach(clientHandler -> {

            StateInNetwork stateInNetwork = state.toStateMessage(game, elems, game.getPlayerIndex(player));

            try {
                clientHandler.sendAnswerMessage(stateInNetwork);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

        clientsStream().forEach(clientHandler -> {
        try{
            clientHandler.sendAnswerMessage(elementsInNetwork);
        } catch (IOException e) {
            e.printStackTrace();
        }

    });
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

            try{
                clientHandler.sendAnswerMessage(elementsInNetwork);
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

}
