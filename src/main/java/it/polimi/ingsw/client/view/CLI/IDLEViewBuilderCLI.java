package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;

import java.util.stream.Stream;


public class IDLEViewBuilderCLI extends it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        getCLIView().setTitle("Waiting for initial game phase, what do you want to do?");

        Stream<Option> optionsToAdd = getNewOptionList();
        Row initialRow = new Row(optionsToAdd);
        CanvasBody horizontalListBody = CanvasBody.centered(initialRow);

        getCLIView().setBody(horizontalListBody);
        initialRow.selectAndRunOption(getCLIView());
        getCLIView().show();
    }

    private Stream<Option> getNewOptionList(){

        Option moveResources = Option.from("Move resources in Warehouse" , this::choosePositions);
       //  Option seePlayerBoard = Option.from("See someone else's Personal Board", () -> getClient().changeViewBuilder(new OtherViewBuilderCLI()));
        //Option seePlayersInfo = Option.from("See player's info", () -> getClient().changeViewBuilder(new LoadableMatches()));

        return  Stream.of(moveResources);
    }

    public void choosePositions(){

        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.MOVING_RES);
        board.initializeFaithTrack(getSimpleModel());

        board.initializeMove();
        board.setStrongBox(PersonalBoardBody.strongBoxBuilder(getThisPlayerCache().getElem(SimpleStrongBox.class).orElseThrow(), board));
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage("Select move starting position or press ENTER to go back");
        getCLIView().setBody(board);
        getCLIView().show();

    }

    private void idleOnSetup(){

        SpinnerBody spinnerBody = new SpinnerBody();
        getCLIView().setBody(spinnerBody);

        Canvas canvas = Canvas.withBorder(CLI.width,getCLIView().getMaxBodyHeight());
        Drawable dwl = new Drawable();
        dwl.add(0,"Waiting for initial phase state");
        canvas.addDrawable(dwl);
        spinnerBody.setMeanwhileShow(canvas);

        getCLIView().show();

    }

}

