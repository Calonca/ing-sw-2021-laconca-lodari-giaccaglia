package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Class to apply the production cards logic
 */
public class Production {
    /**
     * Resources in these two arrays are atomic, and in the same group order as indicated in Enum Resources.
     */
    private int input[];
    private int restoreinput[];
    private int output[];
    private int restoreoutput[];
    private int victorypoints;

    /**
     * This method will check the normal resources first through box,
     * then thorugh warehouse and then if the choiches are possible.
    // * @param personalboard != NULL
     * Returns True if
     */

  /*  public boolean isAvailable(PersonalBoard personalboard)
    {
        Box box= personalboard.getbox();

        WarehouseLeaderDepot depots= personalboard.getWarehouseLeaderDepot;
        int[] array=IntStream.range(0,4).map( pos -> input[pos]-box.numOfResources(pos)).map( res -> Math.max(res, 0)).toArray();
        if(!depots.areThereEnoughResources(array))
                return false;
        return Arrays.stream(input).limit(4).reduce(0, Integer::sum)+input[6]<personalboard.numOfResources();
    }

    /**
     * This class memorizes the choice for "black" resource
     * @param personalboard != NULL
     */
    public void performChoice(Resource resource)
    {
        input[resource.getResourceNumber()]++;
        input[Resource.TOCHOOSE.getResourceNumber()]--;
    }

    public boolean choiceCanBeMade()
    {
        return this.input[Resource.TOCHOOSE.getResourceNumber()] != 0;
    }



    public void resetchoice()
    {
        System.arraycopy(restoreinput, 0, input, 0, input.length);
    }
}
