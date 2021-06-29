package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.SetupPhase;
import it.polimi.ingsw.client.view.CLI.layout.drawables.LobbyCLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Timer;
import it.polimi.ingsw.client.view.abstractview.LobbyViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;

import java.beans.PropertyChangeEvent;

public class LobbyViewBuilderCLI extends LobbyViewBuilder implements CLIBuilder {


    /**
     * In this state the player can see all the match's information, while waiting for it to start
     */
    @Override
    public void run() {

        if (getCommonData().getAvailableMatchesData().isPresent() && getCommonData().getMatchId().isPresent()) {
            getCLIView().setTitle(new Title("Joining match"));
            LobbyCLI.buildLobbyCLI();
        } else {
            getCLIView().setTitle(new Title("Lobby Creation"));
            getCLIView().clearScreen();
            getCLIView().show();
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(CommonData.matchesDataString) || evt.getPropertyName().equals(CommonData.thisMatchData) |! evt.getPropertyName().equals(PlayersInfo.class.getSimpleName()))
            getClient().changeViewBuilder(LobbyViewBuilder.getBuilder(getClient().isCLI()));
        else if (evt.getPropertyName().equals(State.SETUP_PHASE.toString())) {
            Timer.showSecondsOnCLI(getCLIView(), "Ready for Setup Phase, seconds left : ");
            getClient().changeViewBuilder(new SetupPhase());

        } else if (evt.getPropertyName().equals(PlayersInfo.class.getSimpleName())) {
            run();
        }
    }

}
