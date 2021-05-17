package it.polimi.ingsw.server.messages;

import it.polimi.ingsw.network.assets.CardAssetsContainer;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MessagesBuilderHandler {

    private static GameModel gameModel;

    public void InitMessagesBuilderHandler(GameModel currentGameModel){
        gameModel = currentGameModel;
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

                                        intKey -> simpleCardShop
                                                .get(colorKey)
                                                .get(intKey)
                                                .stream()
                                                .map(devCard -> CardAssetsContainer.getCardAsset(devCard.getCardId()))
                                                .filter(Optional::isPresent)
                                                .map(card -> (DevelopmentCardAsset)card.get()).map(DevelopmentCardAsset::getDevelopmentCard)
                                                .collect(Collectors.toList())
                                ))));
    }

}
