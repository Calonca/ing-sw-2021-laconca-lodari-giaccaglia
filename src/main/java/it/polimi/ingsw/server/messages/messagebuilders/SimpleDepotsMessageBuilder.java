package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.GameModel;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleDepotsMessageBuilder {

    public static Map<Integer, List<ResourceAsset>> getSimpleWarehouseLeadersDepots(GameModel gameModel){

        Map<Integer, List<Integer>> warehouse = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleWarehouseLeadersDepots();

       return warehouse.keySet().stream()
               .collect(Collectors.toMap(
               integer -> integer,
                       integer -> warehouse.get(integer).stream()
                               .map(ResourceAsset::fromInt)
                               .collect(Collectors.toList())
       ));
    }

    public static Map<Integer, Pair<ResourceAsset, Integer>> getSimpleStrongBox(GameModel gameModel){

        Map<Integer, Pair<Integer, Integer>> box = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleStrongBox();

        return box.keySet().stream().collect(Collectors.toMap(
                position -> position,
                position -> new Pair<>(
                ResourceAsset.fromInt(box.get(position).getKey()),
                box.get(position).getValue()
                )
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
}
