package it.polimi.ingsw.server.model.player.board;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A deposit composed of cells in which each cell can contain one Resource or be empty.
 */
public abstract class Depot{

    /**
     * A pair of Resource and boolean that indicates if the resource is selected for the next action, meaning that it will be used in the next action
     */
    private List<Pair<Resource,Boolean>> res_sel;
    /**
     * the global position of the first cell of the depot
     */
    private int globalPositionOfFirstElement;
    /**
     * number of occupied cells
     */
    private int numberOfOccupiedSpots;
    /**
     * Type of the resource that this depot can contain
     */
    protected Resource resourceType;


    public Depot(){}
    /**
     * Creates a depot with parameters
     * @param length the size of the depot, must be greater than zero
     * @param globalPositionOfFirstElement Resources in the depot have a global position to make it easier to move them between depots
     * @param resourceType the newly created depot will only accept Resources of this resourceType, can be Resource.EMPTY
     */
    public Depot(int length, int globalPositionOfFirstElement, Resource resourceType){
        res_sel = Stream.generate(()-> new Pair<>(Resource.EMPTY,false)).limit(length).collect(Collectors.toList());
        this.resourceType = resourceType;
        this.globalPositionOfFirstElement = globalPositionOfFirstElement;
    }

    /**
     * Used to get the size of the depot
     * @return the size of the depot
     */
    int getSize(){
        return res_sel.size();
    }


    /**
     * Sets the resourceType of the resource stored in depot to the given resourceType
     * @param resourceType resourceType of the Resources that can be stored in the depot
     */
    void setResourceType(Resource resourceType){
        this.resourceType = resourceType;
    }

    /**
     * Used to get the global position of the last Resource
     * @return the global Position of the last cell in the depot
     */
    int getLastGlobalPosition(){
        return globalPositionFromLocal(getSize()-1);
    }

    /**
     * Used to get the number of occupied cells in the depot
     * @return number of occupied cells
     */
    int getOccupiedSpotsInDepotNum(){
        return numberOfOccupiedSpots;
    }

    /**
     * Converts a local position of a resource to it's global position
     * @param localPosition the local position of the resource
     * @return the global position of the resource
     */
    private int globalPositionFromLocal(int localPosition){
        return globalPositionOfFirstElement+localPosition;
    }

    /**
     * Converts a global position of a resource to a local position
     * @param globalPosition the global position of the resource
     * @return the local position of the resource
     */
    private int globalToLocalPos(int globalPosition) {
        int localPosition = globalPosition - globalPositionOfFirstElement;
        try {
            if (localPosition>= getSize())
                throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e){
            System.out.println("The given global position is not in the depot");
            System.out.println("GlobalPosition: "+globalPosition);
            System.out.println("Depot Range: "+globalPositionOfFirstElement+"->"+getLastGlobalPosition());
            e.printStackTrace();
        }
        return localPosition;
    }

    /**
     * Returns all the positions in this depot where you can move the given resource
     * @param resource the resource of which you want to know the available positions
     * @return an IntStream of the positions this depot where you can move the given resource
     */
    public IntStream availableSpotsFor(Resource resource){
        if (!resource.equals(Resource.EMPTY) && (resourceType.equals(resource) || resourceType.equals(Resource.EMPTY)))
            return IntStream.range(0, getSize()).
                    filter((pos)->
                        !res_sel.get(pos).getValue()&&//isNotSelected
                                res_sel.get(pos).getKey().equals(Resource.EMPTY)).//IsEmpty
                    map(this::globalPositionFromLocal);
        else return IntStream.empty();
    }

    /**
     * Used to get the resource at given global position in the depot
     * @param globalPosition global position of the resource you want to get
     * @return the resource at the given global position
     */
    Pair<Resource,Boolean> getAtGPos(int globalPosition){
        return res_sel.get(globalToLocalPos(globalPosition));
    }

    /**
     * Removes the resource at the given global position
     * @param globalPosition is the position of the resource to remove
     */
    void removeResource(int globalPosition){
        numberOfOccupiedSpots -=1;
        res_sel.set(globalToLocalPos(globalPosition),new Pair<>(Resource.EMPTY,false));
    }

    /**
     * Adds a the given resource in the depot at the given position
     * @param pos_Res a pair of an integer that represents a global position and a resource, the pair represents the position and resource to add
     */
    void addResource(Pair<Integer,Resource> pos_Res) {
        if (getOccupiedSpotsInDepotNum()==0) setResourceType(pos_Res.getValue());
        numberOfOccupiedSpots +=1;
        res_sel.set(globalToLocalPos(pos_Res.getKey()), new Pair<>(pos_Res.getValue(),false));
    }

    /**
     * Returns if the resource at the given global position is selected for the next action
     * @param resGlobalPos the global position of the resource to check whether selected
     * @return if the resource at the given global position is selected
     */
    boolean getSelected(int resGlobalPos){
        return res_sel.get(globalToLocalPos(resGlobalPos)).getValue();
    }

    /**
     * Flags a resource at the given global position as selected or not selected for the next action
     * @param value true if the resource needs to be selected, false elsewhere
     * @param resGlobalPos the global position of the resource that needs to be flagged
     */
    void setSelected(boolean value,int resGlobalPos){
        res_sel.set(globalToLocalPos(resGlobalPos),new Pair<>(getAtGPos(resGlobalPos).getKey(),value));
    }

    /**
     * Toggles the resource at the given global position as flagged for the next action
     * @param resGlobalPos the global position of the resource that needs to be flagged
     */
    void toggleSelected(int resGlobalPos){
        setSelected(!getSelected(resGlobalPos),resGlobalPos);
    }


    /** Returns how many {@link Resource resources} of the given resourceType there are
     * @param type is a {@link Resource}
     * @return number of {@link Resource resources} of the given resourceType in the deposit
     */
    public int getNumberOf(Resource type){
        if (!type.equals(this.resourceType))
            return 0;
        else return getOccupiedSpotsInDepotNum();
    }

    public void changeDepotGlobalPosOfFirstElement(int newGlobalPosOfFirstElement){
        this.globalPositionOfFirstElement = newGlobalPosOfFirstElement;
    }

}