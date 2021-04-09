package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.*;
import it.polimi.ingsw.server.model.player.board.Box;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameModel {

    private Player currentPlayer;
    private List<Player> players;
    private List<Player> onlinePlayers;
    private MarketBoard resourcesMarket;
    private State gamePhase;
    private Player winnerPlayer;
    private boolean isStarted;
    private final boolean isSinglePlayer;
    private Player singlePlayer;
    private SinglePlayerDeck soloDeck;
    private CardShop cardShop;

    /**
     * Constructor for a game of Maestri del Rinascimento.
     * @param nicknames a List of unique names of players.
     * @param isSinglePlayer Indicates if it is a single player game or not.
     */
    public GameModel(List<String> nicknames, boolean isSinglePlayer){

        this.isSinglePlayer = isSinglePlayer;
        commonInit(nicknames);

        if(isSinglePlayer) soloModeInit(nicknames.get(0));

        isStarted = false;
    }

    /**
     * Initializes common attributes between single player and multi player game.
     * @param nicknames a List of unique names of players.
     */
    private void commonInit(List<String> nicknames){
        try {
            resourcesMarket = MarketBoard.initializeMarketBoard("src/main/resources/config/MarketBoardConfig.json");
            cardShop = CardShop.initializeCardShop("src/main/resources/config/CardShopConfig.json");

        } catch (IOException e) {
            System.out.println("Error while class initialization with json config file");
            e.printStackTrace();
        }

        players = new ArrayList<>(nicknames.size());
        players = nicknames.stream().map(Player::new).collect(Collectors.toList());
        onlinePlayers = nicknames.stream().map(Player::new).collect(Collectors.toList());
       // cardShop = new CardShop();
    }

    /**
     * Initializes attributes for a single player game.
     * @param nickName the unique name of the player.
     */
    private void soloModeInit(String nickName){
        singlePlayer = new Player(nickName);
        soloDeck = new SinglePlayerDeck();
        currentPlayer = singlePlayer;
    }

    /**
     * Gets the player currently playing.
     * @return the current player.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Sets the player currently playing to the given {@link Player player}.
     * @param currentPlayer the player to set to current player.
     */
    public void setCurrentPlayer(Player currentPlayer) {this.currentPlayer = currentPlayer;}

    /**
     * Returns the {@link Player player} with the given nickname.
     * @param nickname the unique name of the player.
     * @return the {@link Player player} with the given nickname.
     */
    public Player getPlayer(String nickname) {
          return players.stream()
                .filter(player1 -> player1.getNickName().equals(nickname))
                .findAny().orElse(null);
    }

    private void updateOnlinePlayers() {
        onlinePlayers = players
                .stream()
                .filter(Player::isOnline)
                .collect(Collectors.toList());
    }

    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }

    /**
     * Returns the {@link State state} of the game of the current player.<br>
     * The {@link State state} of the game of each player can be different.
     * @return the {@link State state} of the game for the current player.
     */
    public State getGamePhase(){
        return gamePhase;
    }

    /**
     * Returns the {@link Player player} that will play in the next turn.
     * @return the next player.
     */
    public Player getNextPlayer() {

        int currentPlayerIndex = onlinePlayers.indexOf(currentPlayer);

        if(currentPlayerIndex == (onlinePlayers.size() -1 ) )
            return players.get(0);
        else
            return players.get(onlinePlayers.indexOf(currentPlayer) + 1);
    }

    /**
     * Makes the players different from the current player advance of one position in the faith track.
     */
    public void addFaithPointToOtherPlayers() {
        if(!isSinglePlayer)
        onlinePlayers.stream()
                .filter(player -> !(player == currentPlayer))
                .forEach(Player::moveOnePosition);
        else
            addFaithPointToLorenzo();
    }

    /**
     * In the single players game the player plays against Lorenzo il Magnifico.
     * This methods makes Lorenzo advance of one position in the {@link FaithTrack}.
     */
    private void addFaithPointToLorenzo(){
        singlePlayer.moveLorenzoOnePosition();
    }

    /**
     * Shuffles the deck of actions from which the opponent in the single player game, Lorenzo il Magnifico, has to choose from.
     */
    public void shuffleSoloDeck(){
        soloDeck.shuffleActionTokens();
    }

    /**
     * Returns the action that the opponent in the single player game, Lorenzo il Magnifico, will perform.
     * @return the action of Lorenzo il Magnifico.
     */
    public SoloActionToken showSoloActionToken(){
        return soloDeck.showToken();
    }

    /**
     * Performs the action that the opponent in the single player game, Lorenzo il Magnifico has chosen.
     */
    public void activateSoloActionToken(){
        soloDeck.activateToken(this);
    }

    /**
     * Removes the given number of {@link DevelopmentCard development cards}
     * of the given {@link DevelopmentCardColor type} from the {@link CardShop development card shop}.
     * @param card type of card to discard.
     * @param amount number of cards to discard.
     */
    public void discardCardFromShop(DevelopmentCardColor card, int amount){
        cardShop.discardCard(card, amount);
    }

    /**
     * Returns the {@link Player player} in a single player game.
     * @return the player.
     */
    public Player getSinglePlayer(){
        return singlePlayer;
    }

    /**
     * Puts a marble in the selected {@link MarketLine line} of the {@link MarketBoard}
     * to get the resources in that line.
     * @param line an enum indicating the row or column.
     */
    public void chooseLineFromMarketBoard(MarketLine line){
        resourcesMarket.chooseMarketLine(line);
    }

    /**
     * Updates the resources in the {@link MarketBoard} after the insertion of a marble.
     */
    public void updateMatrixAfterTakingResources(){
        resourcesMarket.updateMatrix();
    }

    public boolean areThereWhiteMarblesInPickedLine(){
        return resourcesMarket.areThereWhiteMarbles();
    }

    public int getNumberOfWhiteMarblesInPickedLine(){
        return resourcesMarket.getNumberOfWhiteMarbles();
    }

    /**
     * Convert the first white marble in the picked market board line to a resource
     * based on the {@link it.polimi.ingsw.server.model.player.leaders.MarketLeader leader} selected for that conversion.
     */
    public void convertWhiteMarbleInPickedLine(Resource mappedResource){
        resourcesMarket.convertWhiteMarble(mappedResource);
    }

    /**
     * Returns a {@link Box box} with the resources and faith points taken from the market board after choosing the line
     * and the eventual conversion for the white marbles.<br>
     * The resources and faith points position ordering is the same as their ordering in the {@link Resource resource enum}
     * @return a {@link Box} containing the resources and the faith points taken from the market.
     */
    public Box getBoxResourcesFromMarketBoard(){
        return resourcesMarket.getMappedResourcesBox();
    }

    /**
     * Flags if the {@link Player player} at the given position in the players list is online.
     * @param playerNumber a position in the players list.
     * @param currentlyOnline if the status of the {@link Player player} needs to be set to online.
     */
    public void setPlayerStatus(int playerNumber, boolean currentlyOnline){
        players.get(playerNumber).setCurrentStatus(currentlyOnline);

        if(currentlyOnline) {
            if (onlinePlayers.stream()
                    .noneMatch(player -> player == players.get(playerNumber)))
                onlinePlayers.add(players.get(playerNumber));
        }
        else{
            onlinePlayers = onlinePlayers.stream()
                    .filter(player -> player!= players.get(playerNumber))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Returns if the {@link Player player} at the given position in the players list is online.
     * @param playerNumber a position in the players list.
     * @return if the {@link Player player} at the given position is online.
     */
    public boolean isPlayerCurrentlyOnline(int playerNumber){
        return onlinePlayers.stream()
                .anyMatch(player -> player == players.get(playerNumber));
    }

    public int getPlayerPosition(Player player){
        return player.getPlayerPosition();
    }

    public int getLorenzoPosition(Player player){
        return player.getLorenzoPosition();
    }

}
