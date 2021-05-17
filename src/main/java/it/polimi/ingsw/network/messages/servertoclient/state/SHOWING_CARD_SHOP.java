package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.server.messages.MessagesBuilderHandler;

import java.util.List;
import java.util.Map;

public class SHOWING_CARD_SHOP {

    private Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> simpleCardShop;

    public SHOWING_CARD_SHOP(){
        simpleCardShop = MessagesBuilderHandler.cardShopAdapter();
    }




}
