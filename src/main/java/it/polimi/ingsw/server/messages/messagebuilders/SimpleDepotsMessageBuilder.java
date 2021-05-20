package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleDepotsMessageBuilder {

    public static Map<Integer, List<ResourceAsset>> getSimpleWarehouseLeadersDepots(Map<Integer, List<Integer>> warehouse){

       return warehouse.keySet().stream()
               .collect(Collectors.toMap(
               integer -> integer,
                       integer -> warehouse.get(integer).stream()
                               .map(ResourceAsset::fromInt)
                               .collect(Collectors.toList())
       ));
    }

    public static Map<ResourceAsset, Integer> getSimpleStrongBox(Map<Integer, Integer> box){

        return box.keySet().stream().collect(Collectors.toMap(
                ResourceAsset::fromInt,
                integer -> integer
        ));
    }
}
