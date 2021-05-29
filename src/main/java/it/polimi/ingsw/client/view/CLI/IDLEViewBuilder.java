package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.client.view.CLI.textUtil.Drawable;

import java.beans.PropertyChangeEvent;


public class IDLEViewBuilder extends it.polimi.ingsw.client.view.abstractview.IDLEViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        SpinnerBody spinnerBody = new SpinnerBody();
        getCLIView().setBody(spinnerBody);

        spinnerBody.performWhenReceiving(State.INITIAL_PHASE.name());
        Canvas canvas = Canvas.withBorder(CLI.width,getCLIView().getMaxBodyHeight());
        Drawable dwl = new Drawable();
        dwl.add(0,"Waiting for initial phase state");
        canvas.addDrawableList(dwl);
        spinnerBody.setMeanwhileShow(canvas);
        spinnerBody.setPerformer(()->
                {
                    getClient().changeViewBuilder(new InitialPhase());
                }
        );
        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
