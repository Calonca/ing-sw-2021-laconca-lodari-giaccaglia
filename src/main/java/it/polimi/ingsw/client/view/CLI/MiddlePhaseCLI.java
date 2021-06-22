package it.polimi.ingsw.client.view.CLI;


import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.IDLE.ReportInfoCLI;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;

public class MiddlePhaseCLI extends MiddlePhaseViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        showVaticanReportInfoBeforeTransition();
        getCLIView().setTitle("Select a choice");

        String numOfPlayers= "NUM OF PLAYERS" + getCommonData().playersOfMatch().map(l -> l.length).orElse(0);
        Row row = new Row();

        Drawable po=new Drawable();
        po.add(0,numOfPlayers);
        row.addElem(middlePhaseOption(po,()->{},false));

        row.addElem(new SizedBox(4,0));

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
        row.addElem(middlePhaseOption(viewPersonalBoard,() -> PersonalBoardBody.seePersonalBoard(getCommonData().getThisPlayerIndex(), PersonalBoardBody.ViewMode.MIDDLE), true));
        row.addElem(new SizedBox(4,0));

        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        Drawable buyCard = new Drawable();
        buyCard.add(0,"Buy a card");
        buyCard.add(0,"from the Card Shop");
        row.addElem(middlePhaseOption(buyCard,()-> sendMessage(Choice.CARD_SHOP), simpleCardShop.getIsAnyCardPurchasable()));
        row.addElem(new SizedBox(4,0));

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
                getClient().changeViewBuilder(new ReportInfoCLI(ReportInfoCLI.ViewMode.AFTER_REPORT));
            }

        }
    }


}
