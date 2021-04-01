package it.polimi.ingsw.server.model.player;

import com.google.gson.Gson;
import it.polimi.ingsw.server.model.Leader;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Player {

    private String nickName;
    PersonalBoard personalBoard;
    public final List<Leader> leaders;
    private State currentState;
    private boolean currentlyOnline;
    private FaithTrack faithTrack;
    private SinglePlayerDeck deck;
    private int[] discounts;

    public Player() throws IOException {
     //   leaders = Arrays.asList(new Pair<Leader, LeaderState>[2]);
        currentlyOnline = true;
        currentState = State.IDLE;
        initializeFaithTrack();
    }

    public int[] getDiscounts()
    {
        return discounts;
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

    private void initializeFaithTrack() throws IOException {
        String FaithTrackClassConfig = Files.readString(Path.of("FaithTrackConfig.json"), StandardCharsets.US_ASCII);
        Gson gson = new Gson();
        faithTrack = gson.fromJson(FaithTrackClassConfig, FaithTrack.class);
    }

    public void setCurrentStatus(boolean currentlyOnline) {
        this.currentlyOnline = currentlyOnline;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public boolean isOnline(){
        return currentlyOnline;
    }

    public boolean areLeaderActionsAvailable(){
        return false;
    }

    public String getNickName() {
        return nickName;
    }

    public void moveOnePosition(){
        faithTrack.moveOnePosition();
    }

    public void moveLorenzoOnePosition(){
        faithTrack.moveLorenzoOnePosition();
    }

}
