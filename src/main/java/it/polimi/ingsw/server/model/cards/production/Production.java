package it.polimi.ingsw.server.model.cards.production;

import it.polimi.ingsw.server.model.Resource;


import java.util.Arrays;

/**
 * Class to apply the production cards logic
 */
public class Production {
    /**
     * Resources in these two arrays are atomic, and in the same group order as indicated in Enum Resources, for
     * easier access. The seventh cell of each array indicates the number of choices to be made on input and output,
     * then the counter will decrease and the basic resource's cells will increase. The two arrays are restored at
     * the end of the operations.
     */
    private int[] input;
    private final int[] restoreinput;
    private int[] output;
    private final int[] restoreoutput;

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
        input = Arrays.stream(restoreinput).toArray();
        output = Arrays.stream(restoreoutput).toArray();
    }

    /**
     * This method returns TRUE if there are any other resources to be chosen from input or output
     */
    public boolean choiceCanBeMade()
    {
        return choiceCanBeMadeOnInput()||choiceCanBeMadeOnOutput();
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
        if (input.length<Resource.TOCHOOSE.getResourceNumber()+1)
            return false;
        else return this.input[Resource.TOCHOOSE.getResourceNumber()] != 0;
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
     * Returns how many {@link Resource resources} there are in the {@link #input} array
     * @return how many {@link Resource resources} there are in the {@link #input} array
     */
    public int getNumOfResInInput(){
        return Arrays.stream(input).reduce(0,Integer::sum);
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
     * Return an array with the inputs of the production
     * @return an array of int with the inputs of the production.
     * In position i of the array there is the number of resources of type i in the enum ordering
     */
    public int[] getInputs(){
        return input;
    }

    /**
     * This method returns TRUE if there are any other resources to be chosen from output
     */
    public boolean choiceCanBeMadeOnOutput()
    {
        if (output.length<Resource.TOCHOOSE.getResourceNumber()+1)
            return false;
        else
            return this.output[Resource.TOCHOOSE.getResourceNumber()] != 0;
    }

    public void resetChoice()
    {
        System.arraycopy(restoreinput, 0, input, 0, input.length);
        System.arraycopy(restoreoutput, 0, output, 0, output.length);
    }

}
