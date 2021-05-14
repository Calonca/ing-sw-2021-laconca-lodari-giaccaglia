package it.polimi.ingsw.network.messages.servertoclient.state;

import java.util.List;
import java.util.UUID;

public class SETUP_PHASE extends StateInNetwork {
    private final List<UUID> leadersToChooseFrom;
    private final int numOfResourcesToChoose;
    private final UUID matchID;
    private final String[] nickNames;

    public SETUP_PHASE(int player, List<UUID> leadersToChooseFrom, int numOfResourcesToChoose, UUID matchID, String[] nickNames) {
        super(player);
        this.leadersToChooseFrom = leadersToChooseFrom;
        this.numOfResourcesToChoose = numOfResourcesToChoose;
        this.matchID = matchID;
        this.nickNames = nickNames;
    }

    public UUID getMatchID() {
        return matchID;
    }

    public String[] getNickNames() {
        return nickNames;
    }

    public List<UUID> getLeadersToChooseFrom(){
        return leadersToChooseFrom;
    }
}
