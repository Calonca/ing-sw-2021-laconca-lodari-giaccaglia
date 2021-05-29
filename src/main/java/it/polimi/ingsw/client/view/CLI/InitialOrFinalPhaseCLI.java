package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.LeaderActionBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Drawable;
import it.polimi.ingsw.client.view.GUI.InitialOrFinalPhaseGUI;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.beans.PropertyChangeEvent;

public class InitialOrFinalPhaseCLI extends InitialOrFinalPhaseViewBuilder implements CLIBuilder {


    public InitialOrFinalPhaseCLI(boolean isInitial) {
        super(isInitial);
    }

    @Override
    public void run() {
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        String initial = "Initial";
        String end = "Final";
        getCLIView().setTitle(InitialOrFinalPhaseGUI.isInitial?initial:end+" phase");
        getCLIView().setBody(new LeaderActionBody(simplePlayerLeaders.getPlayerLeaders(),getClient()));
        getCLIView().refreshCLI();
    }

}
