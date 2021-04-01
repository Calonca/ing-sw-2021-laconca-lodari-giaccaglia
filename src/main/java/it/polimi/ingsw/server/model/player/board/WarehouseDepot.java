package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

public class WarehouseDepot extends Depot {


    /**
     * Creates a depot of the given dimension and given global position
     * Resource.Empty means that it will change when adding resources to the type of the added resource
     * @param length number of cells in the depot
     * @param globalPositionOfFirstElement global position of the first element of the depot
     */
    WarehouseDepot(int length, int globalPositionOfFirstElement) {
        super(length, globalPositionOfFirstElement, Resource.EMPTY);
    }

    /**
     * Adds a the given resource in the depot at the given position.
     * When empty it will change the type of resource that the depot can store to the resource to add.
     * @param pos_Res a pair of an integer that represents a global position and a resource,
     * the pair represents the position and resource to add
     */
    @Override
    void addResource(Pair<Integer, Resource> pos_Res) {
        super.addResource(pos_Res);
        if (getOccupiedSpotsInDepotNum()==0) setType(pos_Res.getValue());
    }

}
