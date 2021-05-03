package it.polimi.ingsw.client.view.CLI.CLIelem;


import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.PlayerCache;
import it.polimi.ingsw.client.view.CLI.SetupPhase;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Spinner extends CLIelem implements Runnable {

    protected final String waitingFor;
    private String meanwhileShow;
    private boolean stopASAP;
    private boolean paused;

    public Spinner(String waitingFor) {
        this.waitingFor = waitingFor;
    }

    /**
     * Spinner for match start.
     * Updates its meanwhileShow from matchesData.
     * Goes to {@link SetupPhase} when the state goes to SetupPhase
     */
    public static Spinner matchToStart(Client client){
        Spinner spinner = new Spinner(
                "Match to start",
                client.getCommonData().thisMatchPlayers().map(Arrays::toString).toString());
        spinner.setPerformer(()-> client.transitionToView(new SetupPhase()));
        spinner.setUpdater(()->{
            if (spinner.getEvt().getPropertyName().equals(CommonData.matchesDataString)) {
                spinner.meanwhileShow = client.getCommonData().thisMatchPlayers().map(Arrays::toString).toString();
            } else if (spinner.getEvt().getPropertyName().equals(PlayerCache.SETUP_PHASE))
                spinner.perform();
        });
        spinner.setPerformer(()-> {
            spinner.stopASAP = true;
            client.transitionToView(new SetupPhase());
        });
        spinner.paused=false;
        return spinner;
    }

    public void stop(){
        stopASAP = true;
    }

    public void pause(){
        stopASAP=true;
    }

    public void resume(){
        run();
    }

    public Spinner(String waitingFor,String meanwhileShow) {
        this.waitingFor = waitingFor;
        this.meanwhileShow = meanwhileShow;
        this.paused=false;
    }

    /** Characters used for the spinner animation */
    private static final String SPINNER = "\\|/-\\|/-";
    /** Ascii backspace character */
    private static final String BACKSPACE = "\010";

    /**
     * Runs the spinner
     */
    @Override
    public void run() {
        synchronized (this) {
            String currentTitle = meanwhileShow;
            if (currentTitle!=null)
                System.out.println(meanwhileShow);
            try {
                this.wait(100);
            } catch (InterruptedException ignored) {}

            int spinnerIdx = 0;
            while (!stopASAP) {
                String lastWaitMessage = SPINNER.charAt(spinnerIdx) + " Waiting for "+ waitingFor +" ...";
                System.out.print(lastWaitMessage);
                spinnerIdx = (spinnerIdx + 1) % SPINNER.length();

                try {
                    this.wait(500);
                } catch (InterruptedException ignored) {}

                //Updates title
                if (currentTitle!=null && !currentTitle.equals(meanwhileShow)) {
                    currentTitle = meanwhileShow;
                    for (int i = 0; i < currentTitle.length(); i++)
                        System.out.print(BACKSPACE);
                }
                /* Erase the last wait message */
                for (int i=0; i<lastWaitMessage.length(); i++)
                    System.out.print(BACKSPACE);
            }
            //Deletes title
            if (meanwhileShow!=null)
                for (int i = 0; i < currentTitle.length(); i++)
                    System.out.print(BACKSPACE);
        }
    }



}
