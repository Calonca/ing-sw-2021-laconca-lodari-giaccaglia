package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import java.util.concurrent.TimeUnit;

public class Timer{

    public static void showSecondsOnCLI(CLI cli, String baseText){
                cli.blockTitleChanges();

                for(int time=5; time>=0; time--){

                    String title = baseText + time;
                    String coloredTitle = Color.colorString(title, Color.GREEN);
                    cli.setTitleWhenBlocked(coloredTitle);
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
