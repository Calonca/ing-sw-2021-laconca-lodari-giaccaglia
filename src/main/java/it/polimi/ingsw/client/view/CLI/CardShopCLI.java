package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardShopCLI extends CardShopViewBuilder {


    public CardShopCLI(boolean viewing) {
        super(viewing);
    }

    @Override
    public void run() {
        getCLIView().setTitle("Card Shop");

        Column grid = new Column();
        for (int i = 3; i >= 1; i--) {
            Row verTest = new Row();
            for (int color = 0; color< NetworkDevelopmentCardColor.values().length-1; color++) {
                NetworkDevelopmentCard netCard = getSimpleCardShop()
                        .getCardFront(NetworkDevelopmentCardColor.fromInt(color), i)
                        .map(DevelopmentCardAsset::getDevelopmentCard)
                        .orElse(null);
                int finalI = i;
                int finalColor = color;
                Option o = Option.from(DrawableDevCard.fromDevCardAsset(netCard), () -> sendChosenCard(finalColor, finalI));
                o.setEnabled(netCard != null && netCard.isSelectable());
                verTest.addElem(o);
            }
            grid.addElem(verTest);
        }

        getCLIView().setBody(CanvasBody.centered(grid));
        boolean viewing = CardShopViewBuilder.viewing;
        if (viewing)
            getCLIView().runOnInput("Press enter to go back",()->getClient().changeViewBuilder(new MiddlePhaseCLI()));
        else grid.selectInEnabledOption(getCLIView(),"Select a card to buy it");
        getCLIView().show();

    }

    /**
     * Get called when choosing resources to buy the card
     */
    @Override
    public void selectResources() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.SELECT_CARD_SHOP);
        NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();

        GridElem chosenCard = Option.noNumber(DrawableDevCard.fromDevCardAsset(card));
        List<ResourceAsset> costs = card.getCostList().stream().flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                .sorted(Comparator.comparing(ResourceAsset::getResourceNumber)).collect(Collectors.toList());
        ResChoiceRow choices = new ResChoiceRow(0,costs,new ArrayList<>());
        Row top = new Row(Stream.of(new SizedBox(10,0),chosenCard,choices));
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
     * Gets called when the player needs to place the development card on his personal board
     */
    @Override
    public void choosePositionForCard() {
        PersonalBoardBody board = new PersonalBoardBody(getThisPlayerCache(), PersonalBoardBody.Mode.CHOOSE_POS_FOR_CARD);
        Row chosenCard = new Row();
        NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();
        chosenCard.addElem(Option.noNumber(DrawableDevCard.fromDevCardAsset(card)));
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
