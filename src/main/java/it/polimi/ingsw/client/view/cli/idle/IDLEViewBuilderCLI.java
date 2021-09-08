package it.polimi.ingsw.client.view.cli.idle;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.cli.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.cli.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.cli.layout.drawables.Timer;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;

/**
 * During IDLE phase a CLI user can see the other players' board, as well as every other information
 */
public class IDLEViewBuilderCLI extends IDLEViewBuilder implements CLIBuilder {

    @Override
    public void run() {


        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new IDLEReportInfoCLI());
        showWarningIfLastTurn();

        getCLIView().setTitle("Waiting for initial game phase, what do you want to do?");

        Row initialRow = getRowOfOptions();
        CanvasBody horizontalListBody = CanvasBody.centered(initialRow);
        getCLIView().setBody(horizontalListBody);
        initialRow.selectInEnabledOption(getCLIView(),"Select a choice");
        getCLIView().show();

    }

    private Row getRowOfOptions() {

        Row row = new Row();

        Drawable moveResourcesDw = new Drawable();
        moveResourcesDw.add(0, "Move resources");
        moveResourcesDw.add(0, " in Warehouse");
        row.addElem(idlePhaseOption(moveResourcesDw, () -> getClient().changeViewBuilder(new IDLEPersonalBoardCLI(-1, true)), true));
        row.addElem(new SizedBox(6, 0));

        Drawable vaticanReportDw = new Drawable();
        vaticanReportDw.add(0, "See Vatican Report");
        vaticanReportDw.add(0, "     status");

        boolean enabled = false;
        if (getSimpleModel().getElem(VaticanReportInfo.class).get().hasReportOccurred())
            enabled = true;

        row.addElem(idlePhaseOption(vaticanReportDw, () -> getClient().changeViewBuilder(new IDLEReportInfoCLI(new IDLEViewBuilderCLI())), enabled));
        row.addElem(new SizedBox(6, 0));

        Drawable resourceMarketDw = new Drawable();
        resourceMarketDw.add(0,"      Look at");
        resourceMarketDw.add(0,"the resource market");
        row.addElem(idlePhaseOption(resourceMarketDw, () -> getClient().changeViewBuilder(new IDLEResourceMarket()), true));

        Drawable playersInfoDw = new Drawable();
        playersInfoDw.add(0, "See players");
        playersInfoDw.add(0, "   info");
        row.addElem(idlePhaseOption(playersInfoDw, () -> getClient().changeViewBuilder(new IDLEPlayersInfoCLI()), true));
        row.addElem(new SizedBox(6, 0));

        Drawable boardDw = new Drawable();
        boardDw.add(0, "  See your");
        boardDw.add(0, "Personal Board");
        row.addElem(idlePhaseOption(boardDw, () -> getClient().changeViewBuilder(new IDLEPersonalBoardCLI(CommonData.getThisPlayerIndex(), false, PersonalBoardBody.ViewMode.IDLE)), true));
        row.addElem(new SizedBox(6, 0));

        Drawable viewCardShop = new Drawable();
        viewCardShop.add(0, "  Look at cards");
        viewCardShop.add(0, "from the Card Shop");
        row.addElem(idlePhaseOption(viewCardShop, () -> getClient().changeViewBuilder(new IDLECardShopCLI()), true));
        row.addElem(new SizedBox(6, 0));



        row.selectInEnabledOption(getCLIView(), "Select a idle phase option");

        return row;
    }

    private Option idlePhaseOption(Drawable d, Runnable r, boolean enabled) {
        Option o = Option.from(d, r);
        o.setEnabled(enabled);
        return o;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();
        if (INITIAL_PHASE.name().equals(propertyName)) {

            if (getSimpleModel().getPlayersCaches().length != 1) {
                run(); // to update players info leader option

                Timer.showSecondsOnCLI(getCLIView(), "It's almost your turn! Seconds left : ", 3);
            }
            getClient().changeViewBuilder(InitialOrFinalPhaseViewBuilder.getBuilder(getClient().isCLI(), true));

        } else if (MIDDLE_PHASE.name().equals(propertyName)) {

            if (getSimpleModel().getPlayersCaches().length != 1) {
                run(); // to update players info leader option
                Timer.showSecondsOnCLI(getCLIView(), "It's almost your turn! Seconds left : ", 3);
            }

            getClient().changeViewBuilder(MiddlePhaseViewBuilder.getBuilder(getClient().isCLI()));
        } else if (IDLE.name().equals(propertyName) || propertyName.equals(PlayersInfo.class.getSimpleName()) || propertyName.equals(CommonData.currentPlayerString)) {
            run();
        } else super.propertyChange(evt);
    }

    protected void showWarningIfLastTurn() {

        PlayersInfo playersInfo = getSimpleModel().getElem(PlayersInfo.class).orElseThrow();
        if(playersInfo.getSimplePlayerInfoMap().size()>1) {
            EndGameInfo endGameInfo = getSimpleModel().getElem(EndGameInfo.class).orElseThrow();

            if (endGameInfo.isCauseOfEndBeenAnnounced()) {
                endGameInfo.handleCauseOfEndString(playersInfo.getSimplePlayerInfoMap());
                String endGameReason = endGameInfo.getCauseOfEndStringWithNames();
                getCLIView().setSubTitle("Last turn! " + endGameReason, Color.BRIGHT_PURPLE);
            }

        }

    }
}

