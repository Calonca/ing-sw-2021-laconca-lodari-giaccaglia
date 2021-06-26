package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
            int LorenzoPosition = gameModel.isSinglePlayer() ? gameModel.getSinglePlayer().getLorenzoPosition() : -1;
            return new PlayersInfo(GameInfoMessageBuilder.getSimplePlayerInfoMap(gameModel), LorenzoPosition);
        }
    },

    VaticanReportInfo(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new VaticanReportInfo(
                    GameInfoMessageBuilder.getPlayersTriggeringVaticanReport(gameModel),
                    GameInfoMessageBuilder.getPopeTileStateMap(gameModel));
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

        private static final List<Element> allPlayerElements = elements.stream().filter(element -> !element.isCommonElement()).collect(Collectors.toList());

        private static final List<Element> allCommonElements = elements.stream().filter(Element::isCommonElement).collect(Collectors.toList());



        Element(final Boolean isCommonElement) {
            this.isCommonElement = isCommonElement;
        }

        public abstract SimpleModelElement buildSimpleModelElement(GameModel model, int playerRequestingUpdate);

        public static List<Element> getAsList() {
            return elements;
        }

        public static List<Element> getAllPlayerElementsAsList(){
            return allPlayerElements;
        }

        public static List<Element> getAllCommonElementsAsList(){
            return allCommonElements;
        }

        public static List<SimpleModelElement> buildPlayerSimpleModelElements(GameModel gameModel, List<Element> elementsToUpdate, int playerRequestingUpdate){

            List<SimpleModelElement> playerSimpleModelElements = elementsToUpdate
                    .stream()
                    .filter(element -> !element.isCommonElement())
                    .map(element -> element.buildSimpleModelElement(gameModel, playerRequestingUpdate))
                    .collect(Collectors.toList());

            return playerSimpleModelElements;
        }

        public static List<SimpleModelElement> buildCommonSimpleModelElements(GameModel gameModel, List<Element> elementsToUpdate, int playerRequestingUpdate){

        List<SimpleModelElement> commonSimpleModelElements = elementsToUpdate
                .stream()
                .filter(Element::isCommonElement)
                .map(element -> element.buildSimpleModelElement(gameModel, playerRequestingUpdate))
                .collect(Collectors.toList());

        return commonSimpleModelElements;
    }

        public boolean isCommonElement() {
            return isCommonElement;
        }


}
