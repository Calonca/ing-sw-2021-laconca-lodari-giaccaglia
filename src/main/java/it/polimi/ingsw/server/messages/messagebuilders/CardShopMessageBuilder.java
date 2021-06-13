package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.Cards;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class CardShopMessageBuilder {

    public static Map<NetworkDevelopmentCardColor, Map<Integer, Pair<Integer, List<DevelopmentCardAsset>>>> cardShopAdapter(GameModel gameModel) {

        Map<DevelopmentCardColor, Map<Integer, Pair<Integer, List<DevelopmentCard>>>> simpleCardShop = gameModel.getSimpleCardShop();

        return simpleCardShop
                .keySet()
                .stream()
                .collect(Collectors.toMap(

                        colorKey -> NetworkDevelopmentCardColor.fromInt(Arrays.asList(DevelopmentCardColor.values()).indexOf(colorKey)),

                        colorKey -> simpleCardShop.get(colorKey)
                                .keySet()
                                .stream()
                                .collect(Collectors.toMap(

                                        intKey -> intKey,

                                        intKey -> {

                                            Pair<Integer, List<DevelopmentCard>> deckPair = simpleCardShop.get(colorKey).get(intKey);

                                            List<DevelopmentCardAsset> netCards = (simpleCardShop
                                                    .get(colorKey)
                                                    .get(intKey).getValue()
                                                    .stream()
                                                    .map(devCard -> Cards.getCardAsset(devCard.getCardId()))
                                                    .filter(Optional::isPresent)
                                                    .map(card -> (new DevelopmentCardAsset((DevelopmentCardAsset) card.get())))
                                                    .collect(Collectors.toList()));

                                            if (netCards.size() > 0) {
                                                DevelopmentCardAsset cardOnTop = netCards.get(0);
                                                DevelopmentCard card = gameModel.getDevCardsMap().get(cardOnTop.getCardId());
                                                boolean isPurchasable = checkCardRequirements(gameModel, card);
                                                cardOnTop.getDevelopmentCard().setSelectable(isPurchasable);
                                            }

                                            int deckSize = deckPair.getKey();

                                            return new Pair<>(deckSize,netCards);
                                        }
                                ))));
    }

    public static DevelopmentCardAsset purchasedCardAdapter(GameModel gameModel) {

        DevelopmentCard purchasedCard = gameModel.getCardShop().getCopyOfPurchasedCard();

        return purchasedCard != null ? (DevelopmentCardAsset) Cards.getCardAsset(purchasedCard.getCardId()).orElse(null) : null;
    }

        //        resourceInt  amount
    public static Map<Integer, Integer> costMapOfPurchasedCardWithDiscounts(GameModel gameModel) {

        Map<Integer, Integer> costList = new HashMap<>();

        if (gameModel.getCardShop().getCopyOfPurchasedCard() != null) {

            List<Integer> discounts = Arrays.stream((gameModel.getCurrentPlayer().getPersonalBoard().getDiscounts())).boxed().collect(Collectors.toList());

            DevelopmentCard purchasedCard = gameModel.getCardShop().getCopyOfPurchasedCard();

            costList = purchasedCard.getCostMap().entrySet().stream().collect(Collectors.toMap(

                    entry ->  entry.getKey().getResourceNumber(),

                    entry -> {

                int resourceIntValue = entry.getKey().getResourceNumber();
                int updatedCost = entry.getValue() - discounts.get(resourceIntValue);

                return updatedCost;

            }));
        }

        return costList;


    }

    private static boolean checkCardRequirements(GameModel gameModel, DevelopmentCard card){
        return gameModel.getCurrentPlayer().getPersonalBoard().areDevCardRequirementsSatisfied(card);

    }
}
