package it.polimi.ingsw.client.view.cli.CLIelem.body;


import it.polimi.ingsw.client.view.cli.CLI;
import it.polimi.ingsw.client.view.cli.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.cli.layout.GridElem;
import it.polimi.ingsw.client.view.cli.layout.drawables.Canvas;

public class CanvasBody extends CLIelem {
    private final Canvas canvas;
    GridElem gridElem;

    public static CanvasBody centered(GridElem gridElem) {
        CanvasBody body = CanvasBody.fromGrid(gridElem);
        body.gridElem.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return body;
    }

    /**
     * @param gridElem is a valid element
     * @return the corresponding CanvasBody
     */
    public static CanvasBody fromGrid(GridElem gridElem) {
        CanvasBody body = new CanvasBody(CLI.WIDTH ,cli.getMaxBodyHeight());
        body.gridElem = gridElem;
        return body;
    }

    public CanvasBody(int width, int height) {
        canvas = Canvas.withBorder(width,height);
    }

    @Override
    public String toString() {
        if (gridElem !=null)
            gridElem.addToCanvas(canvas,0,(canvas.getHeight()- gridElem.getMinHeight())/2);
        return canvas.toString();
    }
}
