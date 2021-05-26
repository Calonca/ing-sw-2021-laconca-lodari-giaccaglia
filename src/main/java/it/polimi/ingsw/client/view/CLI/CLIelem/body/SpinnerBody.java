package it.polimi.ingsw.client.view.CLI.CLIelem.body;


import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.SetupPhase;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import java.util.*;

public class SpinnerBody extends CLIelem {

    Canvas meanwhileShow;

    @Override
    public String toString() {
        return  meanwhileShow==null? "null": meanwhileShow.toString();
    }

    @Override
    public int horizontalSize() {
        return -1;
    }

}
