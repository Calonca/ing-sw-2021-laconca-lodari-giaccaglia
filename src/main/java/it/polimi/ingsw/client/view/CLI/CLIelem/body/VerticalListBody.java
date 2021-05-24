package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VerticalListBody extends OptionList {

    public VerticalListBody() {
    }

    public VerticalListBody(Stream<Option> optionStream) {
        super(optionStream);
    }

    //Prints horizontally
    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,CLI.height-3);
        String spaces = StringUtil.spaces(CLI.getCenterX()-horizontalSize()/2);
        canvas.drawWithDefaultColor(spaces.length(),0,stringWithNumber());
        return canvas.toString();
    }



    public String stringWithNumber() {
        return IntStream.range(0,options.size())
                .mapToObj(i->i+": "+options.get(i).toString()).reduce("",(a,b)->a+b+"\n");
    }

    @Override
    public int horizontalSize() {
        return options.stream().mapToInt(Option::horizontalSize).max().orElse(0)+3;
    }
}
