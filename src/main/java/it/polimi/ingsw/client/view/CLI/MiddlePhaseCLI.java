package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.HorizontalListBody;
import it.polimi.ingsw.client.view.CLI.drawables.Drawable;
import it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.IDLE;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle("Select a choice");
        HorizontalListBody horizontalListBody = new HorizontalListBody(getCLIView().getMaxBodyHeight());

        Drawable productionDw = new Drawable();
        productionDw.add(0,"Make a production");
        productionDw.add(0,"on your personal board");
        horizontalListBody.addOption(Option.from(productionDw,()-> sendMessage(Choice.PRODUCTION)));

        Drawable resourceMk = new Drawable();
        resourceMk.add(0,"Take resources from");
        resourceMk.add(0,"the resource market");
        horizontalListBody.addOption(Option.from(resourceMk,()-> sendMessage(Choice.RESOURCE_MARKET)));

        Drawable cardShop = new Drawable();
        cardShop.add(0,"Buy a new card from");
        cardShop.add(0,"the card shop");
        horizontalListBody.addOption(Option.from(cardShop,()-> sendMessage(Choice.CARD_SHOP)));

        getCLIView().setBody(horizontalListBody);
        getCLIView().show();
    }


}
