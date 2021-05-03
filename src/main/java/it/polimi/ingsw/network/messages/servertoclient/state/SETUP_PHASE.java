package it.polimi.ingsw.network.messages.servertoclient.state;

import java.util.UUID;

public class SETUP_PHASE extends StateMessage {
    protected Object[] leadersToChooseFrom;
    protected int numOfToChoose;
    private final UUID matchID;

    public SETUP_PHASE(int player, Object[] leadersToChooseFrom, int numOfToChoose, UUID matchID) {
        super(player);
        this.leadersToChooseFrom = leadersToChooseFrom;
        this.numOfToChoose = numOfToChoose;
        this.matchID = matchID;
    }

    public UUID getMatchID() {
        return matchID;
    }
}
