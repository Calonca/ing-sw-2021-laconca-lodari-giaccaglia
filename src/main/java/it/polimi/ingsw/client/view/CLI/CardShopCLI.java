package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardCells;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody.prodOption;

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
        SimpleFaithTrack[] simpleFaithTracks =Arrays.stream(getSimpleModel().getPlayersCaches())
                .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
        board.setTop(new FaithTrackGridElem(getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks));
        Row prodsRow = new Row();
        board.setProductions(prodsRow);
        NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();
        prodsRow.addElem(Option.noNumber(DrawableDevCard.fromDevCardAsset(card)));
        List<ResourceAsset> costs = card.getCostList().stream().flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                .sorted(Comparator.comparing(ResourceAsset::getResourceNumber)).collect(Collectors.toList());
        board.setResChoiceRow(new ResChoiceRow(0,costs,new ArrayList<>()));
        board.initializeBuyOrChoosePos();
        board.setMessage("Select the resources to buy the card, only working for warehouse for now");
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
        Map<Integer,Optional<NetworkDevelopmentCard>> frontCards=simpleCardCells.getVisibleCardsOnCells().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().map(DevelopmentCardAsset::getDevelopmentCard)));
        Row prodsRow = new Row(frontCards.entrySet().stream().map(e-> {
            Option cell = prodOption(
                    () -> sendCardPlacementPosition(e.getKey()),
                    e.getValue().orElse(null));
            cell.setEnabled(simpleCardCells.isSpotAvailable(e.getKey()));
            return cell;
        }));
        board.setProductions(prodsRow);
        board.setMessage("Choose a position for the card");
        getCLIView().setBody(board);
        getCLIView().show();
    }


}
