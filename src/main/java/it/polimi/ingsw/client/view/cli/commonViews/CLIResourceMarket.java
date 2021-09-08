package it.polimi.ingsw.client.view.cli.commonViews;

import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.drawables.MarbleCLI;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.simplemodel.SimpleMarketBoard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class CLIResourceMarket {

    private CLIResourceMarket(){}

     public static void buildMarketGrid(Column grid, SimpleMarketBoard marketBoard){

        Row gridAndLines = grid.addAndGetRow();

        Column marbleGrid = gridAndLines.addAndGetColumn();

        MarbleAsset[][] marketMatrix = marketBoard.getMarbleMatrix();
        int rows = marketMatrix.length;
        int columns = marketMatrix.length>0?marketMatrix[0].length:0;

        for (MarbleAsset[] assetRow : marketMatrix) {
            marbleGrid.addElem(new Row(buildResourceOptionStream(assetRow)));
            marbleGrid.addElem(new SizedBox(2,0));
        }

        Column lastColumn = gridAndLines.addAndGetColumn();
        lastColumn.addElem(new SizedBox(0, MarbleCLI.height()/2));
        List<Option> collect = buildLineStream(0, rows).collect(Collectors.toList());
        for (int i = 0, collectSize = collect.size(); i < collectSize; i++) {
            Option o = collect.get(i);
            lastColumn.addElem(o);
            if (i<collectSize-1)
                lastColumn.addElem(new SizedBox(0, MarbleCLI.height() - 1));
        }

        Row lastLine = new Row(buildLineStream(rows, rows+columns));
        lastLine.addElem(buildResourceOption(marketBoard.getSlideMarble()));

        grid.addElem(lastLine);

    }

    private static Stream<Option> buildResourceOptionStream(MarbleAsset[] res){
        return Arrays.stream(res).map(CLIResourceMarket::buildResourceOption);
    }


    /**
     * @param res is a marbleAsset
     * @return the corresponding Option
     */
    private static Option buildResourceOption(MarbleAsset res){
        return Option.noNumber(MarbleCLI.fromAsset(res).toBigDrawable());
    }

    private static Stream<Option> buildLineStream(int start, int stop){
        return IntStream.range(start,stop).mapToObj(i->Option.noNumber(StringUtil.untilReachingSize(" Line "+i,MarbleCLI.width())));
    }
}
