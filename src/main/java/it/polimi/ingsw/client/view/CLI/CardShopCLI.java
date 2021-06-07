package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.drawables.FaithTrackGridElem;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;
import it.polimi.ingsw.network.simplemodel.SimpleStrongBox;

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
        SimpleFaithTrack[] simpleFaithTracks =Arrays.stream(getSimpleModel().getPlayersCaches())
                .map(c->c.getElem(SimpleFaithTrack.class).orElseThrow()).toArray(SimpleFaithTrack[]::new);
        board.setFaithTrack(new FaithTrackGridElem(getThisPlayerCache().getElem(SimpleFaithTrack.class).orElseThrow(),simpleFaithTracks));
        Row prodsRow = new Row();
        board.setProductions(prodsRow);
        NetworkDevelopmentCard card = getSimpleCardShop().getPurchasedCard().map(DevelopmentCardAsset::getDevelopmentCard).orElseThrow();
        prodsRow.addElem(Option.noNumber(DrawableDevCard.fromDevCardAsset(card)));
        List<ResourceAsset> costs = card.getCostList().stream().flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                .sorted(Comparator.comparing(ResourceAsset::getResourceNumber)).collect(Collectors.toList());
        board.setResChoiceRow(new ResChoiceRow(0,costs,new ArrayList<>()));
        PersonalBoardBody.initializeBuy(board);
        board.setMessage("Select the resources to buy the card, only working for warehouse for now");
        getCLIView().setBody(board);
        getCLIView().show();
    }

    /**
     * Gets called when the player needs to place the development card on his personal board
     */
    @Override
    public void choosePositionForCard() {

    }
}
