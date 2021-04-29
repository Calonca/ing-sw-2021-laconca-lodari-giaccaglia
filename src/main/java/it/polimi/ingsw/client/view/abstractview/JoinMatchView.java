package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIView;
import it.polimi.ingsw.server.messages.clienttoserver.JoinMatchRequest;

import java.util.Scanner;
import java.util.UUID;

public abstract class JoinMatchView extends View implements CLIView {

    protected UUID matchId;

    public JoinMatchView(UUID matchId) {
        this.matchId = matchId;
    }

    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {

    }
}
