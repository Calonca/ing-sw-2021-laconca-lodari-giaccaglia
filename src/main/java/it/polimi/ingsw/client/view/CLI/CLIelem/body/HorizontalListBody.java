package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.textUtil.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HorizontalListBody extends OptionList {

    int height;
    Mode mode;

    public enum Mode {
        WIDE{
            @Override
            public String s(HorizontalListBody horizontalListBody) {
                return horizontalListBody.toWideString();
            }
        }
        ,COMPACT {
            @Override
            public String s(HorizontalListBody horizontalListBody) {
                return horizontalListBody.toCenteredString();
            }
        };
        public abstract String s(HorizontalListBody horizontalListBody);

    }

    public HorizontalListBody(int height) {
        this.height = height;
        this.mode = Mode.COMPACT;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return mode.s(this);
    }

    public String toWideString() {
        int width = CLI.width;
        int optWidth = width/options.size();
        AtomicInteger startX = new AtomicInteger(optWidth / options.size());
        Canvas canvas = Canvas.withBorder(CLI.width,height+1);
        toStringStream().map(l->DrawableList.shifted(startX.get(),height-optMaxHeight(),l))
                .forEach(o->{
            canvas.draw(o);
            startX.addAndGet(optWidth);
        });
        return canvas.toString();
    }


    public String toCenteredString() {
        int width = CLI.width;
        int optWidth = optMaxWidth()+4;
        AtomicInteger startX = new AtomicInteger((width-(optWidth*options.size()))/2);
        Canvas canvas = Canvas.withBorder(CLI.width,height+1);
        toBelowStringStream().map(l->DrawableList.shifted(startX.get()+optWidth/4,height-optMaxHeight(),l))
                .forEach(o->{
            canvas.draw(o);
            startX.addAndGet(optWidth);
        });
        return canvas.toString();
    }


    public Stream<DrawableList> toStringStream() {
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


}
