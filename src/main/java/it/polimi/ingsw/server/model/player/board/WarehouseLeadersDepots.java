package it.polimi.ingsw.server.model.player.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Contains both warehouse and leader depots,
 * uses global position to address each cell in both leader and warehouse depots as a single entity
 */
public class WarehouseLeadersDepots {
    /**
     * List of warehouse depot followed by leader depots in order of activation
     */
    private final List<Depot> depots;
    /**
     * In position i there is the number of the depot that contains that global position.
     * Used to get the depot for a given position in O(1) time
     */
    private final List<Integer> depotAtPosition;


    /**
     * Creates a new WarehouseLeadersDepots with the given depots
     * @param depots is a list of depots that the new WarehouseLeadersDepots will have
     */
    WarehouseLeadersDepots(List<Depot> depots){
        this.depots = depots;
        depotAtPosition = IntStream.range(0,depots.size()).
                flatMap((value)-> IntStream.generate(()->value).limit(depots.get(value).getSize()))
                .boxed().collect(Collectors.toList());
    }

    /**
     * Default construct for a game of Maestri del Rinascimento
     */
    WarehouseLeadersDepots(){
        this(Stream.of(
        new WarehouseDepot(1,0),
        new WarehouseDepot(2,1),
        new WarehouseDepot(3,3)).collect(Collectors.toList())
        );
    }

    /**
     * Returns the depot containing the cell with the given global position
     * @param position the global position used to search the depot where that position is present
     * @return the depot containing the cell with the given global position
     */
    private Depot depotThatHasPos(int position){
        return depots.get(depotAtPosition.get(position));
    }

    /**
     * Return the resource at the given global position and whether is selected
     * @param position the global position of the resource to get
     * @return A resource and whether is selected for production
     */
    private Pair<Resource,Boolean> getResourceAndSelectedAt(int position){
        return depotThatHasPos(position).getAtGPos(position);
    }

    /**
     * Return the resource stored is a depot at the given global position
     * @param position the global position of the resource to get
     * @return The resource at the given global position
     */
    private Resource getResourceAt(int position){
        return getResourceAndSelectedAt(position).getKey();
    }


    /**
     * Returns if the resource at the given global position is selected for production
     * @param resGlobalPos the global position of the resource to check whether selected
     * @return if the resource at the given global position is selected for production
     */
    boolean getSelected(int resGlobalPos){
        return depotThatHasPos(resGlobalPos).getSelected(resGlobalPos);
    }

    /**
     * Flags a resource at the given global position as selected or not selected for production
     * @param value true if the resource needs to be selected, false elsewhere
     * @param resGlobalPos the global position of the resource that needs to be flagged for production
     */
    void setSelected(boolean value,int resGlobalPos){
        depotThatHasPos(resGlobalPos).setSelected(value,resGlobalPos);
    }

    /**
     * Toggles the resource at the given global position as flagged for production
     * @param resGlobalPos the global position of the resource that needs to be flagged for production
     */
    void toggleSelected(int resGlobalPos){
        setSelected(!getSelected(resGlobalPos),resGlobalPos);
    }

    /**
     * Removes the resources in all the positions present in given array
     * @param positions an array of global positions.
     *                  The resources at those positions will be removed
     */
    void removeResources(int[] positions){
        Arrays.stream(positions).forEach(
                (pos)->depotThatHasPos(pos).removeResource(pos)
        );
    }

    /**
     * Adds resources to the deposits
     * @param resources A array of pairs.
     *                  Each element of the array represents a resource and the global position where it will be added
     */
    void addResources(Pair<Integer,Resource>[] resources){
        Arrays.stream(resources).forEach((pos_res)->depotThatHasPos(pos_res.getKey()).addResource(pos_res));
    }

    /**
     * Moves a resource from the starting position to the end position
     * @param startPos the starting global position of the resource that will be moved
     * @param endPos the ending global position of the resource that will be moved
     */
    void moveResource(int startPos,int endPos){
        depotThatHasPos(endPos).addResource(
                new Pair<>(endPos,getResourceAt(startPos)));
        depotThatHasPos(startPos).removeResource(startPos);
    }

    /**
     * Returns an array of all the available positions in all the depots where the resource at the given position can be moved
     * @param position the global position of the resource of which available moving positions will be returned
     * @return  an array of all the available global positions in all the depots where the resource at the given global position can be moved
     */
    int[] availableMovingPositionsForResourceAt(int position){
        return depots.stream()
                .flatMapToInt((depot)->depot.availableSpotsFor(getResourceAt(position))).toArray();
    }

    /**
     * Returns a list of available moving positions for all the resources in all the depots
     * @return a list of available moving global positions for all the resources in all the depots
     */
    Map<Integer,int[]> availableMovingPositionsForAllResources(){
        return IntStream.range(0, depots.size())
                .mapToObj((pos)->new Pair<>(pos,availableMovingPositionsForResourceAt(pos)))
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));
    }

    /**
     * Adds a new depot at the end of the list of depots
     * @param depot the depot that will be added to the list
     */
    void addDepot(Depot depot){
        depots.add(depot);
        depotAtPosition.addAll(Collections.nCopies(depot.getSize(),depots.size()));
    }

    /**
     * Returns the number of occupied cells in all the depots
     * @return the number of occupied cells in all the depots
     */
    int getOccupiedSpotsNum(){
        return depots.stream().map(Depot::getOccupiedSpotsInDepotNum).reduce(0,Integer::sum);
    }

    /**
     * Calculates if the given resources are present in the any of the depots
     * @param resources an array in which at position i there is the resource at position i in the resource ordering
     * @return true if the given resources were found, else false
     */
    boolean areThereEnoughResources(int[] resources){
        int totalToFind = Arrays.stream(resources).reduce(0, Integer::sum);
        if (totalToFind>getOccupiedSpotsNum())
            return false;
        for (int c=0;c<depotAtPosition.size();c++){
            if (resources[getResourceAt(c).getResourceNumber()]>0 && totalToFind>0) {
                totalToFind -=1;
                resources[getResourceAt(c).getResourceNumber()]-=1;
            }
        }
        return totalToFind<=0;
    }

    /**
     * Removes all resources that will be used for a production from all the depots
     */
    void  removeSelected(){
        removeResources(IntStream.range(0,depots.size()).filter((pos)->depotThatHasPos(pos).getSelected(pos)).toArray());
    }

    /**
     * Returns the global position of the first cell of the depot that will be added.
     * Useful to initialize a depot
     * @return the global position of the first cell of the depot that will be added
     */
    int getNextGlobalPosition() {
        return depots.get(depots.size()-1).getLastGlobalPosition()+1;
    }
}
