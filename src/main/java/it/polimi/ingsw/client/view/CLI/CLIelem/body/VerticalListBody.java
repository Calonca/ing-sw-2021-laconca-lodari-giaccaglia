package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;

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
        //Todo make spaces center the object.
        String spaces = "                                                    ";
        return toStringStream()
                .map(s -> spaces +s.replace("\n","\n"+spaces) + "\n").reduce("",(a,b)->a+b);
    }
}
