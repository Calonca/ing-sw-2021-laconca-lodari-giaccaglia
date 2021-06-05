package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.List;

public class ResChoiceRow {

    private int index;
    private final int arrowPos;
    private final List<ResourceAsset> in;
    private final List<ResourceAsset> out;
    private Runnable outRunnable;

    public ResChoiceRow(int arrowPos, List<ResourceAsset> in, List<ResourceAsset> out) {
        this.arrowPos = arrowPos;
        this.in = in;
        this.out = out;
    }

    public void setOnChosenOutput(Runnable r) {
        outRunnable = r;
    }

    private Row choosingOutput(Runnable r){
        Row resToChooseFrom = new Row();

        List<Drawable> optionsDwList = ResourceCLI.toList();
        for (int i = 0, optionsDwListSize = optionsDwList.size(); i < optionsDwListSize; i++) {
            index = i;
            Drawable drawable = optionsDwList.get(i);
            Option o = Option.from(drawable, r);
            o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
            resToChooseFrom.addElem(o);
        }

        resToChooseFrom.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return resToChooseFrom;
    }


    private Row selectedResourcesRow(){
        String resToChooseString = "Resources to choose: ";
        String chosenResString= numOfOutputChoices()==0?"No resources to choose from":"Your chosen resources";
        int CenterX = 1+ CLI.getCenterX()-(ResourceCLI.width())/2;

        int startPositionX = CenterX-resToChooseString.length()-(ResourceCLI.width()+1)*(arrowPos);

        Row row = new Row();
        row.addElem(new SizedBox(startPositionX,0));

        if (!resAreChosen())
            row.addElem(Option.noNumber(resToChooseString));
        else
            row.addElem(new SizedBox(resToChooseString.length(),0));

        for (int i = 0, outSize = out.size(); i < outSize; i++) {
            ResourceAsset resAsset = out.get(i);
            Drawable dl = ResourceCLI.fromAsset(resAsset).toBigDrawableList(i==arrowPos);
            row.addElem(Option.noNumber(dl));
            row.addElem(new SizedBox(1, 0));
        }

        if (resAreChosen())
            row.addElem(Option.noNumber(chosenResString));

        return row;
    }



    private boolean resAreChosen(){
        return arrowPos>=in.size()+out.size();
    }

    public void addToGrid(OptionList e) {
        Column column = e.addAndGetColumn();
        column.addElem(selectedResourcesRow());
        if (numOfOutputChoices() > 0){
            column.addElem(arrow());
            Row choosingOutRow = choosingOutput(outRunnable);
            column.addElem(choosingOutRow);
            choosingOutRow.setFirstIdx(0);
        }
    }

    private Option arrow(){
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.width,"^");
        dwl.addToCenter(CLI.width,"|");
        dwl.addToCenter(CLI.width,"|");
        dwl.addToCenter(CLI.width,"|"+"-".repeat(1+(ResourceCLI.width()+6)*4)+"|");
        return Option.noNumber(dwl);
    }

    private int numOfOutputChoices(){
        return (int) out.stream().filter(r->r.equals(ResourceAsset.TOCHOOSE)).count();
    }

    public int getIndex() {
        return index;
    }
}
