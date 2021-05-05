package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.RunnableWithString;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;

import java.util.UUID;

public class JoinMatch extends it.polimi.ingsw.client.view.abstractview.JoinMatch implements CLIBuilder {


    public JoinMatch(UUID matchId) {
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
        getCLIView().resetCLI();
        getCLIView().setTitle(new Title("Joining match"));
        getCLIView().displayWithDivider();

        RunnableWithString rs = new RunnableWithString();
        rs.afterInputCall(()->{
            getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,rs.getString()));
            getCLIView().resetCLI();
            getCLIView().setSpinner(Spinner.matchToStart(getClient(),this));
            getCLIView().displayWithDivider();
        });
        getCLIView().getInAndCallRunnable("Your nickname: ",rs);



    }

}
