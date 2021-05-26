package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.LeadersBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;

import java.beans.PropertyChangeEvent;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        String title = "Select two leader cards and resources";
        getCLIView().setTitle(new Title(title));

        //TODO CHECK COMMENTED OUT CODE HERE
     /*   LeadersBody leadersBody = new LeadersBody(
                (SETUP_PHASE) getClient().currentPlayerCache().get().getDataFromState(SETUP_PHASE.class.getSimpleName()).get()
                );
        getCLIView().setBody(leadersBody); */
        getCLIView().runOnInput("Insert anything and press send to test events",
                ()->{
                    SetupPhaseEvent event = new SetupPhaseEvent(0,0,0);
                    getClient().getServerHandler().sendCommandMessage(new EventMessage(event));
                });
        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
