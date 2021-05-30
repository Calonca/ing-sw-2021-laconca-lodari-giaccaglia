package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.drawables.DrawableLine;
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
        toStringStream().map(l-> Drawable.copyShifted(startX.get(),(height-optMaxHeight())/2,l))
                .forEach(o->{
            ca.addDrawable(o);
            startX.addAndGet(optWidth);
        });
        this.canvas = ca;
    }


    public void drawCenteredString() {
        int width = CLI.width;
        int optWidth = optMaxWidth()+4;
        AtomicInteger startX = new AtomicInteger((width-(optWidth*options.size()))/2);
        Canvas canvas = Canvas.withBorder(CLI.width,height+1);
        toBelowStringStream().map(l-> Drawable.copyShifted(startX.get()+optWidth/4,(height-optMaxHeight())/2,l))
                .forEach(o->{
            canvas.addDrawable(o);
            startX.addAndGet(optWidth);
        });
        this.canvas = canvas;
    }


    public Stream<Drawable> toStringStream() {
        if (!showOptions) {
            return options.stream().map(Option::toDrawableList);
        }
        int spaces = (optMaxWidth()/2)-2;
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    Drawable drawable = new Drawable();
                    drawable.add(new DrawableLine(spaces,0, i+":", Color.BRIGHT_BLUE,Background.DEFAULT));
                    Drawable drawableOptions = Drawable.copyShifted(0,1,options.get(i).toDrawableList());
                    drawable.add(drawableOptions);
                    return drawable;});
    }

    public Stream<Drawable> toBelowStringStream() {
        if (!showOptions) {
            return options.stream().map(Option::toDrawableList);
        }
        int spaces = (optMaxWidth()/2)-2;
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    Drawable drawable = new Drawable();
                    drawable.add(options.get(i).toDrawableList());
                    drawable.add(spaces, i+":", Color.BRIGHT_BLUE,Background.DEFAULT);
                    return drawable;});
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
