package it.polimi.ingsw.server.model;

import com.rits.cloning.Cloner;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
import it.polimi.ingsw.server.model.solo.SinglePlayerDeck;
import it.polimi.ingsw.server.model.solo.SoloActionToken;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.states.StatesTransitionTable;
import it.polimi.ingsw.server.utils.Deserializator;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameModel {

    /**
     * Player currently facing playing a game turn.
     */
    private Player currentPlayer;

    private transient Match match;

    /**
     * List of current game registered players after lobby creation.
     */
    private Map<Integer, Player> players;

    /**
     * List of currently online players among ones in {@link GameModel#players} list.
     */
    private Map<Integer, Player> onlinePlayers;

    private List<Integer> vaticanReportTriggers = new ArrayList<>();

    /**
     * This game market to store <em>MarketBoard</em> marbles for {@link State#CHOOSING_MARKET_LINE} <em>Game phase</em>
     */
    private MarketBoard resourcesMarket;

    public enum MacroGamePhase
    {
        ActiveGame,
        LastTurn,
        GameEnded
    }

    private MacroGamePhase macroGamePhase;

    private Map<Integer, Player> playersEndingTheGame = new HashMap<>();

    /**
     * Boolean value indicating if current game is active, meaning at least one player is online
     */
    private boolean isActiveGame;

    /** Boolean value indicating if current game has started, meaning all players were removed from the lobby.
     */
    private boolean isStarted;

    /**
     * Boolean value indicating if current game is either multiplayer or singleplayer
     */
    private boolean isSinglePlayer = false;

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
     * official game rules.
     */
    private CardShop cardShop;

    /**
     * List containing Leader cards to distribute in {@link State#SETUP_PHASE}.
     */
    private Map<UUID, Leader> leaders;

    Map<UUID, DevelopmentCard> devCardsMap;

    /**
     *
     * @throws IOException
     */
    private void initializeLeadersList() throws IOException {
       leaders = Deserializator.leadersCardMapDeserialization();
    }

    /**
     * Constructor for a game of Maestri del Rinascimento.
     * @param nicknames a List of unique names of players.
     * @param isSinglePlayer Indicates if it is a single player game or not.
     */
    public GameModel(List<String> nicknames, boolean isSinglePlayer,Match match){
        this.match = match;
        this.isSinglePlayer = isSinglePlayer;
        commonInit(nicknames);

        if(isSinglePlayer) soloModeInit(players.get(0));

        isActiveGame = false;
    }

    public GameModel(){}

    /**
     * Initializes common attributes between single player and multi player game.
     * @param nicknames a List of unique names of players.
     */
    private void commonInit(List<String> nicknames){

        try {
            resourcesMarket = MarketBoard.initializeMarketBoard();
            cardShop = CardShop.initializeCardShop();
            initializeLeadersList();
            devCardsMap = Deserializator.devCardsMap();

        } catch (IOException e) {
            System.out.println("Error while class initialization from config file");
            e.printStackTrace();
        }

        players = IntStream.range(0, nicknames.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i ->
                        {
                            List<UUID> leaderKeys = new ArrayList<>(leaders.keySet());

                            List<UUID> keys = IntStream.range(0,4)
                                    .boxed()
                                    .map(index ->
                                            leaderKeys.remove(new Random().nextInt(leaderKeys.size())))
                                    .collect(Collectors.toList());

                            Map<UUID, Leader> initialLeaders = IntStream.range(0,4)
                                    .boxed()
                                    .collect(Collectors.toMap
                                            (keys::get, k -> leaders.remove(keys.get(k))));

                           return new Player(nicknames.get(i), initialLeaders);
                        }));

        onlinePlayers = IntStream.range(0, nicknames.size())
                .boxed()
                .collect(Collectors.toMap(Function.identity(), i -> players.get(i)));

        players.values().forEach((player)-> player.setStatesTransitionTable(StatesTransitionTable.fromIsSinglePlayer(isSinglePlayer))
        );

        setCurrentPlayer(players.get(0));
    }

    /**
     * Initializes attributes for a single player game.
     * @param singlePlayer the unique player.
     */
    private void soloModeInit(Player singlePlayer){
        this.singlePlayer = singlePlayer;
        soloDeck = new SinglePlayerDeck();
        currentPlayer = singlePlayer;
        singlePlayer.moveLorenzoOnePosition();
    }

    public void start(){
        this.isActiveGame = true;

        if(!isStarted)
            isStarted = true;

        macroGamePhase = MacroGamePhase.ActiveGame;
    }

    public void stop(){
        this.isActiveGame = false;
    }

    public void setMacroGamePhase(MacroGamePhase macroGamePhase){
        this.macroGamePhase = macroGamePhase;
    }

    public MacroGamePhase getMacroGamePhase(){
        return this.macroGamePhase;
    }

    public boolean isGameActive(){
        return isActiveGame;
    }

    public boolean isGameStarted(){
        return isStarted;
    }

    public boolean isSinglePlayer(){
        return isSinglePlayer;
    }

    /**
     * Gets the player currently playing.
     * @return the current player.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public boolean isDevCardInShopAvailable(int cardColorNumber, int cardLevel) {
        return !cardShop.isLevelOfColourOutOfStock(DevelopmentCardColor.fromInt(cardColorNumber), cardLevel);
    }

    public int getMaxDevCardLevelInCardShop(){
        return cardShop.getMaxCardLevel();
    }

    /**
     * @return the cardshop
     */
    public CardShop getCardShop() {
        return cardShop;
    }

    public Map<DevelopmentCardColor, Map<Integer, Pair<Integer,List<DevelopmentCard>>>> getSimpleCardShop(){
        return cardShop.getSimpleCardShop();
    }

    public Map<UUID, DevelopmentCard> getDevCardsMap(){
        return devCardsMap;
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
    public Optional<Player> getPlayer(String nickname) {
          return players.values().stream()
                  .filter(player1 -> player1.getNickname().equals(nickname))
                .findAny();
    }


    /**
     * Returns the {@link Player player} with the given index.
     * @param playerIndex the unique index of the player.
     * @return the {@link Player player} with the given nickname.
     */
    public Optional<Player> getPlayer(int playerIndex) {
        return Optional.ofNullable(players.get(playerIndex));
    }


    /**
     * Method to get a <em>playerNumber</em> associated with a Player.
     * @param player Player to determine the <em>playerNumber</em>
     * @return <em>playerNumber</em> associated with the Player if present, otherwise -1;
     */
    public int getPlayerIndex(Player player){
        return (Objects.nonNull(player) && players.containsValue(player)) ?
                getPlayerIndex(player, players)
                : -1;
    }

    public List<UUID> getRemainingLeadersUUIDs (){
        return new ArrayList<>(leaders.keySet());
    }

    public Leader getLeader(UUID leaderNumber){
        return leaders.remove(leaderNumber);
    }

    public Map<UUID, Leader> getLeaders () {
        Cloner c = new Cloner();
        return c.deepClone(leaders);
    }

    public boolean anyLeaderPlayableForCurrentPlayer(){
        return currentPlayer.anyLeaderPlayable();
    }

    public void discardLeadersOnSetupPhase(List<UUID> chosenLeaders){
        currentPlayer.getLeadersUUIDs().stream().filter(leaderId -> !chosenLeaders.contains(leaderId)).forEach(leaderId -> currentPlayer.discardLeader(leaderId));
    }

    public void setOfflinePlayer(Player player){
        player.setCurrentStatus(false);
        updateOnlinePlayers();

        if(onlinePlayers.isEmpty())
            isActiveGame = false;
    }

    public void setOnlinePlayer(Player player){
        player.setCurrentStatus(true);
        updateOnlinePlayers();

        if(!isActiveGame)
            isActiveGame = true;
    }

    private int getPlayerIndex(Player player, Map<Integer, Player> playersMap){
        return playersMap.keySet().stream()
                .filter(index -> playersMap.get(index).equals(player))
                .findFirst().orElse(-1);
    }

    /**
     * Updates this {@link GameModel#onlinePlayers} list with currently online players, by checking
     * {@link GameModel#players players} connection status variable.
     */
    private void updateOnlinePlayers() {

        onlinePlayers = players.keySet().stream().filter(key -> players.get(key).isOnline())
                .collect(Collectors.toMap(Function.identity(), index -> players.get(index)));

    }

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

    public Map<Integer, Player> getMatchPlayers(){
        return players;
    }


   /**
    * Returns the {@link State state} of the game of the current player.<br>
    * The {@link State state} of the game of each player can be different.
    * @return the {@link State state} of the game for the current player.
    */
    public State getGamePhase(){
        return currentPlayer.getCurrentState();
    }

    /**
     * Returns the {@link Player player} that will play in the next turn.
     * @return the next player.
     */
    public Optional<Player> getNextPlayer() {

        int currentPlayerIndex = getPlayerIndex(currentPlayer, onlinePlayers);

        //finds index of first online player
        Optional<Integer> optPlayerIndex = IntStream.concat(
                IntStream.range(currentPlayerIndex + 1, players.size()),
                IntStream.range(0,currentPlayerIndex + 1)).boxed().filter(index -> players.get(index).isOnline()).findFirst();

        if(optPlayerIndex.isEmpty())
            return Optional.empty();

        return Optional.of(players.get(optPlayerIndex.get()));

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

    public DevelopmentCard takePurchasedCardFromShop(){
        return cardShop.takePurchasedCard();
    }

    public boolean isCardColorOutOfStock(DevelopmentCardColor color){
        return cardShop.isColorOutOfStock(color);
    }

    public boolean isSomeDevCardColourOutOfStock(){
        return cardShop.isSomeColourOutOfStock();
    }

    public DevelopmentCardColor getDevCardColorOutOfStock(){
        return cardShop.getColorOutOfStock();
    }

    /**
     * Makes the players different from the current player advance of one position in the faith track, then checks if
     * any of them is in a Pope Space.
     *
     * @return true if any player is in Pope Space, otherwise false.
     */
    public boolean addFaithPointToOtherPlayers() {

        if(!isSinglePlayer) {
            return players.values().stream()
                    .filter(player -> !(player == currentPlayer))
                    .map(player -> {
                        player.moveOnePosition();
                        return player.isInPopeSpaceForTheFirstTime();
                    }).anyMatch(player -> true);
        }
        else
            singlePlayer.moveLorenzoOnePosition();
            return singlePlayer.isLorenzoInPopeSpaceForTheFirstTime();
        }

    /**
      * @return List of integers corresponding to the players triggering a VaticanReport. In case of <em>Solo mode</em>
     * list contains only one integer, corresponding to the singlePlayer, whose LorenzoPiece has triggered a VaticanReport
     */
    public void buildVaticanReportTriggersList(){

        if(isSinglePlayer){

            List<Integer> list = new ArrayList<>();
            if(singlePlayer.isLorenzoInPopeSpaceForTheFirstTime())
                list.add(-1);
            else if(singlePlayer.isInPopeSpaceForTheFirstTime())
                list.add(players.keySet().stream().findFirst().get());
            vaticanReportTriggers = list;
        }

        else {

            vaticanReportTriggers = players.values()
                    .stream()
                    .filter(Player::isInPopeSpaceForTheFirstTime)
                    .mapToInt(

                            player -> players.keySet()
                                    .stream()
                                    .filter(key -> players.get(key)
                                            .equals(player)).findFirst().get())
                    .boxed()
                    .collect(Collectors.toList());
        }
    }

    public void clearPlayersTriggeringVaticanReportList(){
        vaticanReportTriggers.clear();
    }


    /**
     * Method to execute a Vatican Report, when one or more players reaches a PopeSpace, both for <em>MultiPlayer</em>
     * and <em>Solo mode</em> game.
     */
     public void executeVaticanReport(){

         int popeSpacePosition;
         if(isSinglePlayer) {

             if(singlePlayer.isInPopeSpaceForTheFirstTime())
                popeSpacePosition = singlePlayer.getPlayerPosition();
             else
                 popeSpacePosition = singlePlayer.getLorenzoPosition();

         }

         else {
             popeSpacePosition = players
                     .values()
                     .stream()
                     .mapToInt(Player::getPlayerPosition)
                     .max()
                     .orElse(-1);
         }

            players.values().forEach(player -> player.turnPopeFavourTileInFaithTrack(popeSpacePosition));
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
    public SoloActionToken getSoloActionTokenOnTop(){
        return soloDeck.showToken();
    }

    public SoloActionToken getLastActivatedSoloActionToken(){
        if(!isSinglePlayer)
            return SoloActionToken.EMPTY;
        return soloDeck.showLastActivatedToken();
    }

    /**
     * Performs the action that the opponent in the single player game, Lorenzo il Magnifico has chosen.
     */
    public void activateSoloActionToken(){
        soloDeck.activateToken(this);
    }

    /**
     * Removes the given number of {@link DevelopmentCard development cards}
     * of the given {@link DevelopmentCardColor resourceType} from the {@link CardShop development card shop}.
     * @param card resourceType of card to discard.
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

    public int getMarketBoardRows(){
        return resourcesMarket.getRows();
    }

    public int getMarketBoardColumns(){
        return resourcesMarket.getColumns();
    }

    public Marble[][] getMarketMarbles(){
        return new Cloner().deepClone(resourcesMarket.getMarbleMatrix());
    }

    public Marble getSlideMarble(){
        return new Cloner().deepClone(resourcesMarket.getSlideMarble());
    }

    public Marble[] getPickedMarbles(){
        return resourcesMarket.getPickedMarbles();
    }

    public int getFaithPointsFromMarketPickedMarbles(){
        return resourcesMarket.getFaithPointsFromMarbles();
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
    public void convertWhiteMarbleInPickedLine(int mappedResource){
        resourcesMarket.convertWhiteMarble(mappedResource);
    }

    public void convertAllWhiteMarblesInPickedLine(int mappedResource){
       resourcesMarket.convertAllWhiteMarblesInPickedLine(mappedResource);
    }

    /**
     * Returns a {@link Box box} with the resources and faith points taken from the market board after choosing the line
     * and the eventual conversion for the white marbles.<br>
     * The resources and faith points position ordering is the same as their ordering in the {@link Resource resource enum}
     * @return a {@link Box} containing the resources and the faith points taken from the market.
     */
    public Box getBoxOfResourcesFromMarketBoard(){
        return resourcesMarket.getMappedResourcesBox();
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


    public void setMatch(Match match){
        this.match = match;
    }

    public Match getThisMatch(){
        return match;
    }

   public boolean handleVaticanReport(){

       vaticanReportTriggers.clear();;  //new vatican report -> new list

        buildVaticanReportTriggersList();

        if(!vaticanReportTriggers.isEmpty()) {
            executeVaticanReport();
            return true;
        }

        return false;

    }

    public List<Integer> getVaticanReportTriggers(){

        return vaticanReportTriggers;
    }

    public boolean checkTrackStatus(){  //returns true if player reaches track end

        Map<Integer, Player> playersAtTheEnd = getPlayersAtTheEndOfTheFaithTrack();

        if(!isSinglePlayer) {

            if (!playersAtTheEnd.isEmpty()) {
                setPlayersEndingTheGame(playersAtTheEnd);
                return true;

            }

        }

        else{

            if (!playersAtTheEnd.isEmpty() ||  (currentPlayer.hasLorenzoReachedTrackEnd())) {
                setPlayersEndingTheGame(playersAtTheEnd);
                return true;
            }
        }

        return false;

    }

    public void setPlayersEndingTheGame(Map<Integer, Player> playersEndingTheGame){
        this.playersEndingTheGame = playersEndingTheGame;

    }

    public Map<Integer, Player> getPlayersEndingTheGame(){
        return playersEndingTheGame;
    }

    public Map<Integer, Player> getPlayersAtTheEndOfTheFaithTrack(){

        return players.keySet().stream().filter(playerIndex -> players.get(playerIndex).hasReachedTrackEnd())
                .collect(Collectors.toMap(playerIndex -> playerIndex,
                        playerIndex -> players.get(playerIndex)));

    }


}
