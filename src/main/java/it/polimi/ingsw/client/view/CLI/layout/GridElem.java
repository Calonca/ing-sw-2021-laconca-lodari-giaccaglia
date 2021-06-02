package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import javafx.util.Pair;

import java.util.Optional;

public abstract class GridElem {
    public enum Alignment {
        START,
        CANVAS_CENTER_VERTICAL;
    }

    protected Alignment alignment;

    private int firstIdx;
    public int getFirstIdx(){
        return firstIdx;
    };

    public abstract int getLastIndex();

    public Alignment getAlignment() {
        return alignment;
    }
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public void setFirstIdx(int firstIdx) {
        this.firstIdx = firstIdx;
    }

    public abstract Optional<Option> getOptionWithIndex(int i);

    public abstract void addToCanvas(Canvas canvas, int x,int y);

    public abstract int getMinWidth();

    public abstract int getMinHeight();


}
