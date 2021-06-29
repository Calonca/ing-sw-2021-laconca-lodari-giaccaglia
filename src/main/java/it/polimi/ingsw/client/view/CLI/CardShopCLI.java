package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.idle.IDLEViewBuilderCLI;
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
import it.polimi.ingsw.client.view.CLI.middle.MiddlePhaseCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.ActiveLeaderBonusInfo;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CardShopCLI extends CardShopViewBuilder {


    public CardShopCLI(boolean viewing) {
        super(viewing);
    }

    public CardShopCLI(boolean viewing, boolean isIdle){
        super(viewing, isIdle);
    }


    /**
     * This method builds the ASCII Card Shop
     */
    @Override
    public void run() {
        getCLIView().setTitle("Card Shop");


        Column grid = new Column();


        ActiveLeaderBonusInfo cardShopDiscounts = getThisPlayerCache().getElem(ActiveLeaderBonusInfo.class).orElseThrow();


        List<Pair<ResourceAsset,Integer>> discountResources=cardShopDiscounts.getDiscountedResources();


        if(!discountResources.isEmpty())
        {
            Row bonusRow=new Row();
            bonusRow.addElem(Option.noNumber("BONUSES: "));
            grid.addElem(bonusRow);

            Drawable drawable=new Drawable();
            for(Pair<ResourceAsset,Integer> resources: discountResources){
                ResourceCLI res= ResourceCLI.fromAsset(resources.getKey());
                drawable.add(new DrawableLine(discountResources.indexOf(resources)*10, 0, res.getSymbol(), Color.BRIGHT_WHITE,res.getB()));
                drawable.add(new DrawableLine(discountResources.indexOf(resources)*10 + 4,0, " x "+discountResources.get(0).getValue()));

            }



            bonusRow.addElem(Option.noNumber(drawable));
        }

        for (int i = 3; i >= 1; i--) {
            Row verTest = new Row();
            for (int color = 0; color< NetworkDevelopmentCardColor.values().length-1; color++) {
                final NetworkDevelopmentCardColor cardColor = NetworkDevelopmentCardColor.fromInt(color);
                NetworkDevelopmentCard netCard = getSimpleCardShop()
                        .getCardFront(cardColor, i)
                        .map(DevelopmentCardAsset::getDevelopmentCard)
                        .orElse(null);
                int stackHeight = getSimpleCardShop().getStackHeight(cardColor,i);
                int finalI = i;
                Option o = Option.from(DrawableDevCard.fromDevCardAsset(netCard,stackHeight), () -> sendChosenCard(cardColor.ordinal(), finalI));
                o.setEnabled(netCard != null && netCard.isSelectable());
                verTest.addElem(o);
            }
            grid.addElem(verTest);
        }

        getCLIView().setBody(CanvasBody.centered(grid));
        boolean viewing = CardShopViewBuilder.viewing;
        if (viewing) {
            ViewBuilder nextViewBuilder = isIdlePhase ? new IDLEViewBuilderCLI() : new MiddlePhaseCLI();
            getCLIView().runOnInput("Press enter to go back", () -> getClient().changeViewBuilder(nextViewBuilder));
        }
        else grid.selectInEnabledOption(getCLIView(),"Select a card to buy it");
        getCLIView().show();

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
