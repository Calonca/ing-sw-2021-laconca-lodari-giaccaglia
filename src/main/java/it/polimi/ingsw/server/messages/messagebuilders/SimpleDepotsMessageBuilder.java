package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.player.board.WarehouseLeadersDepots;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleDepotsMessageBuilder {

    private SimpleDepotsMessageBuilder(){}

    public static Map<Integer, List<Pair<ResourceAsset, Boolean>>> getSimpleWarehouseLeadersDepots(GameModel gameModel, int playerRequestingUpdate) {

        if (gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            Map<Integer, List<Pair<Resource, Boolean>>> warehouse = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getSimpleWarehouseLeadersDepots();

            return warehouse.keySet().stream()
                    .collect(Collectors.toMap(
                            integer -> integer ,
                            integer -> warehouse.get(integer).stream()
                                    .map(resource -> new Pair<>(ResourceAsset.fromInt(resource.getKey().getResourceNumber()) , resource.getValue()))
                                    .collect(Collectors.toList())
                    ));
        }
        else return new HashMap<>();
    }

    public static Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> getSimpleStrongBox(GameModel gameModel, int playerRequestingUpdate) {

        if(gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            Player player = gameModel.getPlayer(playerRequestingUpdate).get();
            //     Pos           Res        number   selected
            Map<Integer, Pair<Integer, Pair<Integer, Integer>>> box = player.getPersonalBoard().getSimpleStrongBox();


            return box.keySet().stream().collect(Collectors.toMap(

                    position -> position ,
                    position -> new Pair<>(
                            ResourceAsset.fromInt(box.get(position).getKey()) , new Pair<>(
                            box.get(position).getValue().getKey() , box.get(position).getValue().getValue()))
            ));
        }
        else return new HashMap<>();
    }

    public static Map<Integer, Pair<ResourceAsset, Integer>> getSimpleDiscardBox(GameModel gameModel, int playerRequestingUpdate) {

        if(gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            Player player = gameModel.getPlayer(playerRequestingUpdate).get();

            Map<Integer, Pair<Integer, Integer>> box = player.getPersonalBoard().getSimpleDiscardBox();

            return box.keySet().stream().collect(Collectors.toMap(
                    position -> position ,
                    position -> new Pair<>(
                            ResourceAsset.fromInt(box.get(position).getKey()) ,
                            box.get(position).getValue()
                    )
            ));
        }
        else return new HashMap<>();
    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInWarehouseAtPos(GameModel gameModel, int playerRequestingUpdate) {

        if( gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            int warehouseDepotSpaces = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots().getNumOfCellsInAllDepots();
            PersonalBoard playerPersonalBoard = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard();

            List<Integer> positions = IntStream.range(0 , warehouseDepotSpaces).boxed().collect(Collectors.toList());

            return positions.stream().map(position -> getAvailableMovingPositionsForResourceAtPos(playerPersonalBoard , position ,
                    playerPersonalBoard.getWarehouseLeadersDepots().getResourceAt(position)))
                    .collect(Collectors.toMap(
                            Pair::getKey ,
                            Pair::getValue
                    ));
        }
        else return new HashMap<>();

    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInDiscardBoxAtPos(GameModel gameModel, int playerRequesting) {

        if( gameModel.getPlayer(playerRequesting).isPresent()) {

            PersonalBoard playerPersonalBoard = gameModel.getPlayer(playerRequesting).get().getPersonalBoard();

            List<Integer> resourcesPositions = IntStream.range(-4 , 0).boxed().collect(Collectors.toList());

            return resourcesPositions.stream().map(
                    position -> getAvailableMovingPositionsForResourceAtPos(playerPersonalBoard , position ,
                            playerPersonalBoard.getDiscardBox().getResourceAt(position)))
                    .collect(Collectors.toMap(
                            Pair::getKey ,
                            Pair::getValue
                    ));
        }
        else return new HashMap<>();

    }

    public static Pair<Integer, List<Integer>> getAvailableMovingPositionsForResourceAtPos(PersonalBoard board, int position, Resource res) {

        List<Integer> positions = board.getWarehouseLeadersDepots().availableMovingPositionsForResource(res).boxed().collect(Collectors.toList());
        if (!res.equals(Resource.EMPTY) && !positions.isEmpty())
            positions.add(position);  //selected resource can be deselected
        return new Pair<>(position, positions);

    }

    public static boolean isDiscardBoxDiscardable(GameModel gameModel, int playerRequestingUpdate) {

        if (gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            PersonalBoard board = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard();
            Box discardBox = board.getDiscardBox();
            List<Integer> positions = IntStream.range(-4 , 0).boxed().collect(Collectors.toList());

            return positions.stream().noneMatch(position ->
                    board.getWarehouseLeadersDepots().availableMovingPositionsForResource(discardBox.getResourceAt(position)).findAny().isPresent());

        }
        else return false;
    }

    //    leaderDepotSpot   resourceType
    public static Map<Integer, ResourceAsset> getResourcesTypesOfLeaderDepots(GameModel gameModel, int playerRequestingUpdate) {

        if(gameModel.getPlayer(playerRequestingUpdate).isPresent()) {

            WarehouseLeadersDepots currentDepots = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots();

            int numberOfDepots = currentDepots.getNumOfCellsInAllDepots();
            if (numberOfDepots > 6) {
                return IntStream.range(6 , numberOfDepots).boxed().collect(Collectors.toMap(

                        spotPosition -> spotPosition ,
                        spotPosition -> {

                            int resourceIntValue = currentDepots.getLeaderDepotAtPosResourceType(spotPosition);

                            return ResourceAsset.fromInt(resourceIntValue);
                        }
                ));
            } else return new HashMap<>();

        }

        else return new HashMap<>();

    }

    //                    position                        numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableWarehousePositionsForDevCardPurchase(GameModel gameModel, int playerRequestingUpdate) {

        if(gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            WarehouseLeadersDepots warehouseLeadersDepots = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots();
            Map<Integer, Integer> devCardCostMap = CardShopMessageBuilder.costMapOfPurchasedCardWithDiscounts(gameModel , playerRequestingUpdate);
            return getSelectableWarehousePositions(warehouseLeadersDepots , devCardCostMap);
        }
        else return new HashMap<>();
    }

    //                     position                      numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableWarehousePositionsForProduction(GameModel gameModel, int playerRequestingUpdate) {

        if (gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            PersonalBoard playerBoard = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard();
            int lastSelectedProduction = playerBoard.getLastSelectedProductionPosition();
            Production production;
            if (playerBoard.getProductionFromPosition(lastSelectedProduction).isPresent())
                production = playerBoard.getProductionFromPosition(lastSelectedProduction).get();
            else
                return new HashMap<>();

            Map<Integer, Integer> productionInputsMap = production.getInputsMap();
            return getSelectableWarehousePositions(playerBoard.getWarehouseLeadersDepots() , productionInputsMap);

        }
        else return new HashMap<>();
    }

    //                    position                       numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableStrongBoxPositionsForDevCardPurchase(GameModel gameModel, int playerRequestingUpdate) {

        if(gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            Map<Integer, Integer> devCardCostMap = CardShopMessageBuilder.costMapOfPurchasedCardWithDiscounts(gameModel , playerRequestingUpdate);
            Box strongBox = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getStrongBox();

            return getSelectableStrongBoxPositions(strongBox , devCardCostMap);
        }
        else return new HashMap<>();

    }

    //                      position                     numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableStrongBoxPositionsForProduction(GameModel gameModel, int playerRequestingUpdate) {

        if(gameModel.getPlayer(playerRequestingUpdate).isPresent()) {
            PersonalBoard playerBoard = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard();
            int lastSelectedProduction = playerBoard.getLastSelectedProductionPosition();

            if (playerBoard.getProductionFromPosition(lastSelectedProduction).isEmpty())
                return new HashMap<>();

            Production production = playerBoard.getProductionFromPosition(lastSelectedProduction).get();

            Map<Integer, Integer> productionInputsMap = production.getInputsMap();

            return getSelectableStrongBoxPositions(playerBoard.getStrongBox() , productionInputsMap);
        }
        else return new HashMap<>();

    }

    //                       position                         numOfRes   isSelectable
    private static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableWarehousePositions(WarehouseLeadersDepots warehouseLeadersDepots, Map<Integer, Integer> resourcesToChoose) {


        int warehouseCells = warehouseLeadersDepots.getNumOfCellsInAllDepots();

        return IntStream.range(0, warehouseCells).boxed().collect(Collectors.toMap(

                position -> {

                    ResourceAsset resourceAtPost = ResourceAsset.fromInt(warehouseLeadersDepots.getResourceAt(position).getResourceNumber());

                    return new Pair<>(position, resourceAtPost);
                },

                position -> {

                    Resource resourceAtPos = warehouseLeadersDepots.getResourceAt(position);

                    int resourceNumber = resourceAtPos.getResourceNumber();

                    int numOfResource = resourceAtPos.equals(Resource.EMPTY) ? 0 : 1;

                    boolean isSelectable = false;

                    if(!resourceAtPos.equals(Resource.EMPTY) && !warehouseLeadersDepots.getSelected(position)) {

                        if (resourcesToChoose.containsKey(Resource.TO_CHOOSE.getResourceNumber())
                                && resourcesToChoose.get(Resource.TO_CHOOSE.getResourceNumber())>0)
                            isSelectable = true;

                        else
                            isSelectable = resourcesToChoose.get(resourceNumber) > 0 && !warehouseLeadersDepots.getSelected(position);

                    }
                    return new MutablePair<>(numOfResource, isSelectable);

                }
        ));

    }

    //                      position                          numOfRes  isSelectable
    private static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableStrongBoxPositions(Box strongBox, Map<Integer, Integer> resourcesToChoose) {

        int strongBoxCells = strongBox.getNumOfResourcesTypes();

        return IntStream.range(0, strongBoxCells).boxed().collect(Collectors.toMap(

                localPos -> {

                    int globalPos = strongBox.localToGlobalPos(localPos);
                    ResourceAsset resourceAtPost = ResourceAsset.fromInt(strongBox.getResourceAt(globalPos).getResourceNumber());

                    return new Pair<>(globalPos, resourceAtPost);

                },
                localPos -> {

                    int globalPos = strongBox.localToGlobalPos(localPos);
                    Resource resourceAtPos = strongBox.getResourceAt(globalPos);

                    boolean isSelectable = false;

                    if(!resourceAtPos.equals(Resource.EMPTY)) {

                        if (resourcesToChoose.containsKey(Resource.TO_CHOOSE.getResourceNumber())
                                && resourcesToChoose.get(Resource.TO_CHOOSE.getResourceNumber())>0)
                            isSelectable = true;

                        else {

                            isSelectable = resourcesToChoose.get(resourceAtPos.getResourceNumber()) > 0 && strongBox.getNumberOfNotSelected(resourceAtPos) > 0;

                        }

                    }

                    int numOfResources = strongBox.getNumberOf(resourceAtPos);


                    return new MutablePair<>(numOfResources, isSelectable);

                }
        ));

    }


}

