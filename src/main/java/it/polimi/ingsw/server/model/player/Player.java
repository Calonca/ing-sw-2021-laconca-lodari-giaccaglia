package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.states.StatesTransitionTable;
import it.polimi.ingsw.server.utils.Deserializator;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Represents the Player entity inside the Model, needed for handling strictly related game phase events
 * through {@link GameModel} class. Encapsulates both multiplayer and solo mode game logic.
 */
public class Player {

    /**
     * Player's declared nickname at client side game setup
     */
    private final String nickName;

    /**
     * Player's {@link PersonalBoard} for handling personal storage of both {@link DevelopmentCard DevelopmentCards}
     * and {@link Resource Resources}.
     */
    private final PersonalBoard personalBoard;

    private final Map<UUID, Leader> leaders;

    /**
     * {@link Player} current {@link State} during game phases. When not playing, the default state is {@link State#IDLE IDLE}
     */
    private State currentState;

    private boolean wasInIDLE;

    /**
     * Boolean value used by {@link GameModel} to determine currently unavailable players, to handle game logic
     * with online players.
     */
    private boolean currentlyOnline;

    private boolean matchOutcome;

    /**
     * Player's personal {@link FaithTrack} along which {@link FaithTrack#playerPiece playerPiece} is moved and <em>Vatican Report</em>
     * requirements checks are done.
     */
    private FaithTrack faithTrack;

    private final boolean[] marketBonus;

    private StatesTransitionTable statesTransitionTable;


    /**
     * Class constructor
     */
    public Player(String nickName, Map<UUID, Leader> initialLeaders) {
        leaders = initialLeaders;
        personalBoard= new PersonalBoard();
        currentlyOnline = true;
        currentState = State.SETUP_PHASE;
        marketBonus=new boolean[4];
        this.nickName = nickName;
        initializeFaithTrack();
       // faithTrack.cheat();

    }

    public Optional<Leader> getLeader(UUID leaderId){
        return Optional.ofNullable(leaders.get(leaderId));
    }

    public List<Leader> getActiveLeaders(){
        return leaders.values().stream().filter(leader -> leader.getState().equals(LeaderState.ACTIVE)).collect(Collectors.toList());
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }


    public void setMatchOutcome(boolean matchOutcome){
        this.matchOutcome = matchOutcome;
    }

    public boolean getMatchOutCome(){
        return matchOutcome;
    }

    public List<UUID> getLeadersUUIDs() {return new ArrayList<>(leaders.keySet());}

    public void discardLeader(UUID leaderId){
        leaders.remove(leaderId);
    }

    public Optional<Leader> getLeaderToActivate(UUID leaderId){
        return Optional.ofNullable(leaders.get(leaderId));
    }

    /**
     * @param leaderUUID UUID value corresponding to a <em>leaderCard</em>
     * @return true if the <em>leaderCard</em> is available among ones of this player, otherwise false.
     */
    public boolean isLeaderAvailable(UUID leaderUUID){
        return getLeadersUUIDs().stream().anyMatch(availableUUID -> availableUUID.toString().equals(leaderUUID.toString()));
    }

    public boolean[] getMarketBonus() {
        return marketBonus;
    }

    public void setStatesTransitionTable(StatesTransitionTable statesTransitionTable){
        this.statesTransitionTable = statesTransitionTable;
    }

    public StatesTransitionTable getStatesTransitionTable() {
        return statesTransitionTable;
    }


    /**
     * Method to store inside player's {@link Player#marketBonus} array a new {@link Resource} for
     * {@link it.polimi.ingsw.server.model.market.Marble#WHITE WHITE MARBLE} conversion when choosing a row/column from
     * {@link it.polimi.ingsw.server.model.market.MarketBoard}.
     *
     * @param resource {@link Resource}
     */
    public void applyMarketBonus(Resource resource)
    {
        marketBonus[resource.getResourceNumber()]=true;
    }

    private void initializeFaithTrack(){
        faithTrack = Deserializator.faithTrackDeserialization();
        faithTrack.initTrackCells();
    }

    public String getSerializedFaithTrack(){
        return faithTrack.serializeFaithTrack();
    }
    /**
     * Method to set current player connection status, either online or offline.
     * @param currentlyOnline boolean value representing current player connection status.
     */
    public void setCurrentStatus(boolean currentlyOnline) {
        this.currentlyOnline = currentlyOnline;
    }

    /**
     * Method to set current player game phase {@link State}.
     * @param currentState Current Player's{@link State}.
     * */
    public void setCurrentState(State currentState) {

        wasInIDLE = this.currentState.equals(State.IDLE) || this.currentState.equals(State.SETUP_PHASE);

        this.currentState = currentState;

    }

    /**
     * Method to get current player connection status, either online or offline.
     * @return boolean value representing current player connection status.
     */
    public boolean isOnline(){
        return currentlyOnline;
    }

    /**
     * Method to get Player nickname , declared at client side game setup
     * @return Player nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Method to move {@link FaithTrack#playerPiece playerPiece} one position along {@link FaithTrack}
     */
    public void moveOnePosition(){
        faithTrack.moveOnePosition();
    }

    /**
     * Method to move {@link FaithTrack#playerPiece playerPiece} one position along {@link FaithTrack} during
     * <em>Solo mode</em> method.
     */
    public void moveLorenzoOnePosition(){
        faithTrack.moveLorenzoOnePosition();
    }

    /**
     * @return parameter this player position along {@link FaithTrack} as an int value.
     */
    public int getPlayerPosition(){
        return faithTrack.getPlayerPosition();
    }

    /**
     * Solo mode method to get current {@link FaithTrack#lorenzoPiece lorenzoPiece} along {@link FaithTrack}.
     * @return {@link FaithTrack#lorenzoPiece lorenzoPiece} along {@link FaithTrack} as an int value.
     */
    public int getLorenzoPosition(){
        return faithTrack.getLorenzoPosition();
    }

    /**
     * @return true if the <em>PlayerPiece</em> is for the first time in a  <em>Pope Space</em> along {@link FaithTrack}.
     */
    public boolean isInPopeSpaceForTheFirstTime(){
        return faithTrack.isPlayerInPopeSpaceForTheFirstTime();
    }

    /**
     * <em>Solo Mode</em> method to check if <em>LorenzoPiece</em> is in <em>PopeSpace</em>
     * @return true if the <em>LorenzoPiece</em> is currently in a <em>Pope Space</em> along {@link FaithTrack}.
     */
    public boolean isLorenzoInPopeSpace(){
        return faithTrack.isLorenzoInPopeSpaceForTheFirstTime();
    }

    public void turnPopeFavourTileInFaithTrack(int popeSpacePosition){
        faithTrack.turnPopeFavourTile(popeSpacePosition);
    }

    public boolean anyLeaderPlayable(){
        for (Leader leader : leaders.values())
            if (leader.getState()==LeaderState.INACTIVE)
                return true;
        return false;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean wasInIDLE(){
        return wasInIDLE;
    }

    /**
     * Used to verify if the player is required to make a choice
     * @return true if there is only one active Market Bonus
     */
    public boolean moreThanOneMarketBonus()
    {
        int trueCount = 0;
        for (boolean bonus : marketBonus) {
            if (bonus) {
                trueCount++;
            }
        }
        return trueCount>1;
    }

    /**
     * This method can be called only after getting the information that the user only has
     * one active Market Bonus
     * @return the corresponding resource position
     */
    public int getSingleMarketBonus()
    {
        int trueCount = 0;
        for (boolean bonus : marketBonus) {

            if (bonus) {
                return trueCount;
            }
            trueCount++;
        }
        return -1;
    }

    public boolean hasReachedTrackEnd(){
        return faithTrack.hasReachedLastSpace(faithTrack.getPlayerPiece());
    }

    public boolean hasLorenzoReachedTrackEnd(){
        return faithTrack.hasReachedLastSpace(faithTrack.getLorenzoPiece());
    }

    public Optional<Pair<Integer, Boolean>> getStateOfLastTurnedTileInTrack(){
        return faithTrack.getStateOfLastTurnedTile();
    }

    private int getPointsFromLeaders(){

        return leaders.values().stream().mapToInt(Leader::getLeaderPoints).sum();

    }

    public int getCurrentGamePoints(){

        return faithTrack.getPointsFromFaithTrackCell() +
                faithTrack.getPointsFromPopeFavourTiles() +
                personalBoard.getPointsFromResources() +
                personalBoard.getPointsFromDevelopmentCards() +
                getPointsFromLeaders();
    }

}
