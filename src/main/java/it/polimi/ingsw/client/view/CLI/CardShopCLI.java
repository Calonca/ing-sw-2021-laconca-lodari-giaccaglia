package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;

public class CardShopCLI extends CardShopViewBuilder {

    /**
     * Adds view elements to the CLIView
     */
    @Override
    public void run() {
        //getCLIView().setTitle("Select a card to buy");
        //Grid grid = new Grid();
        //grid.setShowNumbers(true);
        //
        //for (int color=0;color<NetworkDevelopmentCardColor.values().length-1;color++) {
        //    Column verTest = new Column(DrawableDevCard.height());
        //    for (int i = 3; i >= 1; i--) {
        //        NetworkDevelopmentCard netCard = getSimpleCardShop().getCardOnFront(NetworkDevelopmentCardColor.fromInt(color), i).orElse(null);
        //        int finalI = i;
        //        int finalColor = color;
        //        verTest.addOption(Option.from(DrawableDevCard.fromDevCardAsset(netCard), () -> {
        //            sendChosenCard(finalColor, finalI);
        //        }));
        //    }
        //    grid.addColumn(verTest);
        //}
        //GridBody gridBody = new GridBody(grid);
        //
        //
        //getCLIView().setBody(gridBody);
        //getCLIView().show();

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
