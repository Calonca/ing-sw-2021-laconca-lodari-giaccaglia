package it.polimi.ingsw.client.view.CLI;

import com.google.gson.Gson;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class WaitForMatchToStart extends it.polimi.ingsw.client.view.abstractview.WaitForMatchToStart implements CLIView {
    private static final String SPINNER = "\\|/-\\|/-";
    /** Ascii backspace character */
    private static final String BACKSPACE = "\010";

    public WaitForMatchToStart(UUID matchId) {
        super(matchId);
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
        CLIBuilder.scroll();
        getCommonData().getMatchesData().ifPresent((matches)->
                Arrays.stream(matches).filter((p)->p.getKey().equals(matchId))
                        .forEach((s)-> System.out.println(new Gson().toJson(s)))
        );

        synchronized (this) {
            try {
                this.wait(100);
            } catch (InterruptedException e) {}

            int spinnerIdx = 0;
            while (!shouldStopInteraction()) {
                String lastWaitMessage = SPINNER.charAt(spinnerIdx) + " WaitingForStart ...";
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

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("matchesData"))
            getClient().transitionToView(new WaitForMatchToStart(matchId));
    }


}
