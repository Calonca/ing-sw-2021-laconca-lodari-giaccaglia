package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.HorizontalListBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle("Select a choice");
        HorizontalList horizontalList = new HorizontalList(getCLIView().getMaxBodyHeight());

        Drawable productionDw = new Drawable();
        productionDw.add(0,"Make a production");
        productionDw.add(0,"on your personal board");
        horizontalList.addOption(Option.from(productionDw,()-> sendMessage(Choice.PRODUCTION)));

        Drawable resourceMk = new Drawable();
        resourceMk.add(0,"Take resources from");
        resourceMk.add(0,"the resource market");
        horizontalList.addOption(Option.from(resourceMk,()-> sendMessage(Choice.RESOURCE_MARKET)));

        Drawable cardShop = new Drawable();
        cardShop.add(0,"Buy a new card from");
        cardShop.add(0,"the card shop");
        horizontalList.addOption(Option.from(cardShop,()-> sendMessage(Choice.CARD_SHOP)));
        horizontalList.selectAndRunOption(getCLIView());

        getCLIView().setBody(new HorizontalListBody(horizontalList));
        getCLIView().show();
    }


}
