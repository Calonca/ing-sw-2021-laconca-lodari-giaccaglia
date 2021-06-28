package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.LeaderBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIActionToken;
import it.polimi.ingsw.client.view.CLI.idle.ReportInfoCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;

import java.util.Objects;
import java.util.Optional;


public class InitialOrFinalPhaseCLI extends InitialOrFinalPhaseViewBuilder implements CLIBuilder {

    public InitialOrFinalPhaseCLI(boolean isInitial) {
        super(isInitial);
    }

    @Override
    public void run() {

        showWarningIfLastTurn();
        if(getSimpleModel().isSinglePlayer() && isInitial && !isFirstTurn()) {
            getClient().saveViewBuilder(this);
            CLIActionToken.showActionTokenBeforeTransition();
        }

        showVaticanReportInfoBeforeTransition();
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        String initial = "Initial";
        String end = "Final";
        getCLIView().clearScreen(); //needed to clear bottom text from idle phase
        getCLIView().setTitle((InitialOrFinalPhaseCLI.isInitial?initial:end)+" phase");
        getCLIView().setBody(new LeaderBody(simplePlayerLeaders.getPlayerLeaders(),getClient()));
        getCLIView().disableViewMode();
        getCLIView().show();

    }
    protected void showVaticanReportInfoBeforeTransition(){

        VaticanReportInfo reportInfo = getSimpleModel().getElem(VaticanReportInfo.class).get();
        if(reportInfo.hasReportOccurred() && !reportInfo.hasReportBeenShown()){
            reportInfo.reportWillBeShown();
            if(getClient().isCLI()) {
                getClient().saveViewBuilder(this);
                getClient().changeViewBuilder(new ReportInfoCLI());
            }

        }
    }

    protected void showWarningIfLastTurn() {
        Optional<EndGameInfo> endGameInfoOptional = getSimpleModel().getElem(EndGameInfo.class);
        if (endGameInfoOptional.isPresent() && Objects.nonNull(endGameInfoOptional.get().getCauseOfEndStringWithNames())) {
            String endGameReason = (getSimpleModel().getElem(EndGameInfo.class).orElseThrow()).getCauseOfEndStringWithNames();
            getCLIView().setSubTitle("Last turn! " + endGameReason, Color.BRIGHT_PURPLE, 1);
        }
    }

}
