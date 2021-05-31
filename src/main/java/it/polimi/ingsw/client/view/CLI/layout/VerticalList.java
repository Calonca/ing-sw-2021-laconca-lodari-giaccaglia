package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.textUtil.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VerticalList extends OptionList {
    private int maxHeight;

    public VerticalList(int h) {
        super();
        maxHeight = h;
    }

    public VerticalList(Stream<Option> optionStream,int h) {
        super(optionStream);
        maxHeight = h;
    }

    @Override
    public void addOption(Option o) {
        super.addOption(o);
    }

    public List<Drawable> toDwList() {
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    Drawable drawable = new Drawable();
                    DrawableLine number = new DrawableLine(0,0, i+": ", Color.OPTION,Background.DEFAULT);
                    drawable.add(number);

                    Drawable drawableOptions = Drawable.copyShifted(number.getWidth(),0,options.get(i).toDrawableList());
                    drawable.add(drawableOptions);


                    return drawable;}).collect(Collectors.toList());
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getOptionNumber(){
        return options.size();
    }

    //Prints vertical list
    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width, maxHeight);
        List<Drawable> dw = toDwList();

        int height =0;
        for (Drawable drawable : dw) {
            drawable.shift(1, height);
            height += drawable.getHeight();
        }

        dw.forEach(canvas::addDrawable);

        return canvas.toString();
    }

}
