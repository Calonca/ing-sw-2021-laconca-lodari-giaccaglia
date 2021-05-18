package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

import java.util.List;
import java.util.Map;

public class SHOWING_CARD_SHOP extends StateInNetwork{

    private Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> simpleCardShop;

    public SHOWING_CARD_SHOP(){
    }

    public SHOWING_CARD_SHOP(int player, Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> simpleCardShop){
        super(player);
        this.simpleCardShop = simpleCardShop;
    }

    public Map<NetworkDevelopmentCardColor, Map<Integer, List<NetworkDevelopmentCard>>> getSimpleCardShop() {
        return simpleCardShop;
    }
}



