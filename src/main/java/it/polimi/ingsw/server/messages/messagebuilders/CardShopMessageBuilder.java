package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.simplemodel.Cards;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CardShopMessageBuilder {

    public static Map<NetworkDevelopmentCardColor, Map<Integer, List<DevelopmentCardAsset>>> cardShopAdapter (GameModel gameModel){

        Map<DevelopmentCardColor, Map<Integer, List<DevelopmentCard>>> simpleCardShop = gameModel.getSimpleCardShop();

        return simpleCardShop
                .keySet()
                .stream()
                .collect(Collectors.toMap(

                        colorKey -> NetworkDevelopmentCardColor.fromInt(Arrays.asList(DevelopmentCardColor.values()).indexOf(colorKey)) ,

                        colorKey -> simpleCardShop.get(colorKey)
                                .keySet()
                                .stream()
                                .collect(Collectors.toMap(

                                        intKey -> intKey,

                                        intKey -> {
                                            List<DevelopmentCardAsset> netCards = (simpleCardShop
                                                    .get(colorKey)
                                                    .get(intKey)
                                                    .stream()
                                                    .map(devCard -> Cards.getCardAsset(devCard.getCardId()))
                                                    .filter(Optional::isPresent)
                                                    .map(card -> (new DevelopmentCardAsset ( (DevelopmentCardAsset) card.get())))
                                                    .collect(Collectors.toList()));

                                            for (DevelopmentCardAsset netCard : netCards) {
                                                DevelopmentCard card = gameModel.getDevCardsMap().get(netCard.getCardId());
                                                boolean isPurchasable = checkCardRequirements(gameModel, card);
                                                netCard.getDevelopmentCard().setSelectable(isPurchasable);
                                            }
                                            return netCards;
                                        }
                                ))));
    }

    public static DevelopmentCardAsset purchasedCardAdapter(GameModel gameModel){

        DevelopmentCard purchasedCard = gameModel.getCardShop().getCopyOfPurchasedCard();

        return purchasedCard!= null ? (DevelopmentCardAsset) Cards.getCardAsset(purchasedCard.getCardId()).orElse(null) : null;
    }

    private static boolean checkCardRequirements(GameModel gameModel, DevelopmentCard card){
        return gameModel.getCurrentPlayer().getPersonalBoard().isDevelopmentCardAvailable(card);

    }
}
