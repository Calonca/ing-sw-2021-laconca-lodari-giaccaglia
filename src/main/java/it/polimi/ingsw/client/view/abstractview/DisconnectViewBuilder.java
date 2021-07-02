package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.DisconnectCLI;
import it.polimi.ingsw.client.view.GUI.CreateMatchGUI;
import it.polimi.ingsw.client.view.GUI.DisconnectGUI;
import javafx.application.Platform;

import java.beans.PropertyChangeEvent;

public abstract class DisconnectViewBuilder extends ViewBuilder {
    protected final static int seconds = 5;
    public static final String disconnectedString = "Disconnected, the client will close in ";

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new DisconnectCLI();
        else return new DisconnectGUI();
    }

    public void startWaitingThread(){
        Thread thread=new Thread(()->
        {
            try {
                for(int i=seconds;i>0;i--)
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
