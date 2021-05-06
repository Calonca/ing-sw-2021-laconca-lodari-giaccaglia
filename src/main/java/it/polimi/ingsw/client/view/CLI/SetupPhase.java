package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SetupBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        String title = "Select two leaders and resources";
        getCLIView().setTitle(new Title(title));
        SetupBody leadersBody = new SetupBody(
                (SETUP_PHASE) getClient().currentPlayerCache().get().getDataFromState(SETUP_PHASE.class.getSimpleName()).get()
                );
        getCLIView().setBody(leadersBody);
        getCLIView().displayWithScroll();
    }

}
