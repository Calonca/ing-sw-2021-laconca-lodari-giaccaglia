package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import java.util.concurrent.TimeUnit;

public class Timer{

    public static void showSecondsOnCLI(CLI cli, String baseText, int seconds){
                cli.blockTitleChanges();

                for(int time=seconds; time>=0; time--){

                    String titleString = baseText + time;
                    Title title = new Title(titleString, Color.GREEN);
                    cli.setTitleWhenBlocked(title.toString());
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
