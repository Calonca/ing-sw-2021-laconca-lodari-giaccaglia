package it.polimi.ingsw.client.view.cli.layout.recursivelist;

import it.polimi.ingsw.client.view.cli.layout.GridElem;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.drawables.Canvas;

import java.util.stream.Stream;

public class Column extends RecursiveList {


    public Column() {
    }

    public Column(Stream<Option> elem) {
        super(elem);
    }

    @Override
    public void addToCanvas(Canvas canvas, int x, int y) {
        int height=0;
        if (alignment.equals(Alignment.CANVAS_CENTER_VERTICAL))
            x = (canvas.getWidth() - getMinWidth()) / 2;
        for (GridElem e : elems) {
            e.addToCanvas(canvas, x, y + height);
            height += e.getMinHeight();
        }
    }

    @Override
    public int getMinWidth() {
        return elems.stream().mapToInt(GridElem::getMinWidth).max().orElse(0);
    }

    @Override
    public int getMinHeight() {
        return elems.stream().mapToInt(GridElem::getMinHeight).sum();
    }

}
