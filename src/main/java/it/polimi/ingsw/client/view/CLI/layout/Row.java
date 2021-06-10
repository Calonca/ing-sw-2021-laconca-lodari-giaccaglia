package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;

import java.util.stream.Stream;

public class Row extends OptionList {

    public Row() {
    }

    public Row(Stream<? extends GridElem> elem) {
        super(elem);
    }


    @Override
    public void addToCanvas(Canvas canvas, int x, int y) {
        int width=0;
        if (alignment.equals(Alignment.CANVAS_CENTER_VERTICAL))
            x = (canvas.getWidth() - getMinWidth()) / 2;
        for (GridElem e : elems) {
            e.addToCanvas(canvas, x+width, y);
            width+=e.getMinWidth();
        }
    }

    @Override
    public int getMinWidth() {
        return elems.stream().mapToInt(GridElem::getMinWidth).sum();
    }

    @Override
    public int getMinHeight() {
        return elems.stream().mapToInt(GridElem::getMinHeight).max().orElse(0);
    }

}
