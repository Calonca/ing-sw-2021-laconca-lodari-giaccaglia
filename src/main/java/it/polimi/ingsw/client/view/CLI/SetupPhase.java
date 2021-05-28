package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SetupBody;
import it.polimi.ingsw.client.view.CLI.textUtil.DrawableList;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
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
        getCLIView().setBody(new SetupBody(simplePlayerLeaders.getPlayerLeaders(),resourcesToChoose,getClient()));

        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(State.IDLE.name()))
            getClient().changeViewBuilder(new InitialPhase());
        else{
            getCLIView().setTitle(new Title("Not found what looking for"));
            CanvasBody cb = new CanvasBody(CLI.width,getCLIView().getMaxBodyHeight());
            DrawableList dwl = new DrawableList();
            dwl.addToCenter(CLI.width,"Setup received: "+evt.getPropertyName());
            cb.getCanvas().addDrawableList(dwl);
            getCLIView().setBody(cb);
            getCLIView().refreshCLI();
        }
    }
}
