package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.network.assets.CardAssetsContainer;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.market.Marble;
import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class MessagesBuilderHandler {

    private static GameModel gameModel;

    private static Map<UUID, DevelopmentCard> devCards;


    public static void initMessagesBuilderHandler(GameModel currentGameModel, Map<UUID, DevelopmentCard> devCardsMap){
        gameModel = currentGameModel;
        devCards = devCardsMap;
    }


    public static Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> cardShopAdapter (){
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
                                            List<NetworkDevelopmentCard> netCards = (simpleCardShop
                                                    .get(colorKey)
                                                    .get(intKey)
                                                    .stream()
                                                    .map(devCard -> CardAssetsContainer.getCardAsset(devCard.getCardId()))
                                                    .filter(Optional::isPresent)
                                                    .map(card -> (DevelopmentCardAsset) card.get()).map(DevelopmentCardAsset::getDevelopmentCard)
                                                    .collect(Collectors.toList()));

                                            for (NetworkDevelopmentCard netCard : netCards) {
                                                DevelopmentCard card = devCards.get(netCard.getCardId());
                                                boolean isPurchasable = checkCardRequirements(card);
                                                netCard.setPurchasable(isPurchasable);
                                            }
                                                return netCards;
                                        }
                                ))));
    }
    private static boolean checkCardRequirements(DevelopmentCard card){
        return gameModel.getCurrentPlayer().getPersonalBoard().isDevelopmentCardAvailable(card);

    }

    public static UUID[][] marketBoardAdapter(Marble[][] marketMarbles, int marketRows, int marketColumns){
        List<UUID> marbles  = Arrays
                .stream(marketMarbles)
                .flatMap(Arrays::stream)
                .map(marble ->
                        UUID.nameUUIDFromBytes(MarbleAsset.fromInt(Marble.getMarbleNumber(marble)).getName().getBytes(StandardCharsets.UTF_8)))
                .collect(Collectors.toList());

        return IntStream.range(0, marketColumns*marketColumns)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(groupingBy((e)->e.getKey()% marketRows))
                .values()
                .stream()
                .map(MessagesBuilderHandler::pairToValue)
                .toArray(UUID[][]::new);
    }
    private static UUID[] pairToValue(List<Pair<Integer, UUID>> pos_marArray){
        return pos_marArray.stream().map(Pair::getValue).toArray(UUID[]::new);
    }

}
