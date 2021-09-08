package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.cli.CLIelem.body.LeaderBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLIActionToken;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;
import it.polimi.ingsw.client.view.cli.idle.IDLEReportInfoCLI;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.util.Objects;
import java.util.Optional;


public class InitialOrFinalPhaseCLI extends InitialOrFinalPhaseViewBuilder implements CLIBuilder {

    public InitialOrFinalPhaseCLI(boolean isInitial) {
        super(isInitial);
    }


    /**
     * This method disambiguates wether it is turn start or end to instantiate the correct game phase
     */
    @Override
    public void run() {

        getClient().saveViewBuilder(this);

        if(getSimpleModel().isSinglePlayer() && isInitialPhase() && isFirstTurn()) {
            CLIActionToken.showActionTokenBeforeTransition();
        }

        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new IDLEReportInfoCLI());

        showWarningIfLastTurn();


        SimplePlayerLeaders simplePlayerLeaders = Objects.requireNonNull(getThisPlayerCache()).getElem(SimplePlayerLeaders.class).orElseThrow();

        String initial = "Initial";
        String end = "Final";
        getCLIView().clearScreen(); //needed to clear bottom text from idle phase
        getCLIView().setTitle((isInitialPhase()?initial:end)+" phase");
        getCLIView().setBody(new LeaderBody(simplePlayerLeaders.getPlayerLeaders(),getClient()));
        getCLIView().disableViewMode();
        getCLIView().show();

    }

    protected void showWarningIfLastTurn() {
        Optional<EndGameInfo> endGameInfoOptional = getSimpleModel().getElem(EndGameInfo.class);
        if (endGameInfoOptional.isPresent() && Objects.nonNull(endGameInfoOptional.get().getCauseOfEndStringWithNames())) {
            String endGameReason = (getSimpleModel().getElem(EndGameInfo.class).orElseThrow()).getCauseOfEndStringWithNames();
            getCLIView().setSubTitle("Last turn! " + endGameReason, Color.BRIGHT_PURPLE);
        }
    }

}
