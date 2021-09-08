package it.polimi.ingsw.client.view.cli.middle;

import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.cli.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.cli.layout.ResChoiceRowCLI;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.ProductionViewBuilder;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleProductions.SimpleProduction;

import java.util.Objects;
import java.util.stream.Stream;

public class MiddleProductionViewCLI extends ProductionViewBuilder implements CLIBuilder {


    /**
     * This method starts the view, asking the player to terminate or choose a production
     */
    @Override
    public void run() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.CHOOSE_PRODUCTION);
        board.initializeFaithTrack();
        board.initializeBuyOrChoosePos();
        SimpleCardCells simpleCardCells = Objects.requireNonNull(getThisPlayerCache()).getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage("Select a production or press ENTER to produce");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    /**
     * This method is called after selecting a selectable production
     */
    @Override
    public void choosingResForProduction() {

        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.SELECT_RES_FOR_PROD);
        SimpleCardCells simpleCardCells = Objects.requireNonNull(getThisPlayerCache()).getElem(SimpleCardCells.class).orElseThrow();
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
