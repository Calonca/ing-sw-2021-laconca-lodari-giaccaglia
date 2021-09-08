package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.cli.DisconnectCLI;
import it.polimi.ingsw.client.view.gui.DisconnectGUI;

import java.beans.PropertyChangeEvent;

public abstract class DisconnectViewBuilder extends ViewBuilder {
    protected static final int SECONDS = 5;
    public static final String DISCONNECTED_STRING = "Disconnected, the client will close in ";

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new DisconnectCLI();
        else return new DisconnectGUI();
    }

    public void startWaitingThread(){
        Thread thread=new Thread(()->
        {
            try {
                for(int i = SECONDS; i>0; i--)
                {
                    Thread.sleep(1000);
                    int finalI = i-1;
                    updateCountDown(finalI);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            exit();
        });

        thread.start();
    }

    public abstract void updateCountDown(int remaining);

    public abstract void exit();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
