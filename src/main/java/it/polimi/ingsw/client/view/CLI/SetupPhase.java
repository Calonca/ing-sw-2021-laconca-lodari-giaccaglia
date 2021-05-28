package it.polimi.ingsw.client.view.CLI;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SetupBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.util.Util;

import java.beans.PropertyChangeEvent;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        String title = "Select two leader cards and resources";
        int resourcesToChoose = Util.resourcesToChooseOnSetup(getCommonData().getThisPlayerIndex().orElse(0));
        getCLIView().setTitle(new Title(title));
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        //String a = new GsonBuilder().setPrettyPrinting().create().toJson(simplePlayerLeaders);
        getCLIView().setBody(new SetupBody(simplePlayerLeaders.getPlayerLeaders(),resourcesToChoose,getClient()));

        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("SEtup received"+evt.getPropertyName());
    }
}
