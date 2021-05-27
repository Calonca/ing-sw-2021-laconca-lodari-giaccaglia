package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.textUtil.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VerticalListBody extends OptionList {

    public VerticalListBody() {
    }

    public VerticalListBody(Stream<Option> optionStream) {
        super(optionStream);
    }

    public List<DrawableList> toDwList() {
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    DrawableList drawableList = new DrawableList();
                    Drawable number = new Drawable(0,0, i+": ", Color.ANSI_BLUE,Background.DEFAULT);
                    drawableList.add(number);

                    DrawableList drawableOptions = DrawableList.shifted(number.getWidth(),0,options.get(i).toDrawableList());
                    drawableList.add(drawableOptions);


                    return drawableList;}).collect(Collectors.toList());
    }

    private int optMaxWidth()
    {
        return options.stream().mapToInt(Option::horizontalSize).max().orElse(0)+1;
    }

    private int optMaxHeight()
    {
        return options.stream().mapToInt(Option::height).max().orElse(0);
    }

    //Prints vertical list
    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,cli.getMaxBodyHeight());
        List<DrawableList> dw = toDwList();



        //Shifting
        int shift =0;
        for (DrawableList dl:dw){
            dl.shift(0,shift);
            shift = dl.getHeight();}

        DrawableList toDraw = new DrawableList(dw.stream().flatMap(l->l.get().stream()).collect(Collectors.toList()));
        int writingX= (CLI.width-toDraw.getWidth())/2;
        int writingY= (CLI.height-toDraw.getHeight())/2;

        toDraw.shift(writingX,writingY);
        canvas.draw(toDraw);

        return canvas.toString();
    }

}
