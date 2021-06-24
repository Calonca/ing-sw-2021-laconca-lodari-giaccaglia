package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.ResChoiceRowCLI;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleProductions.SimpleProduction;

import java.util.stream.Stream;

public class MiddleProductionViewCLI extends ProductionViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.CHOOSE_PRODUCTION);
        board.initializeFaithTrack(getSimpleModel());
        board.initializeBuyOrChoosePos();
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage("Select a production or press ENTER to produce");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    @Override
    public void choosingResForProduction() {

        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.SELECT_RES_FOR_PROD);
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        SimpleProduction lastSelectedProduction = simpleCardCells.getSimpleProductions().lastSelectedProduction().orElseThrow();
        ResChoiceRowCLI choices = new ResChoiceRowCLI(0,lastSelectedProduction.getUnrolledInputs(),lastSelectedProduction.getUnrolledOutputs());
        Row top = new Row(Stream.of(new SizedBox(1,0),choices.getGridElem()));
        board.setTop(top);
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setResChoiceRow(choices);
        board.initializeBuyOrChoosePos();
        board.setMessage("Select the resources for the production");
        getCLIView().setBody(board);
        getCLIView().show();

    }
}
