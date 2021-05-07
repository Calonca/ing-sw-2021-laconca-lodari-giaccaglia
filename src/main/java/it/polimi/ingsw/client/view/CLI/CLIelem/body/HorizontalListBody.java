package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;

public class HorizontalListBody extends OptionList {

    //Todo Print horizontally and not Vertically
    @Override
    public String toString() {
        //Todo make spaces center the object.
        String spaces = "                                                    ";
        return toStringStream()
                .map(s -> spaces +s.replace("\n","\n"+spaces)).reduce("",(a,b)->a+b);
    }

}
