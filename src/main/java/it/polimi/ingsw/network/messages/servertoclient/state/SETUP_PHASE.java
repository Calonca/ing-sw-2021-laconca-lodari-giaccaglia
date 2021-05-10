package it.polimi.ingsw.network.messages.servertoclient.state;

import java.util.UUID;

public class SETUP_PHASE extends StateInNetwork {
    protected Object[] leadersToChooseFrom;
    protected int numOfToChoose;
    private final UUID matchID;
    private final String[] nickNames;

    public SETUP_PHASE(int player, Object[] leadersToChooseFrom, int numOfToChoose, UUID matchID, String[] nickNames) {
        super(player);
        this.leadersToChooseFrom = leadersToChooseFrom;
        this.numOfToChoose = numOfToChoose;
        this.matchID = matchID;
        this.nickNames = nickNames;
    }

    public UUID getMatchID() {
        return matchID;
    }

    public String[] getNickNames() {
        return nickNames;
    }
}
