package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.Column;
import it.polimi.ingsw.client.view.CLI.layout.Row;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.MarbleCLI;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_POSITION_FOR_RESOURCES;

public class ResourceMarketCLI extends ResourceMarketViewBuilder implements CLIBuilder {
    @Override
    public void run() {
        //Todo better wrong event handling
        if (getThisPlayerCache().getCurrentState().equals(CHOOSING_POSITION_FOR_RESOURCES.name())){
            choosePositions();
            return;
        }
        getCLIView().setTitle("Take resources from the resource market");
        Column root = new Column();

        Row gridAndLeft = root.addAndGetRow();
        Column grid = gridAndLeft.addAndGetColumn();

        MarbleAsset[][] marketMatrix = getMarketMatrix();
        int rows = marketMatrix.length;
        int columns = marketMatrix.length>0?marketMatrix[0].length:0;

        for (MarbleAsset[] assetRow : marketMatrix) {
            grid.addElem(new Row(buildResourceOptionStream(assetRow)));
            grid.addElem(new SizedBox(2,0));
        }

        Column lastColumn = gridAndLeft.addAndGetColumn();
        lastColumn.addElem(new SizedBox(0,MarbleCLI.height()/2));
        List<Option> collect = buildLineStream(0, rows).collect(Collectors.toList());
        for (int i = 0, collectSize = collect.size(); i < collectSize; i++) {
            Option o = collect.get(i);
            lastColumn.addElem(o);
            if (i<collectSize-1)
                lastColumn.addElem(new SizedBox(0, MarbleCLI.height() - 1));
        }

        Row lastLine = new Row(buildLineStream(rows, rows+columns));
        lastLine.addElem(buildResourceOption(getSimpleMarketBoard().getSlideMarble()));
        root.addElem(lastLine);

        getCLIView().setBody(CanvasBody.centered(root));

        getCLIView().runOnIntInput("Select a row or column to take resources","Incorrect line",0,rows+columns,
            ()->{
                int line = getCLIView().getLastInt();
                sendLine(line);
            });

        getCLIView().show();
    }

    @Override
    public void choosePositions() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.MOVING_RES);
        SimpleFaithTrack[] simpleFaithTracks = Arrays.stream(getSimpleModel().getPlayersCaches())
                .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
        board.setTop(new FaithTrackGridElem(getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks));

        board.initializeMove();
        board.setStrongBox(PersonalBoardBody.strongBoxBuilder(getThisPlayerCache().getElem(SimpleStrongBox.class).orElseThrow(), board));
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage("Select move starting position or discard resources");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    private Stream<Option> buildLineStream(int start, int stop){
        return IntStream.range(start,stop).mapToObj(i->Option.noNumber(StringUtil.untilReachingSize(" Line "+i,MarbleCLI.width())));
    }

    private Stream<Option> buildResourceOptionStream(MarbleAsset[] res){
        return Arrays.stream(res).map(this::buildResourceOption);
    }

    private Option buildResourceOption(MarbleAsset res){
        return Option.noNumber(MarbleCLI.fromAsset(res).toBigDrawable());
    }

}
