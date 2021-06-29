package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
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

    public static Map<Integer, List<Pair<ResourceAsset, Boolean>>> getSimpleWarehouseLeadersDepots(GameModel gameModel, int playerRequestingUpdate) {

        Map<Integer, List<Pair<Resource, Boolean>>> warehouse = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getSimpleWarehouseLeadersDepots();

        return warehouse.keySet().stream()
                .collect(Collectors.toMap(
                        integer -> integer,
                        integer -> warehouse.get(integer).stream()
                                .map(resource -> new Pair<>(ResourceAsset.fromInt(resource.getKey().getResourceNumber()), resource.getValue()))
                                .collect(Collectors.toList())
                ));
    }

    public static Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>> getSimpleStrongBox(GameModel gameModel) {

        //     Pos           Res        number   selected
        Map<Integer, Pair<Integer, Pair<Integer, Integer>>> box = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleStrongBox();


        return box.keySet().stream().collect(Collectors.toMap(

                position -> position,
                position -> new Pair<>(
                        ResourceAsset.fromInt(box.get(position).getKey()), new Pair<>(
                        box.get(position).getValue().getKey(), box.get(position).getValue().getValue()))
        ));
    }

    public static Map<Integer, Pair<ResourceAsset, Integer>> getSimpleDiscardBox(GameModel gameModel) {

        Map<Integer, Pair<Integer, Integer>> box = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleDiscardBox();

        return box.keySet().stream().collect(Collectors.toMap(
                position -> position,
                position -> new Pair<>(
                        ResourceAsset.fromInt(box.get(position).getKey()),
                        box.get(position).getValue()
                )
        ));
    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInWarehouseAtPos(GameModel gameModel, int playerRequestingUpdate) {

        int warehouseDepotSpaces = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots().getNumOfCellsInAllDepots();
        PersonalBoard playerPersonalBoard = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard();

        List<Integer> positions = IntStream.range(0, warehouseDepotSpaces).boxed().collect(Collectors.toList());

        Map<Integer, List<Integer>> availablePositions = positions.stream().map(position -> getAvailableMovingPositionsForResourceAtPos(playerPersonalBoard, position,
                playerPersonalBoard.getWarehouseLeadersDepots().getResourceAt(position)))
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));

        return availablePositions;

    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInDiscardBoxAtPos(GameModel gameModel) {

        PersonalBoard playerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        List<Integer> resourcesPositions = IntStream.range(-4, 0).boxed().collect(Collectors.toList());

        Map<Integer, List<Integer>> availablePositions = resourcesPositions.stream().map(
                position -> getAvailableMovingPositionsForResourceAtPos(playerPersonalBoard, position,
                        playerPersonalBoard.getDiscardBox().getResourceAt(position)))
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));

        return availablePositions;

    }

    public static Pair<Integer, List<Integer>> getAvailableMovingPositionsForResourceAtPos(PersonalBoard board, int position, Resource res) {

        List<Integer> positions = board.getWarehouseLeadersDepots().availableMovingPositionsForResource(res).boxed().collect(Collectors.toList());
        if (!res.equals(Resource.EMPTY) && !positions.isEmpty())
            positions.add(position);  //selected resource can be deselected
        return new Pair<>(position, positions);

    }

    public static boolean isDiscardBoxDiscardable(GameModel gameModel) {

        PersonalBoard board = gameModel.getCurrentPlayer().getPersonalBoard();
        Box discardBox = board.getDiscardBox();
        List<Integer> positions = IntStream.range(-4, 0).boxed().collect(Collectors.toList());

        return positions.stream().noneMatch(position ->
                board.getWarehouseLeadersDepots().availableMovingPositionsForResource(discardBox.getResourceAt(position)).findAny().isPresent());

    }

    //    leaderDepotSpot   resourceType
    public static Map<Integer, ResourceAsset> getResourcesTypesOfLeaderDepots(GameModel gameModel, int playerRequestingUpdate) {

        WarehouseLeadersDepots currentDepots = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots();

        int numberOfDepots = currentDepots.getNumOfCellsInAllDepots();
        if (numberOfDepots > 6) {
            Map<Integer, ResourceAsset> resourcesTypes = IntStream.range(6, numberOfDepots).boxed().collect(Collectors.toMap(

                    spotPosition -> spotPosition,
                    spotPosition -> {

                        int resourceIntValue = currentDepots.getLeaderDepotAtPosResourceType(spotPosition);

                        return ResourceAsset.fromInt(resourceIntValue);
                    }
            ));
            return resourcesTypes;
        } else return new HashMap<>();

    }

    //                    position                        numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableWarehousePositionsForDevCardPurchase(GameModel gameModel) {

        WarehouseLeadersDepots warehouseLeadersDepots = gameModel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots();
        Map<Integer, Integer> devCardCostMap = CardShopMessageBuilder.costMapOfPurchasedCardWithDiscounts(gameModel);
        return getSelectableWarehousePositions(warehouseLeadersDepots, devCardCostMap);

    }

    //                     position                      numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableWarehousePositionsForProduction(GameModel gameModel) {

        PersonalBoard playerBoard = gameModel.getCurrentPlayer().getPersonalBoard();
        int lastSelectedProduction = playerBoard.getLastSelectedProductionPosition();
        Production production = playerBoard.getProductionFromPosition(lastSelectedProduction).get();
        Map<Integer, Integer> productionInputsMap = production.getInputsMap();
        return getSelectableWarehousePositions(playerBoard.getWarehouseLeadersDepots(), productionInputsMap);

    }

    //                    position                       numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableStrongBoxPositionsForDevCardPurchase(GameModel gameModel) {

        Map<Integer, Integer> devCardCostMap = CardShopMessageBuilder.costMapOfPurchasedCardWithDiscounts(gameModel);
        Box strongBox = gameModel.getCurrentPlayer().getPersonalBoard().getStrongBox();

        return getSelectableStrongBoxPositions(strongBox, devCardCostMap);

    }

    //                      position                     numOfRes  isSelectable
    public static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableStrongBoxPositionsForProduction(GameModel gameModel) {

        PersonalBoard playerBoard = gameModel.getCurrentPlayer().getPersonalBoard();
        int lastSelectedProduction = playerBoard.getLastSelectedProductionPosition();

        Production production = playerBoard.getProductionFromPosition(lastSelectedProduction).get();

        Map<Integer, Integer> productionInputsMap = production.getInputsMap();

        return getSelectableStrongBoxPositions(playerBoard.getStrongBox(), productionInputsMap);

    }

    //                       position                         numOfRes   isSelectable
    private static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableWarehousePositions(WarehouseLeadersDepots warehouseLeadersDepots, Map<Integer, Integer> resourcesToChoose) {


        int warehouseCells = warehouseLeadersDepots.getNumOfCellsInAllDepots();

        Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> map = IntStream.range(0, warehouseCells).boxed().collect(Collectors.toMap(

                position -> {

                    ResourceAsset resourceAtPost = ResourceAsset.fromInt(warehouseLeadersDepots.getResourceAt(position).getResourceNumber());

                    return new Pair<>(position, resourceAtPost);
                },

                position -> {

                    Resource resourceAtPos = warehouseLeadersDepots.getResourceAt(position);

                    int resourceNumber = resourceAtPos.getResourceNumber();

                    int numOfResource = resourceAtPos.equals(Resource.EMPTY) ? 0 : 1;

                    boolean isSelectable = false;

                    if(!resourceAtPos.equals(Resource.EMPTY) && !warehouseLeadersDepots.getSelected(resourceAtPos.getResourceNumber())) {

                        if (resourcesToChoose.containsKey(Resource.TO_CHOOSE.getResourceNumber())
                                && resourcesToChoose.get(Resource.TO_CHOOSE.getResourceNumber())>0)
                            isSelectable = true;

                        else
                            isSelectable = resourcesToChoose.get(resourceNumber) > 0 && !warehouseLeadersDepots.getSelected(resourceNumber);

                    }
                    return new MutablePair<>(numOfResource, isSelectable);

                }
        ));

        return map;

    }

    //                      position                          numOfRes  isSelectable
    private static Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> getSelectableStrongBoxPositions(Box strongBox, Map<Integer, Integer> resourcesToChoose) {

        int strongBoxCells = strongBox.getNumOfResourcesTypes();

        Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> map = IntStream.range(0, strongBoxCells).boxed().collect(Collectors.toMap(

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

        return map;

    }


}

