package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

public class WarehouseDepot extends Depot {


    /**
     * Creates a depot of the given dimension and given global position
     * NetworkResource.Empty means that it will change when adding resources to the type of the added resource
     * @param length number of cells in the depot
     * @param globalPositionOfFirstElement global position of the first element of the depot
     */
    WarehouseDepot(int length, int globalPositionOfFirstElement) {
        super(length, globalPositionOfFirstElement, Resource.EMPTY);
    }

    /**
     * Removes the resource at the given global position
     * and sets the type of depot to empty
     * @param globalPosition is the position of the resource to remove
     */
    @Override
    void removeResource(int globalPosition) {
        super.removeResource(globalPosition);
        if (getOccupiedSpotsInDepotNum()==0) setType(Resource.EMPTY);
    }
}
