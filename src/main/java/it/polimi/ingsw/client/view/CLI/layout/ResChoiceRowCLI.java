package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.ResChoiceRow;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.List;

public class ResChoiceRowCLI extends ResChoiceRow {
    public ResChoiceRowCLI(int arrowPos, List<ResourceAsset> in, List<ResourceAsset> out) {
        super(arrowPos, in, out);
    }

    private Row choosingOutputRow(){
        Row resToChooseFrom = new Row();

        List<Drawable> optionsDwList = ResourceCLI.toList();
        for (Drawable drawable : optionsDwList) {
            Option o = Option.from(drawable, outRunnable);
            o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
            resToChooseFrom.addElem(o);
        }

        resToChooseFrom.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return resToChooseFrom;
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
            if (!(in.isEmpty()||out.isEmpty()) && i==in.size())
                row.addElem(Option.noNumber(" -> "));
            Drawable dl = ResourceCLI.fromAsset(resAsset).toBigDrawableList(i==arrowPos);
            row.addElem(Option.noNumber(dl));
            row.addElem(new SizedBox(1, 0));
        }

        if (resAreChosen())
            row.addElem(Option.noNumber(chosenResString));

        return row;
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

    public Column getGridElem() {
        Column column = new Column();
        column.addElem(selectedResourcesRow());
        if (getPointedResource().isPresent())
            column.addElem(arrowHead());
        if (numOfOutputChoices() > 0 && !choosingInput()){
            column.addElem(arrowBody());
            Row choosingOutRow = choosingOutputRow();
            column.addElem(choosingOutRow);
        }
        return column;
    }
}
