package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.drawables.Drawable;

import java.beans.PropertyChangeEvent;


public class IDLEViewBuilder extends it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        SpinnerBody spinnerBody = new SpinnerBody();
        getCLIView().setBody(spinnerBody);

        Canvas canvas = Canvas.withBorder(CLI.width,getCLIView().getMaxBodyHeight());
        Drawable dwl = new Drawable();
        dwl.add(0,"Waiting for initial phase state");
        canvas.addDrawable(dwl);
        spinnerBody.setMeanwhileShow(canvas);
        spinnerBody.switchToStateWhenReceiving(State.INITIAL_PHASE.name(),new InitialOrFinalPhaseCLI(true),getClient());

        getCLIView().show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
