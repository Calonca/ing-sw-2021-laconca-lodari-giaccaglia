package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.RunnableWithString;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.abstractview.CreateMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;

public class CreateMatch extends CreateMatchViewBuilder implements CLIBuilder {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        getCLIView().setTitle(new Title("Creating match"));
        getCLIView().displayWithDivider();

        RunnableWithString rs = new RunnableWithString();
        rs.afterInputCall(()->{
            int numberOfPlayers;
            do {
                String in = rs.getString();
                numberOfPlayers = Integer.parseInt(in);
            }while (numberOfPlayers <1|| numberOfPlayers >4);

            RunnableWithString rs2 = new RunnableWithString();
            int finalNumberOfPlayers = numberOfPlayers;
            rs2.afterInputCall(()->{
                String nickName = rs2.getString();
                getCLIView().resetCLI();
                getCLIView().setSpinner(Spinner.matchToStart(getClient(),this));
                getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(finalNumberOfPlayers,nickName));
                getCLIView().displayWithDivider();
            });
            getCLIView().getInputAndLaunchRunnable("Your nickname: ",rs2);
        });
        getCLIView().getInputAndLaunchRunnable("Number of people: ",rs);

    }

}
