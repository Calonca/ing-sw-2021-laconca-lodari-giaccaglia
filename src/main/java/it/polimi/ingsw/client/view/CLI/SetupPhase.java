package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.LeadersBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;

import java.beans.PropertyChangeEvent;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        String title = "Select two cards.leaders and resources";
        getCLIView().setTitle(new Title(title));
        LeadersBody leadersBody = new LeadersBody(
                (SETUP_PHASE) getClient().currentPlayerCache().get().getDataFromState(SETUP_PHASE.class.getSimpleName()).get()
                );
        getCLIView().setBody(leadersBody);
        getCLIView().runOnInput("Insert anything and press send to test events",
                ()->{
                    SetupPhaseEvent event = new SetupPhaseEvent(0,0,0,0);
                    getClient().getServerHandler().sendCommandMessage(new EventMessage(event));
                });
        getCLIView().displayWithScroll();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
