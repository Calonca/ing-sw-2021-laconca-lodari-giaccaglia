package it.polimi.ingsw.client.view.CLI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SetupBody;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;
import it.polimi.ingsw.network.util.Util;

import java.beans.PropertyChangeEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;

public class SetupPhase extends SetupPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        String title = "Select resources and the two leaders you want to keep";
        int resourcesToChoose = Util.resourcesToChooseOnSetup(getCommonData().getThisPlayerIndex());
        getCLIView().setTitle(new Title(title));
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();
        getCLIView().setBody(new SetupBody(simplePlayerLeaders.getPlayerLeaders(),resourcesToChoose,getClient()));
        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(new IDLEViewBuilder());
        else{
            Gson builder = new GsonBuilder().registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter()).setPrettyPrinting().create();
            System.out.println("Setup received: "+evt.getPropertyName()+builder.toJson(evt.getNewValue()));
        }
    }
}
