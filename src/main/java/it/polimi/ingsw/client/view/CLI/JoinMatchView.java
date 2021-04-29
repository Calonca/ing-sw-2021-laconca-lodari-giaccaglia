package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;

import java.beans.PropertyChangeEvent;
import java.util.Scanner;
import java.util.UUID;

public class JoinMatchView extends it.polimi.ingsw.client.view.abstractview.JoinMatchView implements CLIView {


    public JoinMatchView(UUID matchId) {
        super(matchId);
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
        Scanner scanner = new Scanner(System.in);
        String nickname;
        CLIBuilder.scroll();
        System.out.println("Joining match\n");
        System.out.println("Your nickname: ");
        nickname = scanner.nextLine();
        getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,nickname));
        getClient().transitionToView(new WaitForMatchToStart(matchId));
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
