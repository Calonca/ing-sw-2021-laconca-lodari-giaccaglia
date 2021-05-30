package it.polimi.ingsw.client.view.CLI.CLIelem.body;


import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;

public class CanvasBody extends CLIelem {
    Canvas canvas;

    public CanvasBody(int width,int height) {
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
        return canvas.toString();
    }
}
