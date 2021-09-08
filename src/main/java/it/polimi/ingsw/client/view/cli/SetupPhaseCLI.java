package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.cli.CLIelem.Title;
import it.polimi.ingsw.client.view.cli.CLIelem.body.SetupBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.util.Util;

import java.util.Objects;

public class SetupPhaseCLI extends SetupPhaseViewBuilder implements CLIBuilder {


    /**
     * This method shows the setupPhase and allows the user to select the initial leaders and bonuses if available
     */
    @Override
    public void run() {
        String title = "Select resources and the two leaders you want to keep";
        int resourcesToChoose = Util.resourcesToChooseOnSetup(CommonData.getThisPlayerIndex());
        getCLIView().setTitle(new Title(title));
        SimplePlayerLeaders simplePlayerLeaders = Objects.requireNonNull(getThisPlayerCache()).getElem(SimplePlayerLeaders.class).orElseThrow();
        getCLIView().setBody(new SetupBody(simplePlayerLeaders.getPlayerLeaders(),resourcesToChoose,getClient()));
        getCLIView().show();
    }

}
