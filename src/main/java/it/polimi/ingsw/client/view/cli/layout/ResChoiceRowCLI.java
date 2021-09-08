package it.polimi.ingsw.client.view.cli.layout;

import it.polimi.ingsw.client.view.cli.CLI;
import it.polimi.ingsw.client.view.cli.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.cli.layout.drawables.ResourceCLI;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
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
        String chosenResString= in.size()+out.size()==0? "You don't have resources to choose!" : "Your chosen resources";
        int startPositionX = 0;

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
        dwl.addToCenter(CLI.WIDTH ,"^");
        return Option.noNumber(dwl);
    }

    private Option arrowBody(){
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.WIDTH ,"|");
        dwl.addToCenter(CLI.WIDTH ,"|");
        dwl.addToCenter(CLI.WIDTH ,"|"+"-".repeat(1+(ResourceCLI.width()+6)*4)+"|");
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
