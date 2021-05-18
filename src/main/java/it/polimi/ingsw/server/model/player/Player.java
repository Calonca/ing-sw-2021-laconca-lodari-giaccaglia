package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.states.StatesTransitionTable;
import it.polimi.ingsw.server.utils.Deserializator;
import it.polimi.ingsw.server.model.states.State;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.leaders.DevelopmentDiscountLeader;
import it.polimi.ingsw.server.model.player.board.*;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.LeaderState;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
import javafx.util.Pair;

import java.util.*;


/**
 * Represents the Player entity inside the Model, needed for handling strictly related game phase events
 * through {@link GameModel} class. Encapsulates both multiplayer and solo mode game logic.
 */
public class Player {

    /**
     * Player's declared nickname at client side game setup
     */
    private String nickName;

    /**
     * Player's {@link PersonalBoard} for handling personal storage of both {@link DevelopmentCard DevelopmentCards}
     * and {@link Resource Resources}.
     */
    private final PersonalBoard personalBoard;


    private Map<UUID, Leader> leaders;

    /**
     * {@link Player} current {@link State} during game phases. When not playing, the default state is {@link State#IDLE IDLE}
     */
    private State currentState;

    /**
     * Boolean value used by {@link GameModel} to determine currently unavailable players, to handle game logic
     * with online players.
     */
    private boolean currentlyOnline;

    /**
     * Player's personal {@link FaithTrack} along which {@link FaithTrack#playerPiece playerPiece} is moved and <em>Vatican Report</em>
     * requirements checks are done.
     */
    private FaithTrack faithTrack;

    /**
     * Keeps track of currently active {@link DevelopmentDiscountLeader} related resources discounts.
     */
    private int[] discounts;

    private boolean[] marketBonus;
    private StatesTransitionTable statesTransitionTable;

    /**
     * Class constructor
     */
    public Player(String nickName, Map<UUID, Leader> initialLeaders) {
        leaders = initialLeaders;
        personalBoard= new PersonalBoard();
        currentlyOnline = true;
        currentState = State.IDLE;
        discounts=new int[4];
        marketBonus=new boolean[4];
        this.nickName = nickName;
        initializeFaithTrack();
    }

    public Optional<Leader> getLeader(UUID leaderId){
        return Optional.ofNullable(leaders.get(leaderId));
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    /**
     * Method to get currentlu active {@link DevelopmentCard} required resources discounts, stored inside player's
     * {@link Player#discounts} array as positional int values-
     * @return {@link Player#discounts} array storing updated values.
     */
    public int[] getDiscounts()
    {
        return discounts;
    }


    public List<Leader> getLeadersList() {
        return new ArrayList<>(leaders.values());
    }

    public List<UUID> getLeadersUUIDs() {return new ArrayList<>(leaders.keySet());}

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
     * Method to store inside player's {@link Player#discounts} array a new positional {@link Resource} value to discount
     * after the appropriate {@link Leader} <em>effect</em> has been activated.
     * @param discount {@link Pair} containing a Resource to discount when purchasing a {@link DevelopmentCard} from {@link CardShop} as a key
     * and an int as a value indicating the discount amount.
     * Used by leader interface
     */
    public void applyDiscount(Pair<Resource,Integer> discount)
    {
        discounts[discount.getKey().getResourceNumber()]+=discount.getValue();
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
        this.currentState = currentState;
    }

    /**
     * Method to get current player connection status, either online or offline.
     * @return boolean value representing current player connection status.
     */
    public boolean isOnline(){
        return currentlyOnline;
    }

    public boolean areLeaderActionsAvailable(){
        return false;
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
     * @return true if the <em>PlayerPiece</em> is currently in a <em>Pope Space</em> along {@link FaithTrack}.
     */
    public boolean isInPopeSpace(){
        return faithTrack.isPlayerInPopeSpace();
    }

    /**
     * <em>Solo Mode</em> method to check if <em>LorenzoPiece</em> is in <em>PopeSpace</em>
     * @return true if the <em>LorenzoPiece</em> is currently in a <em>Pope Space</em> along {@link FaithTrack}.
     */
    public boolean isLorenzoInPopeSpace(){
        return faithTrack.isLorenzoInPopeSpace();
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
        return 0;
    }

   /* public State getMacroState() {
        return macroState;
    }

    public void setMacroState(State macroState) {
        this.macroState = macroState;
    }*/
}
