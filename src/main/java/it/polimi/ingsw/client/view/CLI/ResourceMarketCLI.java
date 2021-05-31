package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.GridBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.drawables.MarbleCLI;
import it.polimi.ingsw.client.view.CLI.layout.Grid;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ResourceMarketCLI extends ResourceMarketViewBuilder implements CLIBuilder {
    @Override
    public void run() {
        getCLIView().setTitle("Take resources from the resource market");
        Grid grid = new Grid();

        MarbleAsset[][] marketMatrix = getMarketMatrix();
        int rows = marketMatrix.length;
        int columns = marketMatrix.length>0?marketMatrix[0].length:0;

        for (int i = 0; i < rows; i++) {
            MarbleAsset[] assetRow = marketMatrix[i];
            HorizontalList horizontalList = new HorizontalList(buildResourceOptionStream(assetRow), MarbleCLI.height());
            horizontalList.addOption(Option.activeFrom("\n\nLine "+i+StringUtil.spaces(MarbleCLI.width()-6)));
            grid.addRow(horizontalList);
        }

        HorizontalList lastLine = new HorizontalList(buildColumnsStream(rows, rows+columns),MarbleCLI.height());
        lastLine.addOption(buildResourceOption(getSimpleMarketBoard().getSlideMarble()));
        grid.addRow(lastLine);

        grid.setShowNumbers(false);
        getCLIView().setBody(new GridBody(grid));

        getCLIView().runOnIntInput("Select a row or column to take resources","Incorrect line",0,rows+columns,
            ()->{
                int line = getCLIView().getLastInt();
                sendLine(line);
            });

        getCLIView().show();
    }

    @Override
    public void choosePositions() {
        PersonalBoardBody personalBoard = new PersonalBoardBody(getThisPlayerCache(),true,true);
        getCLIView().setBody(personalBoard);
        personalBoard.selectInDiscardBox();
        getCLIView().show();
    }

    private Stream<Option> buildColumnsStream(int start, int stop){
        return IntStream.range(start,stop).mapToObj(i->Option.activeFrom(StringUtil.stringUntilReachingSize(" Line "+i,MarbleCLI.width())));
    }

    private Stream<Option> buildResourceOptionStream(MarbleAsset[] res){
        return Arrays.stream(res).map(this::buildResourceOption);
    }

    private Option buildResourceOption(MarbleAsset res){
        return Option.from(MarbleCLI.fromAsset(res).toBigDrawableList(),()->{});
    }

}
