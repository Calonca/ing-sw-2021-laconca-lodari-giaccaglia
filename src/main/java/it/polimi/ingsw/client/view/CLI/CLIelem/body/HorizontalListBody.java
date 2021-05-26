package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HorizontalListBody extends OptionList {
    int height;

    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,cli.getMaxBodyHeight());
        //canvas.drawWithDefaultColor(spaces.length(),height,);
        return canvas.toString();
        //Todo make spaces center the object.
        //String spaces = "                                                    ";
        //return toStringStream()
        //        .map(s -> spaces +s.replace("\n","\n"+spaces)).reduce("",(a,b)->a+b);
    }

    public Stream<String> toStringStream() {
        return IntStream.range(0,options.size())
                .mapToObj(i->i+": "+options.get(i).toString());
    }

    private int verticalSize()
    {
        return options.stream().mapToInt(o->StringUtil.maxHeight(o.toString())).max().orElse(0);
    }


}
