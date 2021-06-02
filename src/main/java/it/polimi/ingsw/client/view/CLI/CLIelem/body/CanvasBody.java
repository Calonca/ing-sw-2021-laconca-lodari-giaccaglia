package it.polimi.ingsw.client.view.CLI.CLIelem.body;


import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;

public class CanvasBody extends CLIelem {
    Canvas canvas;
    GridElem gridElem;

    public static CanvasBody centered(GridElem gridElem) {
        CanvasBody body = CanvasBody.fromGrid(gridElem);
        body.gridElem.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return body;
    }

    public static CanvasBody fromGrid(GridElem gridElem) {
        CanvasBody body = new CanvasBody(CLI.width,cli.getMaxBodyHeight());
        body.gridElem = gridElem;
        return body;
    }

    public CanvasBody(int width, int height) {
        canvas = Canvas.withBorder(width,height);
    }

    public CanvasBody(Canvas c) {
        canvas = c;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    @Override
    public String toString() {
        if (gridElem!=null)
            gridElem.addToCanvas(canvas,0,(canvas.getHeight()-gridElem.getMinHeight())/2);
        return canvas.toString();
    }
}
