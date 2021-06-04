package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Column;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.OptionList;
import it.polimi.ingsw.client.view.CLI.layout.Row;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

import java.util.Optional;

public class CardShopCLI extends CardShopViewBuilder {


    @Override
    public void run() {
        getCLIView().setTitle("Select a card to buy");

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
        getCLIView().show();

    }

    /**
     * Get called when choosing resources to buy the card
     */
    @Override
    public void choseResources() {

    }

    /**
     * Gets called when the player needs to place the development card on his personal board
     */
    @Override
    public void choosePositionForCard() {

    }
}
