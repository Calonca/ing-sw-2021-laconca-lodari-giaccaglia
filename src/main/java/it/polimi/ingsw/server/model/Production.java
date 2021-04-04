package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.player.board.WarehouseLeadersDepots;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Class to apply the production cards logic TODO testing
 */
public class Production {
    /**
     * Resources in these two arrays are atomic, and in the same group order as indicated in Enum Resources, for
     * easier access. The seventh cell of each array indicates the number of choices to be made on input and output,
     * then the counter will decrease and the basic resource's cells will increase. The two arrays are restored at
     * the end of the operations.
     */
    private int input[];
    private int restoreinput[];
    private int output[];
    private int restoreoutput[];
    private int victorypoints;

    /**
     * Constructor for basic production
     * @return the basic Production of Maestri del rinascimento
     */
    public static Production basicProduction() {
        return new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1});
    }


    /**
     * Creates a production with the given input and outputs
     * @param inputs an array of int with the inputs of the production.
     *               In position i of the array there is the number of resources of type i in the enum ordering
     * @param outputs an array of int with the outputs of the production.
     *                In position i of the array there is the number of resources of type i in the enum ordering
     */
    public Production(int[] inputs,int[] outputs){
        restoreinput = inputs;
        restoreoutput = outputs;
        System.arraycopy(restoreinput, 0, input, 0, input.length);
        System.arraycopy(restoreoutput, 0, outputs, 0, outputs.length);
    }

    /**
     * This method will check the normal resources first through box,
     * then through warehouse and then if the choiches are possible.
    // * @param personalboard != NULL
     * Returns True if
     */
    public boolean isAvailable(PersonalBoard personalboard)
    {
        Box box= personalboard.getStrongBox();

        WarehouseLeadersDepots depots= personalboard.getWarehouseLeadersDepots();
        int[] array=IntStream.range(0,4).map( pos -> input[pos]-box.getNumberOf(pos)).map( res -> Math.max(res, 0)).toArray();
        if(!depots.enoughResourcesForProductions(array))
                return false;
        return Arrays.stream(input).limit(4).reduce(0, Integer::sum)+input[6]<personalboard.numOfResources();
    }

    /**
     * This method memorizes the choice for "black" resource input
     * @param resource != NULL
     */
    public void performChoiceOnInput(Resource resource)
    {
        input[resource.getResourceNumber()]++;
        input[Resource.TOCHOOSE.getResourceNumber()]--;
    }

    /**
     * This method returns TRUE if there are any other resources to be chosen from input
     */
    public boolean choiceCanBeMadeOnInput()
    {
        return this.input[Resource.TOCHOOSE.getResourceNumber()] != 0;
    }

    /**
     * This method memorizes the choice for "black" output
     * @param resource != NULL
     */
    public void performChoiceOnOutput(Resource resource)
    {
        output[resource.getResourceNumber()]++;
        output[Resource.TOCHOOSE.getResourceNumber()]--;
    }

    /**
     * Return an array with the outputs of the production
     * @return an array of int with the outputs of the production.
     * In position i of the array there is the number of resources of type i in the enum ordering
     */
    public int[] getOutputs(){
        return output;
    }

    /**
     * This method returns TRUE if there are any other resources to be chosen from output
     */
    public boolean choiceCanBeMadeOnOutput()
    {
        return this.output[Resource.TOCHOOSE.getResourceNumber()] != 0;
    }

    public void resetchoice()
    {
        System.arraycopy(restoreinput, 0, input, 0, input.length);
    }

}
