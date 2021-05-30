package it.polimi.ingsw.client.view.CLI.CLIelem.body;


import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;

public class SpinnerBody extends CLIelem {

    Canvas meanwhileShow;

    public void setMeanwhileShow(Canvas meanwhileShow) {
        this.meanwhileShow = meanwhileShow;
    }

    @Override
    public String toString() {
        return  meanwhileShow==null? "no data in spinner": meanwhileShow.toString();
    }

}
