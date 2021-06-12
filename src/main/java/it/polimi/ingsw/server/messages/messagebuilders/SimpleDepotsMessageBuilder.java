package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.player.board.WarehouseLeadersDepots;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleDepotsMessageBuilder {

    public static Map<Integer, List<Pair<ResourceAsset, Boolean>>> getSimpleWarehouseLeadersDepots(GameModel gameModel, int playerRequestingUpdate){

        Map<Integer, List<Pair<Resource, Boolean>>> warehouse = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getSimpleWarehouseLeadersDepots();

       return warehouse.keySet().stream()
               .collect(Collectors.toMap(
               integer -> integer,
                       integer -> warehouse.get(integer).stream()
                               .map(resource -> new Pair<>(ResourceAsset.fromInt(resource.getKey().getResourceNumber()), resource.getValue()))
                               .collect(Collectors.toList())
       ));
    }

    public static  Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>  getSimpleStrongBox(GameModel gameModel){

        //     Pos           Res        number   selected
        Map<Integer, Pair<Integer, Pair<Integer, Integer>> >box = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleStrongBox();



        return box.keySet().stream().collect(Collectors.toMap(

                position -> position,
                position -> new Pair<>(
                ResourceAsset.fromInt(box.get(position).getKey()), new Pair<>(
                box.get(position).getValue().getKey(), box.get(position).getValue().getValue()))
        ));
    }

    public static Map<Integer, Pair<ResourceAsset, Integer>> getSimpleDiscardBox(GameModel gameModel){

        Map<Integer, Pair<Integer, Integer>> box = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleDiscardBox();

        return box.keySet().stream().collect(Collectors.toMap(
                position -> position,
                position -> new Pair<>(
                        ResourceAsset.fromInt(box.get(position).getKey()),
                        box.get(position).getValue()
                )
        ));
    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInWarehouseAtPos(GameModel gameModel, int playerRequestingUpdate){

        int warehouseDepotSpaces = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots().getNumOfCellsInAllDepots();
        PersonalBoard playerPersonalBoard = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard();

        List<Integer> positions= IntStream.range(0, warehouseDepotSpaces).boxed().collect(Collectors.toList());

        Map<Integer, List<Integer>> availablePositions = positions.stream().map(position -> getAvailableMovingPositionsForResourceAtPos(playerPersonalBoard, position,
                playerPersonalBoard.getWarehouseLeadersDepots().getResourceAt(position)))
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));

        return availablePositions;

    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInDiscardBoxAtPos(GameModel gameModel){

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

    public static Pair<Integer, List<Integer>> getAvailableMovingPositionsForResourceAtPos(PersonalBoard board, int position, Resource res){
        List<Integer> positions = board.getWarehouseLeadersDepots().availableMovingPositionsForResource(res).boxed().collect(Collectors.toList());
        if(!res.equals(Resource.EMPTY))
            positions.add(position);  //selected resource can be deselected

        return new Pair<>(position, positions);

    }

    public static boolean isDiscardBoxDiscardable(GameModel gameModel){

        PersonalBoard board = gameModel.getCurrentPlayer().getPersonalBoard();
        Box discardBox = board.getDiscardBox();
        List<Integer> positions = IntStream.range(-4, -1).boxed().collect(Collectors.toList());

        return positions.stream().noneMatch(position ->
                board.getWarehouseLeadersDepots().availableMovingPositionsForResource(discardBox.getResourceAt(position)).findAny().isPresent());

    }

        //    leaderDepotSpot   resourceType
    public static Map<Integer, ResourceAsset> getResourcesTypesOfLeaderDepots(GameModel gameModel, int playerRequestingUpdate){

        WarehouseLeadersDepots currentDepots = gameModel.getPlayer(playerRequestingUpdate).get().getPersonalBoard().getWarehouseLeadersDepots();

        int numberOfDepots = currentDepots.getNumOfCellsInAllDepots();
        if(numberOfDepots>6) {

            Map<Integer, ResourceAsset> resourcesTypes = IntStream.range(6, numberOfDepots).boxed().collect(Collectors.toMap(

                    spotPosition -> spotPosition,
                    spotPosition -> {

                        int resourceIntValue = currentDepots.getLeaderDepotAtPosResourceType(spotPosition);

                        return ResourceAsset.fromInt(resourceIntValue);
                    }
            ));

            return resourcesTypes;
        }

        else return new HashMap<>();

    }

}
