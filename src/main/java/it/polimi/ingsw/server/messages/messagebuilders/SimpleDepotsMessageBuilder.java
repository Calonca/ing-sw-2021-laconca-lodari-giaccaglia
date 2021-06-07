package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleDepotsMessageBuilder {

    public static Map<Integer, List<Pair<ResourceAsset, Boolean>>> getSimpleWarehouseLeadersDepots(GameModel gameModel){

        Map<Integer, List<Pair<Resource, Boolean>>> warehouse = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleWarehouseLeadersDepots();

       return warehouse.keySet().stream()
               .collect(Collectors.toMap(
               integer -> integer,
                       integer -> warehouse.get(integer).stream()
                               .map(resource -> new Pair<>(ResourceAsset.fromInt(resource.getKey().getResourceNumber()), resource.getValue()))
                               .collect(Collectors.toList())
       ));
    }

    public static  Map<Integer, Pair<ResourceAsset, Pair<Integer, Integer>>>  getSimpleStrongBox(GameModel gameModel){

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

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInWarehouseAtPos(GameModel gameModel){

        int warehouseDepotSpaces = gameModel.getCurrentPlayer().getPersonalBoard().getWarehouseLeadersDepots().getNumOfCellsInAllDepots();
        PersonalBoard currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        List<Integer> positions= IntStream.range(0, warehouseDepotSpaces).boxed().collect(Collectors.toList());

        Map<Integer, List<Integer>> availablePositions = positions.stream().map(position -> getAvailableMovingPositionsForResourceAtPos(currentPlayerPersonalBoard, position,
                currentPlayerPersonalBoard.getWarehouseLeadersDepots().getResourceAt(position)))
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));

        return availablePositions;

    }

    public static Map<Integer, List<Integer>> getAvailableMovingPositionsForResourceInDiscardBoxAtPos(GameModel gameModel){

        PersonalBoard currentPlayerPersonalBoard = gameModel.getCurrentPlayer().getPersonalBoard();

        List<Integer> resourcesPositions = IntStream.range(-4, 0).boxed().collect(Collectors.toList());

        Map<Integer, List<Integer>> availablePositions = resourcesPositions.stream().map(
                position -> getAvailableMovingPositionsForResourceAtPos(currentPlayerPersonalBoard, position,
                        currentPlayerPersonalBoard.getDiscardBox().getResourceAt(position)))

                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));

        return availablePositions;

    }

    public static Pair<Integer, List<Integer>> getAvailableMovingPositionsForResourceAtPos(PersonalBoard board, int position, Resource res){
        List<Integer> positions = board.getWarehouseLeadersDepots().availableMovingPositionsForResource(res).boxed().collect(Collectors.toList());
        if(position>=0)
            positions.add(position);

        return new Pair<>(position, positions);

    }


    public static boolean isDiscardBoxDiscardable(GameModel gameModel){

        PersonalBoard board = gameModel.getCurrentPlayer().getPersonalBoard();
        Box discardBox = board.getDiscardBox();
        List<Integer> positions = IntStream.range(-4, -1).boxed().collect(Collectors.toList());

        return positions.stream().noneMatch(position ->
                board.getWarehouseLeadersDepots().availableMovingPositionsForResource(discardBox.getResourceAt(position)).findAny().isPresent());

    }

}
