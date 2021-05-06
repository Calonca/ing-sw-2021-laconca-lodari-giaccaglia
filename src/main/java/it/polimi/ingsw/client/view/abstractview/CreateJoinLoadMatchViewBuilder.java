package it.polimi.ingsw.client.view.abstractview;

public abstract class CreateJoinLoadMatchViewBuilder extends ViewBuilder {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {

    }
}
