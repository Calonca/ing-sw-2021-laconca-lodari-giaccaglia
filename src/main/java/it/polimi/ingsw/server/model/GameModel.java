package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.market.*;
import it.polimi.ingsw.server.model.player.*;
import it.polimi.ingsw.server.model.player.board.*;
import it.polimi.ingsw.server.model.player.track.*;
import it.polimi.ingsw.server.model.solo.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

public class GameModel {


    /**
     * Player currently facing playing a game turn.
     */
    private Player currentPlayer;

    /**
     * List of current game registered players after lobby creation.
     */
    private Map<Integer, Player> players;

    /**
     * List of currently online players among ones in {@link GameModel#players} list.
     */
    private Map<Integer, Player> onlinePlayers;

    /**
     * This game market to store <em>MarketBoard</em> marbles for {@link State#SHOWING_MARKET_RESOURCES} <em>Game phase</em>
     */
    private MarketBoard resourcesMarket;

    /**
     * Ending game variable referencing the winner player, if present, determined after last played {@link State#FINAL_PHASE}
     */
    private Player winnerPlayer;

    /**
     * Boolean value indicating if current player has started or is still in setup phase.
     */
    private boolean isStarted;

    /**
     * Boolean value indicating if current game is either multiplayer or singleplayer
     */
    private final boolean isSinglePlayer;

    /**
     * Unique player facing <em>Lorenzo Il Magnifico</em> in <em>Solo Mode</em> game.
     * player is still contained in {@link GameModel#players} and {@link GameModel#onlinePlayers} lists as in multiplayer game.
     */
    private Player singlePlayer;

    /**
     * <em>Solo Mode</em> container to store {@link SoloActionToken SoloActionTokens} to reveal after each turn.
     */
    private SinglePlayerDeck soloDeck;


    /**
     * This game data structure to represent a {@link DevelopmentCard DevelopmentCards} grid structured according to
     * official game rules
     */
    private CardShop cardShop;


    /**
     * Constructor for a game of Maestri del Rinascimento.
     * @param nicknames a List of unique names of players.
     * @param isSinglePlayer Indicates if it is a single player game or not.
     */
    public GameModel(List<String> nicknames, boolean isSinglePlayer){

        this.isSinglePlayer = isSinglePlayer;
        commonInit(nicknames);

        if(isSinglePlayer) soloModeInit(players.get(0));

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

        players = IntStream.range(0, nicknames.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> new Player(nicknames.get(i))));

       // players = nicknames.stream().map(Player::new).collect(Collectors.toList());
        onlinePlayers = IntStream.range(0, nicknames.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> players.get(i)));

    }

    /**
     * Initializes attributes for a single player game.
     * @param singlePlayer the unique player.
     */
    private void soloModeInit(Player singlePlayer){
        this.singlePlayer = singlePlayer;
        soloDeck = new SinglePlayerDeck();
        currentPlayer = singlePlayer;
    }

    public void setGameStatus(boolean isStarted){
        this.isStarted = isStarted;
    }

    /**
     * Gets the player currently playing.
     * @return the current player.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }


    /**
     * @return the cardshop
     */
    public CardShop getCardShop() {
        return cardShop;
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
          return players.values().stream()
                  .filter(player1 -> player1.getNickName().equals(nickname))
                .findAny().orElse(null);
    }

    public void setOfflinePlayer(Player player){
        player.setCurrentStatus(false);
        updateOnlinePlayers();
        setCurrentPlayer(getNextPlayer());
    }

    public void setOnlinePlayer(Player player){

        int playerIndex = getPlayerIndex(player, players);
        setPlayerStatus(playerIndex, true);

    }

    private int getPlayerIndex(Player player, Map<Integer, Player> playersMap){

        return playersMap.keySet().stream()
                .filter(index -> playersMap.get(index).equals(player))
                .findFirst().orElse(0);
    }

    /**
     * Updates this {@link GameModel#onlinePlayers} list with currently online players, by checking
     * {@link GameModel#players players} connection status variable.
     */
    private void updateOnlinePlayers() {
        onlinePlayers = onlinePlayers.keySet().stream().filter(key -> onlinePlayers.get(key).isOnline())
                .collect(Collectors.toMap(Function.identity(), index -> onlinePlayers.get(index)));
    }

    //TODO CHECK IF IS REASONABLE TO RETURN PLAYER REFERENCE, MAYBE STRING NAME IS ENOUGH
    /**
     * @return a list of currently online {@link Player Players}
     */
    public Map<Integer, Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    public Map<Integer, Player> getOfflinePlayers(){
        return players.keySet().stream().filter(key -> !players.get(key).isOnline()).collect(Collectors.toMap(
                Function.identity(), index -> players.get(index)));
    }

    /* //TODO CHECK IF IS A USELESS METHOD
   /**
    * Returns the {@link State state} of the game of the current player.<br>
    * The {@link State state} of the game of each player can be different.
    * @return the {@link State state} of the game for the current player.

    public State getGamePhase(){
        return gamePhase;
    } */

    /**
     * Returns the {@link Player player} that will play in the next turn.
     * @return the next player.
     */
    public Player getNextPlayer() {

        int currentPlayerIndex = getPlayerIndex(currentPlayer, onlinePlayers);

        if(currentPlayerIndex == (players.size() -1 ) )
            return players.get(0);
        else
            return players.get(currentPlayerIndex + 1);

    }

    public void setCurrentPlayerState(State currentState) {
        currentPlayer.setCurrentState(currentState);
    }

    public boolean purchaseCardFromCardShop(DevelopmentCardColor color, int level){

        if(cardShop.isLevelOfColourOutOfStock(color, level))
            return false;

        cardShop.purchaseCard(color, level);
        return true;
    }

    public DevelopmentCard getPurchasedCard(){
        return cardShop.getPurchasedCard();
    }

    public boolean isCardColorOutOfStock(DevelopmentCardColor color){
        return cardShop.isColorOutOfStock(color);
    }

    public boolean isSomeDevCardColourOutOfStock(){
        return cardShop.isSomeColourOutOfStock();
    }

    public DevelopmentCardColor getDevCardColorOutOfStock(){
        return cardShop.getColourOutOfStock();
    }

    /**
     * Makes the players different from the current player advance of one position in the faith track.
     */
    public void addFaithPointToOtherPlayers() {
        if(!isSinglePlayer)
        onlinePlayers.values().stream()
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
    public Marble[] chooseLineFromMarketBoard(MarketLine line){
        return resourcesMarket.chooseMarketLine(line);
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
            if (onlinePlayers.values().stream()
                    .noneMatch(player -> player == players.get(playerNumber)))
                onlinePlayers.put(playerNumber,players.get(playerNumber));
        }
        else{

            onlinePlayers = onlinePlayers.keySet().stream()
                    .filter(index -> players.get(index).isOnline())
                    .collect(Collectors.toMap(Function.identity(), index -> players.get(index)));
        }

    }

    /**
     * Returns if the {@link Player player} at the given position in the players list is online.
     * @param playerNumber a position in the players list.
     * @return if the {@link Player player} at the given position is online.
     */
    public boolean isPlayerCurrentlyOnline(int playerNumber){
        return players.get(playerNumber).isOnline();
    }

    /**
     * @param player Current Game player, belonging to {@link GameModel#players} list.
     * @return parameter player position along {@link FaithTrack} as an int value.
     */
    public int getPlayerPosition(Player player){
        return player.getPlayerPosition();
    }

    /**
     * Solo mode method to get current {@link FaithTrack#lorenzoPiece lorenzoPiece} along {@link FaithTrack}.
     * @param player Current Game player, belonging to {@link GameModel#players} list.
     * @return {@link FaithTrack#lorenzoPiece lorenzoPiece} along {@link FaithTrack} as an int value.
     */
    public int getLorenzoPosition(Player player){
        return player.getLorenzoPosition();
    }

    public boolean isPlayerPieceInPopeSpace(Player player){
        return player.isInPopeSpace();
    }

    public boolean isLorenzoInPopeSpace(Player player){
        return player.isLorenzoInPopeSpace();
    }


}
