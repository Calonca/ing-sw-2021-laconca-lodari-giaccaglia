package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.CLI;
import it.polimi.ingsw.client.view.cli.CLIelem.Title;
import it.polimi.ingsw.client.view.cli.textutil.Color;

import java.util.concurrent.TimeUnit;

public class Timer{

    private Timer(){}

    public static void showSecondsOnCLI(CLI cli, String baseText, int seconds){
                cli.blockTitleChanges();

                for(int time=seconds; time>0; time--){

                    String titleString = baseText + time;
                    Title title = new Title(titleString, Color.GREEN);
                    cli.setTitle(title);
                    cli.show();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                cli.enableTitleChanges();
            }
}
