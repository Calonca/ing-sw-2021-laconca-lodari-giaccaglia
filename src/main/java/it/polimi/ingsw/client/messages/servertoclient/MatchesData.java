package it.polimi.ingsw.client.messages.servertoclient;

import it.polimi.ingsw.client.ServerHandler;
import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatchView;
import it.polimi.ingsw.client.view.CLI.GenericWait;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class MatchesData extends it.polimi.ingsw.network.messages.servertoclient.MatchesData implements ClientMessage {

    public MatchesData(Pair<UUID, String[]>[] matchesData) {
        super(matchesData);
    }


    /**
     * Method invoked in the client to process the message.
     *
     * @param serverHandler
     */
    @Override
    public void processMessage(ServerHandler serverHandler) throws IOException {
        serverHandler.getClient().getCommonData().matchesData = Optional.ofNullable(matchesData);
        //Use observer pattern instead of this check
        if (serverHandler.getClient().getCurrentView().getClass().getName().equals(GenericWait.class.getName()))
            serverHandler.getClient().transitionToView(new CreateJoinLoadMatchView());
        else if (serverHandler.getClient().getCurrentView().getClass().getName().equals(CreateJoinLoadMatchView.class.getName()))
            serverHandler.getClient().getCurrentView().update();
    }
}
