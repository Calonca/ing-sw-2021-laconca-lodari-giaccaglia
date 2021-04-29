package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.View;

public class WaitForServerConnection extends View implements CLIView {


    /** Characters used for the spinner animation */
    private static final String SPINNER = "\\|/-\\|/-";
    /** Ascii backspace character */
    private static final String BACKSPACE = "\010";

    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        synchronized (this) {
            try {
                this.wait(100);
            } catch (InterruptedException e) {}

            int spinnerIdx = 0;
            while (!shouldStopInteraction()) {
                String lastWaitMessage = SPINNER.charAt(spinnerIdx) + " Connecting to server ...";
                System.out.print(lastWaitMessage);
                spinnerIdx = (spinnerIdx + 1) % SPINNER.length();

                try {
                    this.wait(500);
                } catch (InterruptedException e) {}

                /* Erase the last wait message */
                for (int i=0; i<lastWaitMessage.length(); i++)
                    System.out.print(BACKSPACE);
            }
        }
    }
}
