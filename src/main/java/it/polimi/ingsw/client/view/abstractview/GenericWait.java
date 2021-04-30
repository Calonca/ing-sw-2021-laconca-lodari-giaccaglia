package it.polimi.ingsw.client.view.abstractview;

public abstract class GenericWait extends View{

    protected Runnable r;
    protected String name;

    public GenericWait(Runnable r, String s) {
        this.r = r;
        this.name=s;
    }

    public GenericWait(String s) {
        this.r = ()->{};
        this.name=s;
    }

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

    public void perform() {
        r.run();
    }
}
