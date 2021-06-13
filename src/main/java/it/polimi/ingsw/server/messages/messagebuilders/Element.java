package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

import java.nio.charset.StandardCharsets;
import java.util.*;

public enum Element {

    //----------common elements----------//

    SimpleCardShop(true) {
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate) {
            return new SimpleCardShop(
                    CardShopMessageBuilder.cardShopAdapter(gameModel),
                    CardShopMessageBuilder.purchasedCardAdapter(gameModel));
        }

    },

    SimpleMarketBoard(true) {
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate) {
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
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new EndGameInfo(
                    GameInfoMessageBuilder.endGameInfoMap(gameModel),
                    GameInfoMessageBuilder.getPlayersEndingTheGame(gameModel), gameModel.getThisMatch().getReasonOfGameEnd());
        }
    },

    PlayersInfo(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new PlayersInfo(GameInfoMessageBuilder.getSimplePlayerInfoMap(gameModel));
        }
    },

    //--------player elements-----------//

        SimplePlayerLeaders(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate) {
                return new SimplePlayerLeaders(PlayerLeadersMessageBuilder.playerLeadersMap(gameModel));
            }
        },

        SimpleFaithTrack(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                it.polimi.ingsw.network.simplemodel.SimpleFaithTrack track = new SimpleFaithTrack();
                return track.faithTrackConstructor(gameModel.getCurrentPlayer().getSerializedFaithTrack());
            }

        },

        SimpleStrongBox(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleStrongBox(SimpleDepotsMessageBuilder.getSimpleStrongBox(gameModel));
            }
        },

        SimpleDiscardBox(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleDiscardBox(

                        SimpleDepotsMessageBuilder.getSimpleDiscardBox(gameModel),
                        SimpleDepotsMessageBuilder.getAvailableMovingPositionsForResourceInDiscardBoxAtPos(gameModel) ,
                        SimpleDepotsMessageBuilder.isDiscardBoxDiscardable(gameModel));

            }
        },

        SimpleWareHouseLeadersDepot(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleWarehouseLeadersDepot(

                        SimpleDepotsMessageBuilder.getSimpleWarehouseLeadersDepots(gameModel, playerRequestingUpdate),
                        SimpleDepotsMessageBuilder.getAvailableMovingPositionsForResourceInWarehouseAtPos(gameModel, playerRequestingUpdate),
                        SimpleDepotsMessageBuilder.getResourcesTypesOfLeaderDepots(gameModel, playerRequestingUpdate));

            }
        },

        SimpleCardCells(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleCardCells(
                        SimpleCardsCellsMessageBuilder.cardCellsAdapter(gameModel),
                        gameModel.getCurrentPlayer().getPersonalBoard().getAvailableSpotsForDevCard(gameModel.getCardShop().getCopyOfPurchasedCard())
                );
            }
        },


        ActiveLeaderInfo(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new ActiveLeaderBonusInfo(

                        GameInfoMessageBuilder.depotBonusResources(gameModel),
                        GameInfoMessageBuilder.discountBonusResources(gameModel),
                        GameInfoMessageBuilder.marketBonusResources(gameModel),
                        GameInfoMessageBuilder.productionBonusResources(gameModel));
            }
        },

        SimpleProductions(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new SimpleProductions(

                    SimpleProductionsMessageBuilder.simpleProductionsMap(gameModel),
                    gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getLastSelectedProductionPosition());

           }
        },

        SimpleSoloActionToken(false){

        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new SimpleSoloActionToken(

                    UUID.nameUUIDFromBytes(gameModel.showLastActivatedSoloActionToken().name().getBytes(StandardCharsets.UTF_8))
            );
        }
        },

        SelectablePositions(false){

        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                State currentPlayerState = gameModel.getCurrentPlayer().getCurrentState();

                if(currentPlayerState.equals(State.CHOOSING_RESOURCE_FOR_PRODUCTION))
                    return new SelectablePositions(

                            SimpleDepotsMessageBuilder.getSelectableWarehousePositionsForProduction(gameModel),
                            SimpleDepotsMessageBuilder.getSelectableStrongBoxPositionsForProduction(gameModel)

                    );

                else if(currentPlayerState.equals(State.CHOOSING_RESOURCES_FOR_DEVCARD))
                    return new SelectablePositions(

                            SimpleDepotsMessageBuilder.getSelectableWarehousePositionsForDevCardPurchase(gameModel),
                            SimpleDepotsMessageBuilder.getSelectableStrongBoxPositionsForDevCardPurchase(gameModel)
                    );

                return new SelectablePositions();
        }
        };

        private final boolean isCommonElement;

        private static final List<Element> elements = Arrays.asList(Element.values());

        Element(final Boolean isCommonElement) {
            this.isCommonElement = isCommonElement;
        }

        public abstract SimpleModelElement buildSimpleModelElement(GameModel model, int playerRequestingUpdate);

        public static List<Element> getAsList() {
            return elements;
        }

        public boolean isCommonElement() {
            return isCommonElement;
        }

}
