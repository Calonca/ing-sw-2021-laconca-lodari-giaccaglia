package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.HorizontalListBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Drawable;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;

import java.beans.PropertyChangeEvent;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle("Select a choice");
        HorizontalListBody horizontalListBody = new HorizontalListBody(getCLIView().getMaxBodyHeight());

        Drawable productionDw = new Drawable();
        productionDw.add(0,"Make a production");
        productionDw.add(0,"on your personal board");
        horizontalListBody.addOption(Option.from(productionDw,()-> MiddlePhaseViewBuilder.sendMessage(Choice.PRODUCTION)));

        Drawable resourceMk = new Drawable();
        resourceMk.add(0,"Take resources from");
        resourceMk.add(0,"the resource market");
        horizontalListBody.addOption(Option.from(resourceMk,()-> MiddlePhaseViewBuilder.sendMessage(Choice.RESOURCE_MARKET)));

        Drawable cardShop = new Drawable();
        cardShop.add(0,"Buy a new card from");
        cardShop.add(0,"the card shop");
        horizontalListBody.addOption(Option.from(cardShop,()-> MiddlePhaseViewBuilder.sendMessage(Choice.CARD_SHOP)));

        getCLIView().setBody(horizontalListBody);
        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
