package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder;
import it.polimi.ingsw.client.view.abstractview.InitialOrFinalPhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.*;


public class IDLEViewBuilderCLI extends IDLEViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        buildView();
    }

    protected void buildView(){

        getCLIView().setTitle("Waiting for initial game phase, what do you want to do?");

        Row initialRow = getNewRow();
        CanvasBody horizontalListBody = CanvasBody.centered(initialRow);
        getCLIView().setBody(horizontalListBody);
        initialRow.selectAndRunOption(getCLIView());
        getCLIView().show();
    }

    private Row getNewRow(){
        Row row = new Row();

        Drawable moveResourcesDw = new Drawable();
        moveResourcesDw.add(0,"Move resources");
        moveResourcesDw.add(0," in Warehouse");
        row.addElem(idlePhaseOption(moveResourcesDw,this::choosePositions, true));
        row.addElem(new SizedBox(6,0));

        Drawable vaticanReportDw = new Drawable();
        vaticanReportDw.add(0,"See Vatican Report");
        vaticanReportDw.add(0,"    status");

        boolean enabled = false;
        if(getSimpleModel().getElem(VaticanReportInfo.class).get().hasReportOccurred())
           enabled = true;

        row.addElem(idlePhaseOption(vaticanReportDw, () -> getClient().changeViewBuilder(new ReportInfoCLI()), enabled));
        row.addElem(new SizedBox(6,0));

        Drawable playersInfoDw = new Drawable();
        playersInfoDw.add(0,"See players");
        playersInfoDw.add(0,"   info");
        row.addElem(idlePhaseOption(playersInfoDw, () -> getClient().changeViewBuilder(new PlayersInfoCLI()), true));
        row.addElem(new SizedBox(6,0));

        Drawable boardDw = new Drawable();
        boardDw.add(0,"  See your");
        boardDw.add(0,"Personal Board");
        row.addElem(idlePhaseOption(boardDw,this::seePersonalBoard, true));
        row.addElem(new SizedBox(6,0));

        Drawable viewCardShop = new Drawable();
        viewCardShop.add(0,"  Look at cards");
        viewCardShop.add(0,"from the Card Shop");
        row.addElem(idlePhaseOption(viewCardShop,()-> getClient().changeViewBuilder(new CardShopCLI(true, true)), true));
        row.addElem(new SizedBox(6,0));

        row.selectInEnabledOption(getCLIView(), "Select a idle phase option");

        return row;
    }

    private void preparePersonalBoard(PersonalBoardBody.Mode mode, String message){

        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), mode);
        board.initializeFaithTrack(getSimpleModel());

        board.initializeMove();
        board.setStrongBox(PersonalBoardBody.strongBoxBuilder(getThisPlayerCache().getElem(SimpleStrongBox.class).orElseThrow(), board));
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage(message);
        getCLIView().setBody(board);
        getCLIView().show();

    }
    private void seePersonalBoard(){
        preparePersonalBoard(PersonalBoardBody.Mode.VIEWING, "Press ENTER to go back");
    }
    private void choosePositions(){preparePersonalBoard(PersonalBoardBody.Mode.MOVING_RES, "Select move starting position or press ENTER to go back");}
    private Option idlePhaseOption(Drawable d, Runnable r, boolean enabled){
        Option o = Option.from(d,r);
        o.setEnabled(enabled);
        return o;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();
        if (INITIAL_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(InitialOrFinalPhaseViewBuilder.getBuilder(getClient().isCLI(),true));
        }else if (MIDDLE_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(MiddlePhaseViewBuilder.getBuilder(getClient().isCLI()));
        }
        else if(IDLE.name().equals(propertyName) || evt.getPropertyName().equals(PlayersInfo.class.getSimpleName())){
            buildView();
        }
        else ViewBuilder.printWrongStateReceived(evt);
    }
}

