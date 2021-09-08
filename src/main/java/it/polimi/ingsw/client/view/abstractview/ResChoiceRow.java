package it.polimi.ingsw.client.view.abstractview;


import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This is a helper class to select resources conversion.
 */
public class ResChoiceRow {

    protected int arrowPos;
    protected final List<ResourceAsset> in;
    protected final List<ResourceAsset> out;
    protected final List<Integer> chosenInputPos;
    protected final List<Integer> chosenOutputRes;
    protected Runnable outRunnable;

    public ResChoiceRow(int arrowPos, List<ResourceAsset> in, List<ResourceAsset> out) {
        this.arrowPos = arrowPos;
        this.in = in;
        this.out = out;
        this.chosenInputPos = new ArrayList<>();
        this.chosenOutputRes = new ArrayList<>();
    }

    /**
     * Called when the player has to make a selection on TO CHOOSE resources
     * @return the chosen input resources
     */
    public boolean choosingInput(){
        return in.size()>chosenInputPos.size();
    }



    public void setOnChosenOutput(Runnable r) {
        outRunnable = r;
    }


    /**
     * Gets the next TO CHOOSE resource "in line"
     */
    public Optional<ResourceAsset> getPointedResource(){
        if (arrowPos>=in.size()+out.size())
            return Optional.empty();
        ResourceAsset res = arrowPos < in.size() ? in.get(arrowPos) : out.get(arrowPos - in.size());
        return Optional.ofNullable(res);
    }

    /**
     * sets the next TO CHOOSE resource "in line"
     */
    private void setPointedResource(ResourceAsset res){
        if (choosingInput()) {
            in.set(arrowPos, res);
        } else {
            out.set(arrowPos - in.size(), res);
        }
    }



    /**
     * When all TO CHOOSE resource are temporarily transformed in concrete resources
     */
    protected boolean resAreChosen(){
        return arrowPos>=in.size()+out.size();
    }


    /**
     * Number of TO CHOOSE resources in input
     */
    protected int numOfOutputChoices(){
        return (int) out.stream().filter(r->r.equals(ResourceAsset.TO_CHOOSE)).count();
    }

    /**
     * Sets the next resource to send
     * @param pos global position of the resource in the Warehouse or Strongbox
     * @param res The resource to select if choosing output
     */
    public void setNextInputPos(int pos, ResourceAsset res)
    {
        if (res!=null)
            setPointedResource(res);

        if (choosingInput()) {
            chosenInputPos.add(pos);
            arrowPos++;
            if (!choosingInput()) {
                int firstToChoose = out.indexOf(ResourceAsset.TO_CHOOSE);
                arrowPos = in.size() + (firstToChoose == -1 ? out.size() : firstToChoose);
            }
        }
        else {
            if (res!=null)
                chosenOutputRes.add(res.ordinal());
            int firstToChoose  =  out.indexOf(ResourceAsset.TO_CHOOSE);
            arrowPos = in.size()+ (firstToChoose==-1?out.size():firstToChoose);
        }

    }


    public List<Integer> getChosenInputPos() {
        return chosenInputPos;
    }

    public List<Integer> getChosenOutputRes() {
        return chosenOutputRes;
    }

}
