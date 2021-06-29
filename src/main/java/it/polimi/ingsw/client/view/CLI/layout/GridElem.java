package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.RecursiveList;

import java.util.List;
import java.util.Optional;

/**
 * An element of {@link RecursiveList}, it has an index in the list, an alignment, a character width and height in the cli
 * and also a method to draw the element
 *
 */
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

    /**
     * Returns the index of the next added element
     */
    public abstract int getNextElemIndex();

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

    public abstract List<Option> getAllEnabledOption();

    public abstract void addToCanvas(Canvas canvas, int x,int y);

    public abstract int getMinWidth();

    public abstract int getMinHeight();


}
