package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLIResourceMarket;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.drawables.MarbleCLI;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.ActiveLeaderBonusInfo;
import it.polimi.ingsw.network.simplemodel.SimpleMarketBoard;

import java.util.List;

import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_POSITION_FOR_RESOURCES;
import static it.polimi.ingsw.client.simplemodel.State.CHOOSING_WHITEMARBLE_CONVERSION;

public class MiddleResourceMarketCLI extends ResourceMarketViewBuilder implements CLIBuilder {

    int numOfConversions;

    /**
     * This method initializes the market marbles and eventually displays the market bonuses
     */
    @Override
    public void run() {


        if (getThisPlayerCache().getCurrentState().equals(CHOOSING_POSITION_FOR_RESOURCES.name())){
            choosePositions();
            return;
        }

        if (getThisPlayerCache().getCurrentState().equals(CHOOSING_WHITEMARBLE_CONVERSION.name())){
            chooseMarbleConversion();
            return;
        }

        getCLIView().setTitle("Take resources from the resource market");

        Row root = new Row();

        ActiveLeaderBonusInfo marketBonuses = getThisPlayerCache().getElem(ActiveLeaderBonusInfo.class).orElseThrow();
        Column activeConversions = root.addAndGetColumn();
        numOfConversions = marketBonuses.getMarketBonusResources().size();
        if(numOfConversions>0)
            buildConversionColumn(activeConversions, marketBonuses);

        int width = numOfConversions == 0 ? 100 : 80;
        root.addElem(new SizedBox(width,0));

        Column grid = root.addAndGetColumn();

        SimpleMarketBoard marketBoard = getSimpleModel().getElem(SimpleMarketBoard.class).orElseThrow();
        CLIResourceMarket.buildMarketGrid(grid, marketBoard);
        getCLIView().setBody(CanvasBody.centered(root));

        MarbleAsset[][] marketMatrix = getMarketMatrix();
        int rows = marketMatrix.length;
        int columns = marketMatrix.length>0 ? marketMatrix[0].length : 0;
        getCLIView().runOnIntInput("Select a row or column to take resources","Incorrect line",0,rows+columns,
            ()->{
                if(!marketBonuses.getMarketBonusResources().isEmpty())
                    System.out.println("should select bonuses");
                int line = getCLIView().getLastInt();
                sendLine(line);
            });

        getCLIView().show();
    }

    /**
     * This method is called during CHOOSING_POSITION_FOR_RESOURCES
     */
    @Override
    public void choosePositions() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.MOVING_RES);
        board.preparePersonalBoard( "Select move starting position or discard resources");
    }

    /**
     * This method is called during CHOOSING_WHITEMARBLE_CONVERSION
     */
    @Override
    public void chooseMarbleConversion() {

        Row root = new Row();
        Column activeConversions = root.addAndGetColumn();

        ActiveLeaderBonusInfo marketBonuses = getThisPlayerCache().getElem(ActiveLeaderBonusInfo.class).orElseThrow();


        List<ResourceAsset> marketBonusResources=marketBonuses.getMarketBonusResources();

        for(ResourceAsset res : marketBonusResources) {
            addConversionsToColumn(activeConversions, res);
        }

        String text = conversionString(numOfConversions);
        root.selectInEnabledOption(getCLIView(), text);
        numOfConversions--;

        getCLIView().setBody(CanvasBody.centered(root));
        getCLIView().show();

    }

    private void buildConversionColumn(Column activeConversions, ActiveLeaderBonusInfo marketBonuses){

            activeConversions.addElem(new SizedBox(20, 0));
            activeConversions.addElem(Option.noNumber("Active White Marble conversions"));

        for(ResourceAsset res : marketBonuses.getMarketBonusResources()) {
            addConversionsToColumn(activeConversions, res);
        }

    }

    /**
     * This method converts resourceAssets to MarbleAssets
     */
    private void addConversionsToColumn(Column activeConversions, ResourceAsset res) {

        if(res==ResourceAsset.STONE)
            activeConversions.addElem(buildResourceChoice(MarbleAsset.GRAY,3));
        else if(res==ResourceAsset.SERVANT)
            activeConversions.addElem(buildResourceChoice(MarbleAsset.PURPLE,1));
        else if(res==ResourceAsset.GOLD)
            activeConversions.addElem(buildResourceChoice(MarbleAsset.YELLOW,0));
        else if(res==ResourceAsset.SHIELD)
            activeConversions.addElem(buildResourceChoice(MarbleAsset.BLUE,2));
    }

    private String conversionString(int index){

        if(index==2)
            return "Select the first White Marble conversion";
        else
            return "Select the second White Marble conversion";

    }

    /**
     *
     * @param res is a marbleAsset
     * @param resourceNumber is the corresponding resource number
     * @return is the corresponding selectable Option
     */
    private Option buildResourceChoice(MarbleAsset res, int resourceNumber){


        MarbleCLI marbleCli = MarbleCLI.fromAsset(res);
        String resourceSymbol = marbleCli.fromMarbleCLI().getNameWithSpaces();
        Drawable conversionDrawable = new Drawable();
        conversionDrawable.add(MarbleCLI.WHITE.toBigDrawable());
        DrawableLine arrow1 = new DrawableLine(12, 2, " --> ", Color.BRIGHT_GREEN, Background.DEFAULT);
        conversionDrawable.add(arrow1);
        conversionDrawable.add(marbleCli.toBigDrawableCustomXPos(20));
        DrawableLine arrow2 = new DrawableLine(32, 2, " --> ", Color.BRIGHT_GREEN, Background.DEFAULT);
        conversionDrawable.add(arrow2);
        DrawableLine resource = new DrawableLine(40, 2, resourceSymbol, Color.BRIGHT_WHITE, marbleCli.fromMarbleCLI().getB());

        Drawable dw = new Drawable(conversionDrawable, resource);

        DrawableLine line = new DrawableLine(0,5, "────────────────────────────", Color.BRIGHT_BLACK, Background.DEFAULT);

        dw.add(line);


        Option o = Option.from(dw, ()->sendWhiteMarbleConversion(resourceNumber));
        o.setMode(Option.VisMode.NUMBER_TO_LEFT);
        o.setEnabled(true);
        return o;
    }
}
