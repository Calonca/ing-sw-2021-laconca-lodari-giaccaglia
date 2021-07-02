package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.commonViews.CLICardShop;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.ResChoiceRowCLI;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.ActiveLeaderBonusInfo;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MiddleCardShopCLI extends CardShopViewBuilder {


    /**
     * Method to initialize the viewBuilder
     *
     * @param viewing represents the card shop state
     */
    public MiddleCardShopCLI(boolean viewing) {
        super(viewing);
    }

    /**
     * This method builds the ASCII Card Shop
     */
    @Override
    public void run() {

        getCLIView().setTitle("Card Shop");

        Column grid = new Column();


        ActiveLeaderBonusInfo activeDiscounts = getThisPlayerCache().getElem(ActiveLeaderBonusInfo.class).orElseThrow();
        List<Pair<ResourceAsset,Integer>> discountResources= activeDiscounts.getDiscountedResources();

        Column bonusColumn= grid.addAndGetColumn();

        if(activeDiscounts.getDiscountedResources().size()>0)
            buildDiscountsColumn(bonusColumn, activeDiscounts);
        grid.addElem(bonusColumn);



        CLICardShop cliCardShop = new CLICardShop();

        cliCardShop.buildCardShop(grid, viewing);

        getCLIView().setBody(CanvasBody.centered(grid));

        if (viewing) {

            getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(new MiddlePhaseCLI()));
        }
        else grid.selectInEnabledOption(getCLIView(),"Select a card to buy it");
        getCLIView().show();

    }

    private void buildDiscountsColumn(Column bonusColumn, ActiveLeaderBonusInfo discounts){
        Drawable dr = new Drawable();
        dr.add(new DrawableLine(-27, 0, "Active Discounts: ", Color.YELLOW, Background.DEFAULT));
        bonusColumn.addElem(Option.noNumber(dr));

        List<Pair<ResourceAsset,Integer>> discountsList = discounts.getDiscountedResources();
        for(Pair<ResourceAsset, Integer> pair : discountsList){
           bonusColumn.addElem(addDiscountToColumn(pair));
        }
    }

    private Option addDiscountToColumn(Pair<ResourceAsset,Integer> discountPair){

        ResourceCLI resourceCLI = ResourceCLI.fromAsset(discountPair.getKey());
        String resourceSymbol =resourceCLI.getFullName();
        DrawableLine resource = new DrawableLine(-27, -2, resourceSymbol, Color.BRIGHT_WHITE, resourceCLI.getB());
        Drawable dw = new Drawable();
        dw.add(resource);
        return Option.noNumber(dw);
    }


    /**
     * This method is called during SELECT_RESOURCES_FOR_DEVCARD
     */
    @Override
    public void selectResources() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.SELECT_CARD_SHOP);
        NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();

        GridElem chosenCard = Option.noNumber(DrawableDevCard.fromDevCardAsset(card,0));
        List<ResourceAsset> costs = card.getCosts();
        ResChoiceRowCLI choices = new ResChoiceRowCLI(0,costs,new ArrayList<>());
        Row top = new Row(Stream.of(new SizedBox(10,0),chosenCard,choices.getGridElem()));
        board.setTop(top);
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setResChoiceRow(choices);
        board.initializeBuyOrChoosePos();
        board.setMessage("Select the resources to buy the card");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    /**
     * This method is called during CHOOSING_POSITION_FOR_DEVCARD
     */
    @Override
    public void choosePositionForCard() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.CHOOSE_POS_FOR_CARD);
        Row chosenCard = new Row();
        NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();
        chosenCard.addElem(Option.noNumber(DrawableDevCard.fromDevCardAsset(card,0)));
        board.setTop(chosenCard);
        board.initializeBuyOrChoosePos();
        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        Row prodsRow = board.productionsBuilder(simpleCardCells);
        board.setProductions(prodsRow);
        board.setMessage("Choose a position for the card");
        getCLIView().setBody(board);
        getCLIView().show();
    }


}
