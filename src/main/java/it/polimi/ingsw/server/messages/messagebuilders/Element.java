package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.model.GameModel;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum Element {

    //----------common elements----------//

    SimpleCardShop(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new SimpleCardShop(CardShopMessageBuilder.cardShopAdapter(gameModel));
        }


    },

    SimpleMarketBoard(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new SimpleMarketBoard(
                    MarketBoardMessageBuilder.marketBoardAdapter(gameModel.getMarketMarbles(), gameModel.getMarketBoardRows(), gameModel.getMarketBoardColumns()),
                    gameModel.getMarketBoardColumns(),
                    gameModel.getMarketBoardRows(),
                    UUID.nameUUIDFromBytes(gameModel.getSlideMarble().toString().getBytes(StandardCharsets.UTF_8)));
        }
    },

    //--------player elements-----------//

    SimplePlayerLeaders(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new SimplePlayerLeaders(gameModel.getCurrentPlayer().getLeadersUUIDs());
        }
    },

    TestElem(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel model) {
            return new TestElem();
        }
    },

    SimpleFaithTrack(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            it.polimi.ingsw.network.simplemodel.SimpleFaithTrack track = new SimpleFaithTrack();
            return track.faithTrackConstructor(gameModel.getCurrentPlayer().getSerializedFaithTrack());
        }

    },

    SimpleStrongBox(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new SimpleCardShop(CardShopMessageBuilder.cardShopAdapter(gameModel));
        }
    },

    SimpleWareHouseLeadersDepot(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new SimpleWarehouseLeadersDepot(
                    SimpleDepotsMessageBuilder.getSimpleWarehouseLeadersDepots(
                            gameModel.getCurrentPlayer().getPersonalBoard().getSimpleWarehouseLeadersDepots()));
        }
    },

    SimpleCardCells(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new SimpleCardShop(CardShopMessageBuilder.cardShopAdapter(gameModel));
        }
    };

    private final boolean isCommonElement;

    private static List<Element> elements = Arrays.asList(Element.values());

    Element(final Boolean isCommonElement) {
        this.isCommonElement = isCommonElement;
    }

    public abstract SimpleModelElement buildSimpleModelElement(GameModel model);

    public static List<Element> getAsList(){
        return elements;
    }

    public boolean isCommonElement(){
        return isCommonElement;
    }

}
