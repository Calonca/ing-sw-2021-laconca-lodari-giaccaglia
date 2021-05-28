package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.textUtil.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HorizontalListBody extends OptionList {

    private final int height;
    private Mode mode;
    private boolean showOptions=true;
    private Canvas canvas;

    public enum Mode {
        WIDE{
            @Override
            public void draw(HorizontalListBody horizontalListBody) {
                horizontalListBody.drawWideString();
            }
        }
        ,COMPACT {
            @Override
            public void draw(HorizontalListBody horizontalListBody) {
                horizontalListBody.drawCenteredString();
            }
        };
        public abstract void draw(HorizontalListBody horizontalListBody);

    }

    public HorizontalListBody(int height) {
        this.height = height;
        this.mode = Mode.COMPACT;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setShowOptions(boolean showOptions) {
        this.showOptions = showOptions;
    }

    @Override
    public String toString() {
        mode.draw(this);
        return canvas.toString();
    }

    public void drawWideString() {
        int width = CLI.width;
        int optWidth = width/options.size();
        AtomicInteger startX = new AtomicInteger(optWidth / options.size());
        Canvas ca = Canvas.withBorder(CLI.width,height+1);
        toStringStream().map(l->DrawableList.shifted(startX.get(),(height-optMaxHeight())/2,l))
                .forEach(o->{
            ca.addDrawableList(o);
            startX.addAndGet(optWidth);
        });
        this.canvas = ca;
    }


    public void drawCenteredString() {
        int width = CLI.width;
        int optWidth = optMaxWidth()+4;
        AtomicInteger startX = new AtomicInteger((width-(optWidth*options.size()))/2);
        Canvas canvas = Canvas.withBorder(CLI.width,height+1);
        toBelowStringStream().map(l->DrawableList.shifted(startX.get()+optWidth/4,(height-optMaxHeight())/2,l))
                .forEach(o->{
            canvas.addDrawableList(o);
            startX.addAndGet(optWidth);
        });
        this.canvas = canvas;
    }


    public Stream<DrawableList> toStringStream() {
        if (!showOptions) {
            return options.stream().map(Option::toDrawableList);
        }
        int spaces = (optMaxWidth()/2)-2;
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    DrawableList drawableList = new DrawableList();
                    drawableList.add(new Drawable(spaces,0, i+":", Color.ANSI_BLUE,Background.DEFAULT));
                    DrawableList drawableOptions = DrawableList.shifted(0,1,options.get(i).toDrawableList());
                    drawableList.add(drawableOptions);
                    return drawableList;});
    }

    public Stream<DrawableList> toBelowStringStream() {
        if (!showOptions) {
            return options.stream().map(Option::toDrawableList);
        }
        int spaces = (optMaxWidth()/2)-2;
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    DrawableList drawableList = new DrawableList();
                    drawableList.add(options.get(i).toDrawableList());
                    drawableList.add(spaces, i+":", Color.ANSI_BLUE,Background.DEFAULT);
                    return drawableList;});
    }

    private int optMaxWidth()
    {
        return options.stream().mapToInt(Option::horizontalSize).max().orElse(0)+1;
    }

    private int optMaxHeight()
    {
        return options.stream().mapToInt(Option::height).max().orElse(0);
    }

    public Canvas getCanvas() {
        mode.draw(this);
        return canvas;
    }
}
