package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.textUtil.*;

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

    public List<Drawable> toDwList() {
        return IntStream.range(0,options.size())
                .mapToObj(i->{
                    Drawable drawable = new Drawable();
                    DrawableLine number = new DrawableLine(0,0, i+": ", Color.ANSI_BLUE,Background.DEFAULT);
                    drawable.add(number);

                    Drawable drawableOptions = Drawable.copyShifted(number.getWidth(),0,options.get(i).toDrawableList());
                    drawable.add(drawableOptions);


                    return drawable;}).collect(Collectors.toList());
    }

    //Prints vertical list
    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,cli.getMaxBodyHeight());
        List<Drawable> dw = toDwList();



        //Shifting
        int shift =0;
        for (Drawable dl:dw){
            dl.shift(0,shift);
            shift = dl.getHeight();}

        Drawable toDraw = new Drawable(dw.stream().flatMap(l->l.get().stream()).collect(Collectors.toList()));
        int writingX= (CLI.width-toDraw.getWidth())/2;
        int writingY= (CLI.height-toDraw.getHeight())/2;

        toDraw.shift(writingX,writingY);
        canvas.addDrawableList(toDraw);

        return canvas.toString();
    }

}
