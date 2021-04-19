package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;


/**
 * Interface to facilitate moving {@link Resource resources} between storage units
 */
interface StorageUnit {

    /**
     * Removes the {@link Resource resource} at the given global position from the {@link StorageUnit}
     * @param globalPos an int representing the position
     */
    void removeResource(int globalPos);

    /**
     * Adds the given {@link Resource resource} to the {@link StorageUnit} at the given global position
     * @param gPos_res a {@link Pair} of global position and {@link Resource}
     */
    void addResource(Pair<Integer, Resource> gPos_res);


    /**
     * Returns the {@link Resource resource} at the given global position in the {@link StorageUnit}
     * @param globalPos the position of the resource to get
     * @return The {@link Resource resource} at the given global position in the {@link StorageUnit}
     */
    Resource getResourceAt(int globalPos);


    /**
     * Flags the {@link Resource resource} at the given global position in the {@link StorageUnit} as selected for the next action.
     * For example a production or buying a card from the CardShop.
     * @param globalPos the global position of the {@link Resource resource} that needs to be selected for production
     */
    void selectResourceAt(int globalPos);

    /**
     * Flags the {@link Resource resource} at the given global position in the {@link StorageUnit} as deselected for the next action.
     * For example a production or buying a card from the CardShop.
     * @param globalPos the global position of the {@link Resource resource} that needs to be deselected for production
     */
    void deselectResourceAt(int globalPos);

    /** Returns how many {@link Resource resources} flagged for the next action there are in the {@link StorageUnit}
     * @return Number of selected {@link Resource resource}
     */
    int getTotalSelected();

    /**
     * Removes the selected {@link Resource resources} from the {@link StorageUnit}
     */
    void removeSelected();


    ///**
    // * Returns if the {@link Resource resource} at the given global position in the {@link StorageUnit} is selected for production
    // * @param globalPos the global position of the {@link Resource resource}
    // * @return if the {@link Resource resource} at the given global position in the {@link StorageUnit} is selected for production
    // */
    //boolean getResSelectedAt(int globalPos);
}
