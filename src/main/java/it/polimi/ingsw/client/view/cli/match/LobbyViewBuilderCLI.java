package it.polimi.ingsw.client.view.cli.match;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.cli.CLIelem.Title;
import it.polimi.ingsw.client.view.cli.SetupPhaseCLI;
import it.polimi.ingsw.client.view.cli.layout.drawables.LobbyCLI;
import it.polimi.ingsw.client.view.cli.layout.drawables.Timer;
import it.polimi.ingsw.client.view.abstractview.LobbyViewBuilder;

import java.beans.PropertyChangeEvent;

public class LobbyViewBuilderCLI extends LobbyViewBuilder implements CLIBuilder {


    /**
     * In this state the player can see all the match's information, while waiting for it to start
     */
    @Override
    public void run() {
        String text;
        if (getCommonData().getAvailableMatchesData().isPresent() && getCommonData().getMatchId().isPresent()) {
            getCLIView().setTitle(new Title("Joining match"));
            LobbyCLI.buildLobbyCLI();

            if (getCommonData().getCurrentPlayerIndex() == CommonData.getThisPlayerIndex()){
                if(getThisPlayerCache() != null && getThisPlayerCache().getCurrentState().equals("SETUP_PHASE"))
                    text = "Ready for Setup Phase, seconds left : ";
                else
                    text = "Ready for your turn, seconds left : ";

                Timer.showSecondsOnCLI(getCLIView(), text, 3);
            }

        } else {
            getCLIView().setTitle(new Title("Lobby Creation"));
            getCLIView().clearScreen();
            getCLIView().show();
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getPropertyName().equals(State.SETUP_PHASE.toString())) {
            getClient().changeViewBuilder(new SetupPhaseCLI());
        }

        else{
            run();
        }
    }

}
