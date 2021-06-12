package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResChoiceRow extends GridElem {

    private int arrowPos;
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
            arrowPos = i;
            Drawable drawable = optionsDwList.get(i);
            Option o = Option.from(drawable, r);
            o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
            resToChooseFrom.addElem(o);
        }

        //resToChooseFrom.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return resToChooseFrom;
    }


    public Optional<ResourceAsset> getPointedResource(){
        if (arrowPos>=in.size()+out.size())
            return Optional.empty();
        ResourceAsset res = arrowPos <= in.size() ? in.get(arrowPos) : out.get(arrowPos - in.size());
        return Optional.ofNullable(res);
    }

    private Row selectedResourcesRow(){
        String resToChooseString = "Resources to choose: ";
        String chosenResString= in.size()+out.size()==0?"No resources to choose from":"Your chosen resources";
        int CenterX = 1+ CLI.getCenterX()-(ResourceCLI.width())/2;
        int startPositionX = 0;
        //int startPositionX = CenterX-resToChooseString.length()-(ResourceCLI.width()+1)*(arrowPos);

        Row row = new Row();
        row.addElem(new SizedBox(startPositionX,0));

        if (!resAreChosen())
            row.addElem(Option.noNumber(resToChooseString));
        else
            row.addElem(new SizedBox(resToChooseString.length(),0));

        for (int i = 0, size = out.size()+in.size(); i < size; i++) {
            ResourceAsset resAsset;
            if (i<in.size())
                 resAsset = in.get(i);
                else
            resAsset = out.get(i-in.size());
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

    private Option arrowHead(){
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.width,"^");
        return Option.noNumber(dwl);
    }

    private Option arrowBody(){
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.width,"|");
        dwl.addToCenter(CLI.width,"|");
        dwl.addToCenter(CLI.width,"|"+"-".repeat(1+(ResourceCLI.width()+6)*4)+"|");
        return Option.noNumber(dwl);
    }

    private int numOfOutputChoices(){
        return (int) out.stream().filter(r->r.equals(ResourceAsset.TOCHOOSE)).count();
    }

    public int getIndex() {
        return arrowPos;
    }

    public void moveToNextIndex() {
        arrowPos++;
    }

    @Override
    public int getNextElemIndex() {
        return getFirstIdx();
    }

    @Override
    public Optional<Option> getOptionWithIndex(int i) {
        return Optional.empty();
    }

    @Override
    public List<Option> getAllEnabledOption() {
        return new ArrayList<>();
    }



    @Override
    public void addToCanvas(Canvas canvas, int x, int y) {
        Column column = getColumn();
        column.addToCanvas(canvas,x,y);
    }

    @NotNull
    private Column getColumn() {
        Column column = new Column();
        column.addElem(selectedResourcesRow());
        if (getPointedResource().isPresent())
            column.addElem(arrowHead());
        if (numOfOutputChoices() > 0){
            column.addElem(arrowBody());
            Row choosingOutRow = choosingOutput(outRunnable);
            column.addElem(choosingOutRow);
            choosingOutRow.setFirstIdx(0);
        }
        return column;
    }

    @Override
    public int getMinWidth() {
        return getColumn().getMinWidth();
    }

    @Override
    public int getMinHeight() {
        return getColumn().getMinHeight();
    }
}
