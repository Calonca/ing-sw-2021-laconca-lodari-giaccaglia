package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.LeaderActionBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Drawable;
import it.polimi.ingsw.client.view.abstractview.InitialPhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.beans.PropertyChangeEvent;

public class InitialPhase extends InitialPhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        SimplePlayerLeaders simplePlayerLeaders = getThisPlayerCache().getElem(SimplePlayerLeaders.class).orElseThrow();

        getCLIView().setTitle(new Title("Initial Phase"));
        getCLIView().setBody(new LeaderActionBody(simplePlayerLeaders.getPlayerLeaders(),getClient()));
        getCLIView().refreshCLI();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (false)
            getClient().changeViewBuilder(new InitialPhase());
        else{
            getCLIView().setTitle(new Title("Not found what looking for"));
            CanvasBody cb = new CanvasBody(CLI.width,getCLIView().getMaxBodyHeight());
            Drawable dwl = new Drawable();
            dwl.addToCenter(CLI.width,"Initial pahse received "+evt.getPropertyName());
            cb.getCanvas().addDrawableList(dwl);
            getCLIView().setBody(cb);
            getCLIView().refreshCLI();
        }
    }
}
