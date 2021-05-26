package it.polimi.ingsw.client.view.CLI;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SetupBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.beans.PropertyChangeEvent;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        String title = "Select two leader cards and resources";
        getCLIView().setTitle(new Title(title));
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).get();
        String a = new GsonBuilder().setPrettyPrinting().create().toJson(simplePlayerLeaders);
        getCLIView().setBody(new SetupBody(a));
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
