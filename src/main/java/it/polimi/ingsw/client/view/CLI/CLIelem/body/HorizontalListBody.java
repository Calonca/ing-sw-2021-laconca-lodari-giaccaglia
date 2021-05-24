package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class HorizontalListBody extends OptionList {

    //Todo Print horizontally and not Vertically
    @Override
    public String toString() {
        //Todo make spaces center the object.
        String spaces = "                                                    ";
        return toStringStream()
                .map(s -> spaces +s.replace("\n","\n"+spaces)).reduce("",(a,b)->a+b);
    }

    public Stream<String> toStringStream() {
        return IntStream.range(0,options.size())
                .mapToObj(i->i+": "+options.get(i).toString());
    }

    @Override
    public int horizontalSize() {
        return 0;
    }

}
