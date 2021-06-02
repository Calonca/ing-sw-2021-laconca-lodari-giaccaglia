package it.polimi.ingsw.client.view.CLI;


import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.Row;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;

import java.util.Optional;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().setTitle("Select a choice");
        Row row = new Row();

        Drawable productionDw = new Drawable();
        productionDw.add(0,"Make a production");
        productionDw.add(0,"on your personal board ");
        row.addElem(middlePhaseOption(productionDw,()-> sendMessage(Choice.PRODUCTION)));

        Drawable resourceMk = new Drawable();
        resourceMk.add(0,"Take resources from ");
        resourceMk.add(0,"the resource market");
        row.addElem(middlePhaseOption(resourceMk,()-> sendMessage(Choice.RESOURCE_MARKET)));

        Drawable cardShop = new Drawable();
        cardShop.add(0,"Buy a new card from ");
        cardShop.add(0,"the card shop");
        row.addElem(middlePhaseOption(cardShop,()-> sendMessage(Choice.CARD_SHOP)));
        row.selectAndRunOption(getCLIView());

        getCLIView().setBody(CanvasBody.centered(row));
        getCLIView().show();
    }

    private Option middlePhaseOption(Drawable d,Runnable r){
           Option o = Option.from(d,r);
           o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
           return o;
    }


}
