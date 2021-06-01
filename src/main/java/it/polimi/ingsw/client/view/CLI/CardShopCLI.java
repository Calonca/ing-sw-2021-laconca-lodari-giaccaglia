package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.GridBody;
import it.polimi.ingsw.client.view.CLI.layout.Grid;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.VerticalList;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

public class CardShopCLI extends CardShopViewBuilder {

    /**
     * Adds view elements to the CLIView
     */
    @Override
    public void run() {
        getCLIView().setTitle("Select a card to buy");
        Grid grid = new Grid();
        grid.setShowNumbers(true);

        for (int color=0;color<NetworkDevelopmentCardColor.values().length-1;color++) {
            VerticalList verTest = new VerticalList(DrawableDevCard.height());
            for (int i = 3; i >= 1; i--) {
                DevelopmentCardAsset netCard = getSimpleCardShop().getCardFront(NetworkDevelopmentCardColor.fromInt(color), i).orElse(null);
                int finalI = i;
                int finalColor = color;
                verTest.addOption(Option.from(DrawableDevCard.fromDevCardAsset(netCard), () -> {
                    sendChosenCard(finalColor, finalI);
                }));
            }
            grid.addColumn(verTest);
        }
        GridBody gridBody = new GridBody(grid);


        getCLIView().setBody(gridBody);
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
