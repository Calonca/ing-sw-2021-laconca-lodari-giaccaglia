package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import java.beans.PropertyChangeEvent;

public class TestGridBody extends ViewBuilder {

    @Override
    public void run() {
        Canvas c = Canvas.withBorder(CLI.width, 20);
        Column column = new Column();
        Option o = Option.from("Test",buildTestRunnable(1));
        o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
        column.addElem(o);
        o.setFirstIdx(3);

        Row row = new Row();
        column.addElem(row);

        row.addElem(Option.from("Test3 ",buildTestRunnable(3)));

        row.addElem(Option.from("Test4",buildTestRunnable(4)));

        Option o2 = Option.from("Test5",buildTestRunnable(5));
        o2.setMode(Option.VisMode.NUMBER_TO_BOTTOM_SPACED);
        column.addElem(o2);

        Option o3 = Option.from("Test6",buildTestRunnable(6));
        o3.setMode(Option.VisMode.NUMBER_TO_LEFT);
        column.addElem(o3);

        Option o4 = Option.from("Test7",buildTestRunnable(7));
        o4.setMode(Option.VisMode.NUMBER_TO_LEFT);
        column.addElem(o4);

        column.addElem(Option.from("OtherLine",buildTestRunnable(8)));

        column.addToCanvas(c,3,0);

        row.selectAndRunOption(getCLIView());
        getCLIView().setBody(new CanvasBody(c));
        getCLIView().show();


    }

    private Runnable buildTestRunnable(int i){
        return ()->{
            getCLIView().setTitle("The result is "+i);
            getCLIView().show();
        };
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
