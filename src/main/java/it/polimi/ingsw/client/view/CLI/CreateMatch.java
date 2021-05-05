package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.RunnableWithInputString;
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

        Runnable r1 = ()->{
            int numberOfPlayers = getCLIView().getLastInt();
            Runnable r2 = ()-> {
                String nickName = getCLIView().getLastInput();
                getCLIView().resetCLI();
                getCLIView().setSpinner(Spinner.matchToStart(getClient(),this));
                getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,nickName));
                getCLIView().displayWithDivider();
            };
            getCLIView().runOnInput("Your nickname: ",r2);
            getCLIView().displayWithDivider();
        };
        getCLIView().runOnIntInput("Number of people (1 to 4): ","Number of people not valid",1,4,r1);
        getCLIView().displayWithDivider();

    }

}
