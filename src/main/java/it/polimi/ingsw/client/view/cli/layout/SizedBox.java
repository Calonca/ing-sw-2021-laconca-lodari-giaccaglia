package it.polimi.ingsw.client.view.cli.layout;

import it.polimi.ingsw.client.view.cli.layout.drawables.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A {@link GridElem} used to create space between two {@link GridElem elements}
 */
public class SizedBox extends GridElem {
    private final int height;
    private final int width;

    public SizedBox(int width,int height) {
        this.height = height;
        this.width = width;
    }



    @Override
    public int getNextElemIndex() {
        return getFirstIdx();
    }

    @Override
    public Optional<Option> getOptionWithIndex(int i) {
        return Optional.empty();
    }

    @Override
    public List<Option> getAllEnabledOption() {
        return new ArrayList<>();
    }

    @Override
    public void addToCanvas(Canvas canvas, int x, int y) {

    }

    @Override
    public int getMinWidth() {
        return width;
    }

    @Override
    public int getMinHeight() {
        return height;
    }
}
