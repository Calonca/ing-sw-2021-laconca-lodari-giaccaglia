package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static it.polimi.ingsw.network.jsonUtility.serialize;

/**
 * Contains both warehouse and leader depots,
 * uses global position to address each cell in both leader and warehouse depots as a single entity
 */
public class WarehouseLeadersDepots implements StorageUnit {
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
     * Default construct for a game of Masters of Renaissance
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
     * @return A resource and whether is selected
     */
    private Pair<Resource,Boolean> getResourceAndSelectedAt(int position){
        return depotThatHasPos(position).getAtGPos(position);
    }

    /**
     * Return the resource stored is a depot at the given global position
     * @param globalPos the global position of the resource to get
     * @return The resource at the given global position
     */
    @Override
    public Resource getResourceAt(int globalPos){
        return getResourceAndSelectedAt(globalPos).getKey();
    }


    /**
     * Returns if the resource at the given global position is selected for the next action
     * @param resGlobalPos the global position of the resource to check whether selected
     * @return if the resource at the given global position is selected
     */
    boolean getSelected(int resGlobalPos){
        return depotThatHasPos(resGlobalPos).getSelected(resGlobalPos);
    }

    /**
     * Flags the {@link Resource resource} at the given global position in the {@link StorageUnit} as selected for the next action.
     * For example a production or buying a card from the CardShop.
     * @param globalPos the global position of the {@link Resource resource} that needs to be selected for production
     */
    @Override
    public void selectResourceAt(int globalPos) {
        depotThatHasPos(globalPos).setSelected(true,globalPos);
    }

    /**
     * Flags the {@link Resource resource} at the given global position in the {@link StorageUnit} as deselected for the next action.
     * For example a production or buying a card from the CardShop.
     *
     * @param globalPos the global position of the {@link Resource resource} that needs to be deselected for production
     */
    @Override
    public void deselectResourceAt(int globalPos) {
        depotThatHasPos(globalPos).setSelected(false,globalPos);
    }

    /**
     * Removes the {@link Resource resource} at the given global position from the right {@link Depot}
     *
     * @param globalPos an int representing the global position
     */
    @Override
    public void removeResource(int globalPos) {
        depotThatHasPos(globalPos).removeResource(globalPos);
    }

    /**
     * Adds the given {@link Resource resource} to the right {@link Depot} at the given global position
     *
     * @param gPos_res a {@link Pair} of global position and {@link Resource}
     */
    @Override
    public void addResource(Pair<Integer, Resource> gPos_res) {
        depotThatHasPos(gPos_res.getKey()).addResource(gPos_res);
    }

    /**
     * Removes the resources in all the positions present in given array
     * @param positions an array of global positions.
     *                  The resources at those positions will be removed
     */
    void removeResources(int[] positions){
        Arrays.stream(positions).forEach(this::removeResource);
    }


    /**
     * Adds resources to the deposits
     * @param resources A array of pairs.
     *                  Each element of the array represents a resource and the global position where it will be added
     */
    void addResources(Pair<Integer,Resource>[] resources){
        Arrays.stream(resources).forEach(this::addResource);
    }

    /**
     * Returns all the available positions in all the depots where the resource at the given position can be moved
     * @param position the global position of the resource of which available moving positions will be returned
     * @return an IntStream of global positions
     */
    private IntStream availableMovingPositionsForResourceAt(int position){
        return availableMovingPositionsForResource(getResourceAt(position));
    }

    /**
     * Returns all the available positions in the depots where the given resource can be moved
     */
    public IntStream availableMovingPositionsForResource(Resource resource){
        return depots.stream()
                .flatMapToInt((depot)->depot.availableSpotsFor(resource));
    }

    /**
     * Returns a list of available moving positions for all the resources in all the depots
     * @return a list of available moving global positions for all the resources in all the depots
     */
    Map<Integer,int[]> availableMovingPositionsForAllResources(){
        return IntStream.range(0, depotAtPosition.size())
                .mapToObj((pos)->new Pair<>(pos,availableMovingPositionsForResourceAt(pos).toArray()))
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue
                ));
    }

    /**
     * Json serialization for all available positions
     * @return a string with this format,
     * {"0":[],"1":[],"2":[0,1,3,4,5],"3":[],"4":[],"5":[],"6":[0,3,4,5,7],"7":[]}
     */
    String allAvbPosToJson(){
        return serialize(availableMovingPositionsForAllResources());
    }

    /**
     * Json serialization of all the data contained in all the depots in an organized way
     * @return a string with this format,
     * {"0":[{key:STONE,value:false}],
     * "1":[{key:EMPTY,value:false},{key:STONE,value:false}],
     * "2":[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:GOLD,value:false}],
     * "3":[{key:EMPTY,value:false},{key:GOLD,value:false}]}
     */
    public String structuredTableJson(){
        Map<Integer, List<Pair<Integer, Pair<Resource, Boolean>>>> a = IntStream.range(0, depotAtPosition.size())
                .mapToObj((pos)->new Pair<>(pos,getResourceAndSelectedAt(pos)))
                .collect(Collectors.groupingBy(
                        (p)->depotAtPosition.get(p.getKey())
                )
                );
        Map<Integer, List<Pair<Resource, Boolean>>> b = a.entrySet().stream().map((entry)->
        {
            List<Pair<Resource, Boolean>> test = entry.getValue().stream().map(Pair::getValue).collect(Collectors.toList());
            return new Pair<>(entry.getKey(),test);
        }).collect(Collectors.toMap(Pair::getKey,Pair::getValue));

        return serialize(b);
    }

    /**
     * Adds a new depot at the end of the list of depots
     * @param depot the depot that will be added to the list
     */
    public void addDepot(Depot depot){
        depotAtPosition.addAll(Collections.nCopies(depot.getSize(),depots.size()));
        depots.add(depot);
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
    public boolean enoughResourcesForProductions(int[] resources){
        int totalToFind = Arrays.stream(resources).reduce(0, Integer::sum);
        if (totalToFind>getOccupiedSpotsNum())
            return false;
        for (int c=0;c<depotAtPosition.size();c++){
            if (!getResourceAt(c).equals(Resource.EMPTY) && resources[getResourceAt(c).getResourceNumber()]>0 && totalToFind>0) {
                totalToFind -=1;
                resources[getResourceAt(c).getResourceNumber()]-=1;
            }
        }
        return totalToFind<=0;
    }

    /**
     * Removes the selected {@link Resource resources} from the {@link StorageUnit}
     */
    @Override
    public void  removeSelected(){
        removeResources(IntStream.range(0,depotAtPosition.size()).filter((pos)->depotThatHasPos(pos).getSelected(pos)).toArray());
    }

    /**
     * Returns the global position of the first cell of the depot that will be added.
     * Useful to initialize a depot
     * @return the global position of the first cell of the depot that will be added
     */
    int getNextGlobalPosition() {
        return depots.get(depots.size()-1).getLastGlobalPosition()+1;
    }


    /** Returns how many {@link Resource resources} of the given type there are
     * @param type is a {@link Resource}
     * @return number of {@link Resource resources} of the given type in the deposit
     */
    public int getNumberOf(Resource type){
        return depots.stream().mapToInt((dep)->dep.getNumberOf(type)).reduce(0, Integer::sum);
    }

    /** Returns how many {@link Resource resources} flagged for the next action there are in the {@link StorageUnit}
     * @return Number of selected {@link Resource resource}
     */
    @Override
    public int getTotalSelected(){
        return (int) IntStream.range(0,depotAtPosition.size()).filter((pos)->depotThatHasPos(pos).getSelected(pos)).count();
    }

}
