package it.polimi.ingsw.server.model.player.board;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.stream.IntStream;

import static java.lang.Integer.max;
import static java.lang.Math.min;


/**
 * Represents a storage for resources
 * In a position in all the arrays of the class is stored data pertaining to the resource at the same position in the enum ordering
 */
public class Box implements StorageUnit {
    /**
     * Keeps track of how many resources of type i are stored, where i is the index
     */
    private int[] nResAtPos;
    /**
     * Keeps track of how many resources of type i are selected, where i is the index
     * selected means that the player wants to use the resource with that ordering in the resource enum for the next action
     */
    private int[] nSel;
    /**
     * The number of different resourced that this deposit can contain
     */
    private final int length;
    /**
     * The position of the first element in the global positioning system
     */
    private final int globalPosition;

    /**
     * Use this construct is you want to create a deposit that can contain kindsOfRes different kinds of resources
     * @param kindsOfRes how many kinds of resources you want to have
     */
    Box(int kindsOfRes, int globalPosition){
        nResAtPos = IntStream.generate(()->0).limit(kindsOfRes).toArray();
        nSel = IntStream.generate(()->0).limit(kindsOfRes).toArray();
        this.length = kindsOfRes;
        this.globalPosition = globalPosition;
    }

    /**
     * Returns a deposit for four Resources
     * @return  a deposit for four Resources
     */
    public static Box strongBox(){
        return new Box(4,-8);
    }

    /**
     * Returns a deposit for Resources.
     * Only the first four will be indexed
     * @return  a deposit for Resources
     */
    public static Box discardBox(){
        return new Box(5,-4);
    }


    /**
     * Removes resources from the deposit
     * @param toRemove is an array containing the resources to remove represented using the convention for the arrays in this class
     */
    public void removeResources(int[] toRemove){
        IntStream.range(0, toRemove.length).forEach(i->nResAtPos[i]=max(0,nResAtPos[i]-toRemove[i]));
    }

    /**
     * Adds resources to the deposit
     * @param toAdd is an array containing the resources to add represented using the convention for the arrays in this class
     */
    public void addResources(int[] toAdd){
        IntStream.range(0, toAdd.length).forEach(i->nResAtPos[i]+=toAdd[i]);
    }


    /**
     * Removes the {@link Resource resource} at the given global position from the {@link StorageUnit}
     *
     * @param globalPos an int representing the global position
     */
    @Override
    public void removeResource(int globalPos) {
        nResAtPos[globalPos-globalPosition]-=1;
    }

    /**
     * Adds the given {@link Resource resource} to the {@link StorageUnit} at the given global position
     *
     * @param gPosRes a {@link Pair} of global position and {@link Resource}
     */
    @Override
    public void addResource(Pair<Integer, Resource> gPosRes) {
        nResAtPos[gPosRes.getKey()-globalPosition]+=1;
    }

    /**
     * Returns the {@link Resource resource} at the given global position in the {@link Box} or Resource.EMPTY if there are no resources of that type
     * @param globalPos the global position of the resource to get
     * @return The {@link Resource resource} at the given global position in the {@link Box}
     */
    @Override
    public Resource getResourceAt(int globalPos) {
        int localPos = globalPos-globalPosition;
        if (localPos>=nResAtPos.length || nResAtPos[localPos]==0)
            return Resource.EMPTY;
        else
            return Resource.fromInt(localPos);
    }

    /**
     * Returns the global position of a {@link Resource resource} in the depot
     * @param res a "physical" resource
     * @return a global position
     */
    public int globalPositionOfRes(Resource res){return globalPosition+res.getResourceNumber();}

    public int localToGlobalPos(int localPos){
        return localPos+globalPosition;
    }



    /** Returns how many {@link Resource resources} flagged for the next action there are in the {@link StorageUnit}
     * @return Number of selected {@link Resource resource}
     */
    @Override
    public int getTotalSelected(){
        return Resource.getStream(length).mapToInt(this::getNSelected).reduce(0,Integer::sum);
    }

    /** Returns how many selected resources of the given type there are
     * @param toGet type of resource of which you want to know how many you have
     * @return Number of resources selected of the input type
     */
    public int getNSelected(Resource toGet){
        return nSel[toGet.getResourceNumber()];
    }

    public int getNumOfResourcesTypes(){
        return length;
    }

    /**
     * Marks n resources as selected for an action
     * @param nToSel number of resources to select
     * @param resToSel type of the resource to select
     */
    public void selectN(int nToSel, Resource resToSel){
        nSel[resToSel.getResourceNumber()]=min(nSel[resToSel.getResourceNumber()]+nToSel,nResAtPos[resToSel.getResourceNumber()]);
    }

    /**
     * Removes n resources from the ones selected for an action
     * @param nToDesel number of resources to deselect
     * @param resToDesel type of the resource to deselect
     */
    public void deselectN(int nToDesel, Resource resToDesel){
        nSel[resToDesel.getResourceNumber()]=max(nSel[resToDesel.getResourceNumber()]-nToDesel,0);
    }

    /**
     * Flags the {@link Resource resource} at the given global position in the {@link StorageUnit} as selected for the next action.
     * For example a production or buying a card from the CardShop.
     * @param globalPos the global position of the {@link Resource resource} that needs to be selected for production
     */
    @Override
    public void selectResourceAt(int globalPos) {
        selectN(1,Resource.fromInt(globalPos-globalPosition));
    }

    /**
     * Flags the {@link Resource resource} at the given global position in the {@link StorageUnit} as deselected for the next action.
     * For example a production or buying a card from the CardShop.
     *
     * @param globalPos the global position of the {@link Resource resource} that needs to be deselected for production
     */
    @Override
    public void deselectResourceAt(int globalPos) {
        deselectN(1,Resource.fromInt(globalPos-globalPosition));
    }

    /** Returns how many not selected {@link Resource resources} of the given type there are
     * @param type is a {@link Resource}
     * @return number of not selected {@link Resource resources} of the given type in the deposit
     */
    public int getNumberOf(Resource type){
        if(type.equals(Resource.EMPTY))
            return 0;
        return nResAtPos[type.getResourceNumber()];
    }

    public int getNumberOfNotSelected(Resource type){
        if(type.equals(Resource.EMPTY))
            return 0;
        return nResAtPos[type.getResourceNumber()] - nSel[type.getResourceNumber()];
    }


    /**
     * Removes the selected {@link Resource resources} from the {@link StorageUnit}
     */
    @Override
    public void removeSelected(){
        nResAtPos = IntStream.range(0, length).map(i->nResAtPos[i]-nSel[i]).toArray();
        nSel = IntStream.generate(()->0).limit(nSel.length).toArray();
    }




}
