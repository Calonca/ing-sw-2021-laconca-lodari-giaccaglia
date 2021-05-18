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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class MessagesBuilderHandler {

    private static GameModel gameModel;

    private static Map<UUID, DevelopmentCard> devCards;
    private static Marble[][] market;
    private static int marketRows;
    private static int marketColumns;

    public void initMessagesBuilderHandler(GameModel currentGameModel, Map<UUID, DevelopmentCard> devCardsMap){
        gameModel = currentGameModel;
        devCards = devCardsMap;
    }

    public void initMessagesBuilderHandler(GameModel currentGameModel, Marble[][] marketMarbles, int rows, int columns){
        gameModel = currentGameModel;
        market = marketMarbles;
        marketRows = rows;
        marketColumns = columns;
    }

    public static Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> cardShopAdapter (){


        Map<DevelopmentCardColor, Map<Integer, List<DevelopmentCard>>> simpleCardShop = gameModel.getCardShop().getSimpleCardShop();

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

    public static MarbleAsset[][] marketBoardAdapter(){
        List<MarbleAsset> marbles  = Arrays
                .stream(market)
                .flatMap(Arrays::stream).map(marble -> MarbleAsset.fromInt(Marble.getMarbleNumber(marble))).collect(Collectors.toList());

        return IntStream.range(0, marketColumns*marketColumns)
                .mapToObj((pos)->new Pair<>(pos,marbles.get(pos)))
                .collect(groupingBy((e)->e.getKey()% marketRows))
                .values()
                .stream()
                .map(MessagesBuilderHandler::pairToValue)
                .toArray(MarbleAsset[][]::new);
    }
    private static MarbleAsset[] pairToValue(List<Pair<Integer, MarbleAsset>> pos_marArray){
        return pos_marArray.stream().map(Pair::getValue).toArray(MarbleAsset[]::new);
    }


}
