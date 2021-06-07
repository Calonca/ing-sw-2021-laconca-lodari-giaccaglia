package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.model.GameModel;

import java.nio.charset.StandardCharsets;
import java.util.*;

public enum Element {

    //----------common elements----------//

    SimpleCardShop(true) {
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel) {
            return new SimpleCardShop(CardShopMessageBuilder.cardShopAdapter(gameModel), CardShopMessageBuilder.purchasedCardAdapter(gameModel));
        }

    },

    SimpleMarketBoard(true) {
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel) {
            return new SimpleMarketBoard(
                    MarketBoardMessageBuilder.marketBoardAdapter(gameModel),
                    gameModel.getMarketBoardColumns(),
                    gameModel.getMarketBoardRows(),
                    UUID.nameUUIDFromBytes(gameModel.getSlideMarble().toString().getBytes(StandardCharsets.UTF_8)),
                    MarketBoardMessageBuilder.pickedMarblesAdapter(gameModel),
                    gameModel.getFaithPointsFromMarketPickedMarbles(),
                    gameModel.getNumberOfWhiteMarblesInPickedLine());
        }
    },

    EndGameInfo(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
            return new EndGameInfo(
                    GameInfoMessageBuilder.endGameInfoMap(gameModel),
                    GameInfoMessageBuilder.getPlayersEndingTheGame(gameModel));
        }
    },

    //--------player elements-----------//

        SimplePlayerLeaders(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel) {
                return new SimplePlayerLeaders(PlayerLeadersMessageBuilder.playerLeadersMap(gameModel));
            }
        },

        SimpleFaithTrack(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
                it.polimi.ingsw.network.simplemodel.SimpleFaithTrack track = new SimpleFaithTrack();
                return track.faithTrackConstructor(gameModel.getCurrentPlayer().getSerializedFaithTrack());
            }

        },

        SimpleStrongBox(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
                return new SimpleStrongBox(SimpleDepotsMessageBuilder.getSimpleStrongBox(gameModel));
            }
        },

        SimpleDiscardBox(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
                return new SimpleDiscardBox(

                        SimpleDepotsMessageBuilder.getSimpleDiscardBox(gameModel),
                        SimpleDepotsMessageBuilder.getAvailableMovingPositionsForResourceInDiscardBoxAtPos(gameModel) ,
                        SimpleDepotsMessageBuilder.isDiscardBoxDiscardable(gameModel));

            }
        },

        SimpleWareHouseLeadersDepot(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
                return new SimpleWarehouseLeadersDepot(

                        SimpleDepotsMessageBuilder.getSimpleWarehouseLeadersDepots(gameModel),
                        SimpleDepotsMessageBuilder.getAvailableMovingPositionsForResourceInWarehouseAtPos(gameModel),
                        SimpleDepotsMessageBuilder.getResourcesTypesOfLeaderDepots(gameModel));

            }
        },

        SimpleCardCells(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
                return new SimpleCardCells(

                        SimpleCardsCellsMessageBuilder.cardCellsAdapter(gameModel),
                        gameModel.getCurrentPlayer().getPersonalBoard().getAvailableSpotsForDevCard(gameModel.getCardShop().getCopyOfPurchasedCard())
                );
            }
        },


        ActiveLeaderInfo(false) {

            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel){
                return new ActiveLeaderBonusInfo(

                        GameInfoMessageBuilder.depotBonusResources(gameModel),
                        GameInfoMessageBuilder.discountBonusResources(gameModel),
                        GameInfoMessageBuilder.marketBonusResources(gameModel),
                        GameInfoMessageBuilder.productionBonusResources(gameModel));
            }
        };

        private final boolean isCommonElement;

        private static final List<Element> elements = Arrays.asList(Element.values());

        Element(final Boolean isCommonElement) {
            this.isCommonElement = isCommonElement;
        }

        public abstract SimpleModelElement buildSimpleModelElement(GameModel model);

        public static List<Element> getAsList() {
            return elements;
        }

        public boolean isCommonElement() {
            return isCommonElement;
        }

}
