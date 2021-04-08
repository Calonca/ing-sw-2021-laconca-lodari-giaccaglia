package it.polimi.ingsw.server.model.player;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.board.*;
import javafx.util.Pair;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;


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

    //public List<Leader> leaders;

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
     * Keeps track of currently active {@link DevelopmentDiscountLeader related resources discounts;.
     */
    private int[] discounts;
    private boolean[] marketBonus;

    /**
     * Class constructor
     * @throws IOException Exception thrown when {@link Player#initializeFaithTrack()} fails to
     * read class configuration from json file.
     */
    public Player(String nickName) {
     //   leaders = Arrays.asList(new Pair<Leader, LeaderState>[2]);
        personalBoard= new PersonalBoard();
        currentlyOnline = true;
        currentState = State.IDLE;
        discounts=new int[7];
        marketBonus=new boolean[5];
        this.nickName = nickName;
        initializeFaithTrack();
    }


    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    public int[] getDiscounts()
    {
        return discounts;
    }

    public boolean[] getMarketBonus() {
        return marketBonus;
    }
    /**
     * Applies Discount Leader effect on discount array
     * @param discount != NULL
     * Used by leader interface
     */
    public void applyDiscount(Pair<Resource,Integer> discount)
    {
        discounts[discount.getKey().getResourceNumber()]+=discount.getValue();
    }

    public void applyMarketBonus(Resource resource)
    {
        marketBonus[resource.getResourceNumber()]=true;
    }


    private void initializeFaithTrack() {
        File file = new File("src/main/resources/config/FaithTrackConfig.json");

        String FaithTrackClassConfig = null;
        try {
            FaithTrackClassConfig = Files.readString(Path.of(file.getAbsolutePath()), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        faithTrack = gson.fromJson(FaithTrackClassConfig, FaithTrack.class);
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

    public void moveLorenzoOnePosition(){
        faithTrack.moveLorenzoOnePosition();
    }

}
