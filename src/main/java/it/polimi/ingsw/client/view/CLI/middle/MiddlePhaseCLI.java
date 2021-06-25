package it.polimi.ingsw.client.view.CLI.middle;


import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.CardShopCLI;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIActionToken;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.IDLE;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        if(getSimpleModel().isSinglePlayer()) {
            getClient().saveViewBuilder(this);
            CLIActionToken.showActionTokenBeforeTransition();
        }

        showVaticanReportInfoBeforeTransition();
        getCLIView().setTitle("Select a choice");

        Row row = new Row();

        SimpleProductions simpleProductions = getThisPlayerCache().getElem(SimpleProductions.class).orElseThrow();
        Drawable productionDw = new Drawable();
        productionDw.add(0,"Make a production");
        productionDw.add(0,"on your personal board");
        row.addElem(middlePhaseOption(productionDw,()-> sendMessage(Choice.PRODUCTION), simpleProductions.isAnyProductionAvailable()));
        row.addElem(new SizedBox(4,0));

        Drawable resourceMk = new Drawable();
        resourceMk.add(0,"Take resources from");
        resourceMk.add(0,"the resource market");
        row.addElem(middlePhaseOption(resourceMk,()-> sendMessage(Choice.RESOURCE_MARKET), true));
        row.addElem(new SizedBox(4,0));

        Drawable viewCardShop = new Drawable();
        viewCardShop.add(0,"Look at cards");
        viewCardShop.add(0,"from the Card Shop");
        row.addElem(middlePhaseOption(viewCardShop,()-> getClient().changeViewBuilder(new CardShopCLI(true)), true));
        row.addElem(new SizedBox(4,0));

        Drawable viewPersonalBoard = new Drawable();
        viewPersonalBoard.add(0," Look at your");
        viewPersonalBoard.add(0,"Personal Board");
        row.addElem(middlePhaseOption(viewPersonalBoard, () -> getClient().changeViewBuilder(new MiddlePersonalBoardCLI(getCommonData().getThisPlayerIndex(), false, PersonalBoardBody.ViewMode.MIDDLE)), true));
        row.addElem(new SizedBox(4,0));

        Drawable moveResourcesDw = new Drawable();
        moveResourcesDw.add(0,"Move resources");
        moveResourcesDw.add(0," in Warehouse");
        row.addElem(middlePhaseOption(moveResourcesDw,() -> getClient().changeViewBuilder(new MiddlePersonalBoardCLI(-1, true)) , true));
        row.addElem(new SizedBox(6,0));

        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        Drawable buyCard = new Drawable();
        buyCard.add(0,"Buy a card");
        buyCard.add(0,"from the Card Shop");
        row.addElem(middlePhaseOption(buyCard,()-> sendMessage(Choice.CARD_SHOP), simpleCardShop.getIsAnyCardPurchasable()));
        row.addElem(new SizedBox(4,0));

        Drawable playersInfoDw = new Drawable();
        playersInfoDw.add(0,"See players");
        playersInfoDw.add(0,"   info");
        row.addElem(middlePhaseOption(playersInfoDw, () -> getClient().changeViewBuilder(new MiddlePlayersInfoCLI()), true));
        row.addElem(new SizedBox(6,0));

        row.selectInEnabledOption(getCLIView(), "Select a middle phase option");

        getCLIView().setBody(CanvasBody.centered(row));
        getCLIView().show();
    }

    private Option middlePhaseOption(Drawable d, Runnable r, boolean enabled){
        Option o = Option.from(d,r);
        o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
        o.setEnabled(enabled);
        return o;
    }

    protected void showVaticanReportInfoBeforeTransition(){

        VaticanReportInfo reportInfo = getSimpleModel().getElem(VaticanReportInfo.class).get();

        if(reportInfo.hasReportOccurred() && !reportInfo.hasReportBeenShown()){
            reportInfo.reportWillBeShown();
            if(getClient().isCLI()) {
                getClient().saveViewBuilder(this);
                getClient().changeViewBuilder(new MiddleReportInfoCLI());
            }

        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if(IDLE.name().equals(propertyName) || evt.getPropertyName().equals(PlayersInfo.class.getSimpleName())){
            run();
        }

        else super.propertyChange(evt);
    }

}