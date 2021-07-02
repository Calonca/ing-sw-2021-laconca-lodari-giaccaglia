package it.polimi.ingsw.client.view.CLI.commonViews;

import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableDevCard;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.CardShopViewBuilder;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;


public class CLICardShop{

    public void buildCardShop(Column grid, boolean isViewing){

        for (int i = 3; i >= 1; i--) {
            Row verTest = new Row();
            for (int color = 0; color< NetworkDevelopmentCardColor.values().length-1; color++) {

                final NetworkDevelopmentCardColor cardColor = NetworkDevelopmentCardColor.fromInt(color);
                NetworkDevelopmentCard netCard = CardShopViewBuilder.getSimpleCardShop()
                        .getCardFront(cardColor, i)
                        .map(DevelopmentCardAsset::getDevelopmentCard)
                        .orElse(null);
                int stackHeight = CardShopViewBuilder.getSimpleCardShop().getStackHeight(cardColor,i);

                Runnable runOnOption;

                if(!isViewing) {
                    int finalI = i;
                    runOnOption = () -> {
                    CardShopViewBuilder.sendChosenCard(cardColor.ordinal(), finalI);
                    };
                }
                else
                    runOnOption = () -> {};

                Option o = Option.from(DrawableDevCard.fromDevCardAsset(netCard,stackHeight), runOnOption);
                o.setEnabled(netCard != null && netCard.isSelectable());
                verTest.addElem(o);
            }
            grid.addElem(verTest);
        }
    }


}
