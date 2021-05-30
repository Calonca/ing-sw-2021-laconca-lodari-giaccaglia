package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.GridBody;
import it.polimi.ingsw.client.view.CLI.layout.drawables.MarbleCLI;
import it.polimi.ingsw.client.view.CLI.layout.Grid;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ResourceMarketCLI extends ResourceMarketViewBuilder implements CLIBuilder {
    @Override
    public void run() {
        getCLIView().setTitle("Take resources from the resource market");
        Grid grid = new Grid();

        MarbleAsset[][] marketMatrix = getMarketMatrix();
        for (int i = 0, marketMatrixLength = marketMatrix.length; i < marketMatrixLength; i++) {
            MarbleAsset[] assetRow = marketMatrix[i];
            HorizontalList horizontalList = new HorizontalList(buildResourceOptionStream(assetRow), MarbleCLI.height());
            horizontalList.addOption(Option.activeFrom("\n\nRow "+i+StringUtil.spaces(MarbleCLI.width()-5)));
            grid.addRow(horizontalList);
        }

        HorizontalList lastLine = new HorizontalList(buildColumnsStream(marketMatrix.length, marketMatrix.length+marketMatrix[0].length),1);
        lastLine.addOption(Option.from(""));
        grid.addRow(lastLine);

        grid.setShowNumbers(false);
        getCLIView().setBody(new GridBody(grid));

        getCLIView().runOnIntInput("Select a row or column","Incorrect line",0,6,
            ()->{
                int line = getCLIView().getLastInt();
                sendLine(line);
            });

        getCLIView().show();
    }

    private Stream<Option> buildColumnsStream(int start, int stop){
        return IntStream.range(start,stop).mapToObj(i->Option.activeFrom(StringUtil.stringUntilReachingSize("Column "+i,MarbleCLI.width())));
    }

    private Stream<Option> buildResourceOptionStream(MarbleAsset[] res){
        return Arrays.stream(res).map(this::buildResourceOption);
    }

    private Option buildResourceOption(MarbleAsset res){
        return Option.from(MarbleCLI.fromAsset(res).toBigDrawableList(),()->{});
    }

    @Override
    public void choosePositions() {
        System.out.println("Insert code here to move your resources");
    }

}
