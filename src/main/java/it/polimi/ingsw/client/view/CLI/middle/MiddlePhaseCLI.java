package it.polimi.ingsw.client.view.CLI.middle;


import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIActionToken;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIReportInfoBuilder;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.MIDDLE_PHASE;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {


    /**
     * The player is presented various options: production,market,personal board,move resources,
     * warehouse,card shop, players info
     */
    @Override
    public void run() {


        if(getSimpleModel().isSinglePlayer() && !isFirstTurn()) {
            getClient().saveViewBuilder(this);
            CLIActionToken.showActionTokenBeforeTransition();
        }
        showWarningIfLastTurn();

        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new MiddleReportInfoCLI());

        getCLIView().setTitle("Select a choice");

        Row row = getRowOfOptions();
        getCLIView().setBody(CanvasBody.centered(row));
        getCLIView().show();
    }

    private Row getRowOfOptions(){

        Row row = new Row();

        SimpleProductions simpleProductions = getThisPlayerCache().getElem(SimpleProductions.class).orElseThrow();
        Drawable productionDw = new Drawable();
        productionDw.add(0,"   Make a production");
        productionDw.add(0,"on your personal board");
        row.addElem(middlePhaseOption(productionDw,()-> sendMessage(Choice.PRODUCTION), simpleProductions.isAnyProductionAvailable()));
        row.addElem(new SizedBox(4,0));

        Drawable resourceMkViewing = new Drawable();
        resourceMkViewing.add(0,"      Look at");
        resourceMkViewing.add(0,"the resource market");
        row.addElem(middlePhaseOption(resourceMkViewing,()-> getClient().changeViewBuilder(new MiddleResourceMarketCLIViewing()), true));
        row.addElem(new SizedBox(4,0));

        Drawable resourceMk = new Drawable();
        resourceMk.add(0,"Take resources from");
        resourceMk.add(0,"the resource market");
        row.addElem(middlePhaseOption(resourceMk,()-> sendMessage(Choice.RESOURCE_MARKET), true));
        row.addElem(new SizedBox(4,0));

        Drawable viewCardShop = new Drawable();
        viewCardShop.add(0,"  Look at cards");
        viewCardShop.add(0,"from the Card Shop");
        row.addElem(middlePhaseOption(viewCardShop,()-> getClient().changeViewBuilder(new MiddleCardShopCLI(true)), true));
        row.addElem(new SizedBox(4,0));

        Drawable viewPersonalBoard = new Drawable();
        viewPersonalBoard.add(0," Look at your");
        viewPersonalBoard.add(0,"Personal Board");
        row.addElem(middlePhaseOption(viewPersonalBoard, () -> getClient().changeViewBuilder(new MiddlePersonalBoardCLI(CommonData.getThisPlayerIndex(), false, PersonalBoardBody.ViewMode.MIDDLE)), true));
        row.addElem(new SizedBox(4,0));

        Drawable moveResourcesDw = new Drawable();
        moveResourcesDw.add(0,"Move resources");
        moveResourcesDw.add(0," in Warehouse");
        row.addElem(middlePhaseOption(moveResourcesDw,() -> getClient().changeViewBuilder(new MiddlePersonalBoardCLI(-1, true)) , true));
        row.addElem(new SizedBox(6,0));

        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        Drawable buyCard = new Drawable();
        buyCard.add(0,"   Buy a card");
        buyCard.add(0,"from the Card Shop");
        row.addElem(middlePhaseOption(buyCard,()-> sendMessage(Choice.CARD_SHOP), simpleCardShop.getIsAnyCardPurchasable()));
        row.addElem(new SizedBox(4,0));

        Drawable playersInfoDw = new Drawable();
        playersInfoDw.add(0,"See players");
        playersInfoDw.add(0,"   info");
        row.addElem(middlePhaseOption(playersInfoDw, () -> getClient().changeViewBuilder(new MiddlePlayersInfoCLI()), true));
        row.addElem(new SizedBox(6,0));

        row.selectInEnabledOption(getCLIView(), "Select a middle phase option");

        return row;
    }

    private Option middlePhaseOption(Drawable d, Runnable r, boolean enabled){
        Option o = Option.from(d,r);
        o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
        o.setEnabled(enabled);
        return o;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if(MIDDLE_PHASE.name().equals(propertyName) || evt.getPropertyName().equals(PlayersInfo.class.getSimpleName())){
            run();
        }

        else super.propertyChange(evt);
    }

    protected void showWarningIfLastTurn() {

            PlayersInfo playersInfo = getSimpleModel().getElem(PlayersInfo.class).orElseThrow();

            if (playersInfo.getSimplePlayerInfoMap().size() > 1) {
            EndGameInfo endGameInfo = getSimpleModel().getElem(EndGameInfo.class).orElseThrow();

            if (endGameInfo.isCauseOfEndBeenAnnounced()) {
                endGameInfo.handleCauseOfEndString(playersInfo.getSimplePlayerInfoMap());
                String endGameReason = endGameInfo.getCauseOfEndStringWithNames();
                getCLIView().setSubTitle("Last turn! " + endGameReason, Color.BRIGHT_PURPLE, 1);
            }

        }
    }

}
