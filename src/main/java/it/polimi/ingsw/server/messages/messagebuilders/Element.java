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

    SIMPLE_CARD_SHOP(true) {
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate) {
            return new SimpleCardShop(
                    CardShopMessageBuilder.cardShopAdapter(gameModel),
                    CardShopMessageBuilder.purchasedCardAdapter(gameModel));
        }

    },

    SIMPLE_MARKET_BOARD(true) {
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

    END_GAME_INFO(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new EndGameInfo(
                    GameInfoMessageBuilder.getPlayersEndingTheGame(gameModel), gameModel.getThisMatch().getReasonOfGameEnd(),
                    GameInfoMessageBuilder.getMatchOutcomeMap(gameModel));
        }
    },

    PLAYERS_INFO(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            int lorenzoPosition = gameModel.isSinglePlayer() ? gameModel.getSinglePlayer().getLorenzoPosition() : -1;
            return new PlayersInfo(GameInfoMessageBuilder.getSimplePlayerInfoMap(gameModel), lorenzoPosition);
        }
    },

    VATICAN_REPORT_INFO(true){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new VaticanReportInfo(
                    GameInfoMessageBuilder.getPlayersTriggeringVaticanReport(gameModel),
                    GameInfoMessageBuilder.getPopeTileStateMap(gameModel));
        }
    },

    //--------player elements-----------//

        SIMPLE_PLAYER_LEADERS(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate) {
                return new SimplePlayerLeaders(PlayerLeadersMessageBuilder.playerLeadersMap(gameModel, playerRequestingUpdate));
            }
        },

        SIMPLE_FAITH_TRACK(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                it.polimi.ingsw.network.simplemodel.SimpleFaithTrack track = new SimpleFaithTrack();
                return track.faithTrackConstructor(gameModel.getPlayer(playerRequestingUpdate).get().getSerializedFaithTrack());
            }

        },

        SIMPLE_STRONG_BOX(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleStrongBox(SimpleDepotsMessageBuilder.getSimpleStrongBox(gameModel, playerRequestingUpdate));
            }
        },

        SIMPLE_DISCARD_BOX(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleDiscardBox(

                        SimpleDepotsMessageBuilder.getSimpleDiscardBox(gameModel, playerRequestingUpdate),
                        SimpleDepotsMessageBuilder.getAvailableMovingPositionsForResourceInDiscardBoxAtPos(gameModel, playerRequestingUpdate) ,
                        SimpleDepotsMessageBuilder.isDiscardBoxDiscardable(gameModel, playerRequestingUpdate));

            }
        },

        SIMPLE_WARE_HOUSE_LEADERS_DEPOT(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleWarehouseLeadersDepot(

                        SimpleDepotsMessageBuilder.getSimpleWarehouseLeadersDepots(gameModel, playerRequestingUpdate),
                        SimpleDepotsMessageBuilder.getAvailableMovingPositionsForResourceInWarehouseAtPos(gameModel, playerRequestingUpdate),
                        SimpleDepotsMessageBuilder.getResourcesTypesOfLeaderDepots(gameModel, playerRequestingUpdate));

            }
        },

        SIMPLE_CARD_CELLS(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new SimpleCardCells(
                        SimpleCardsCellsMessageBuilder.cardCellsAdapter(gameModel, playerRequestingUpdate),
                        gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getAvailableSpotsForDevCard(gameModel.getCardShop().getCopyOfPurchasedCard())
                );
            }
        },


        ACTIVE_LEADER_INFO(false) {
            @Override
            public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                return new ActiveLeaderBonusInfo(

                        GameInfoMessageBuilder.depotBonusResources(gameModel, playerRequestingUpdate),
                        GameInfoMessageBuilder.discountBonusResources(gameModel, playerRequestingUpdate),
                        GameInfoMessageBuilder.marketBonusResources(gameModel, playerRequestingUpdate),
                        GameInfoMessageBuilder.productionBonusResources(gameModel, playerRequestingUpdate));
            }
        },

        SIMPLE_PRODUCTIONS(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new SimpleProductions(

                    SimpleProductionsMessageBuilder.simpleProductionsMap(gameModel, playerRequestingUpdate),
                    gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getLastSelectedProductionPosition());

           }
        },

        SIMPLE_SOLO_ACTION_TOKEN(false){

        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
            return new SimpleSoloActionToken(gameModel.getLastActivatedSoloActionToken().name());
        }
        },

        SELECTABLE_POSITIONS(false){
        @Override
        public SimpleModelElement buildSimpleModelElement(GameModel gameModel, int playerRequestingUpdate){
                State currentPlayerState = gameModel.getCurrentPlayer().getCurrentState();

                if(currentPlayerState.equals(State.CHOOSING_RESOURCE_FOR_PRODUCTION))
                    return new SelectablePositions(

                            SimpleDepotsMessageBuilder.getSelectableWarehousePositionsForProduction(gameModel, playerRequestingUpdate),
                            SimpleDepotsMessageBuilder.getSelectableStrongBoxPositionsForProduction(gameModel, playerRequestingUpdate)
                    );

                else if(currentPlayerState.equals(State.CHOOSING_RESOURCES_FOR_DEVCARD))
                    return new SelectablePositions(

                            SimpleDepotsMessageBuilder.getSelectableWarehousePositionsForDevCardPurchase(gameModel, playerRequestingUpdate),
                            SimpleDepotsMessageBuilder.getSelectableStrongBoxPositionsForDevCardPurchase(gameModel, playerRequestingUpdate)
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

            return elementsToUpdate
                    .stream()
                    .filter(element -> !element.isCommonElement())
                    .map(element -> element.buildSimpleModelElement(gameModel, playerRequestingUpdate))
                    .collect(Collectors.toList());
        }

        public static List<SimpleModelElement> buildCommonSimpleModelElements(GameModel gameModel, List<Element> elementsToUpdate, int playerRequestingUpdate){

            return elementsToUpdate
                    .stream()
                    .filter(Element::isCommonElement)
                    .map(element -> element.buildSimpleModelElement(gameModel, playerRequestingUpdate))
                    .collect(Collectors.toList());
    }

        public boolean isCommonElement() {
            return isCommonElement;
        }


}
