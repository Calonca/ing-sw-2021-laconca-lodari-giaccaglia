package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.abstractview.View;
import it.polimi.ingsw.server.messages.clienttoserver.JoinMatchRequest;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class MainMenuView extends View implements CLIView {


    /**
     * The main method of the view. Handles user interaction. User interaction
     * is considered ended when this method exits.
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        Runnable r;

        r = ()->getClient().transitionToView(new CreateJoinLoadMatchView());
        getCLIBuilder().addOption(CLIPos.CENTER,new Option("Browse matches","Join or create",r));

        r = ()->getClient().transitionToView(new CreateMatchView());
        getCLIBuilder().addOption(CLIPos.CENTER,new Option("Create a match","One or more players",r));

        r = ()->getClient().transitionToView(new GenericWait());
        getCLIBuilder().addOption(CLIPos.CENTER,new Option("Edit Cards","Costs and effects",r));

        r = ()->getClient().transitionToView(new GenericWait());
        getCLIBuilder().addOption(CLIPos.CENTER,new Option("Exit",":*",r));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getCLIBuilder().display();

        String s="k";
        //Loop for choices
        //Todo move some of the selection code to CLIBuilder to reduce code duplication
        int choice=0;
        do  {
            try
            {
                System.out.println(Color.colorString("Please insert a number",Color.ANSI_GREEN));
                choice = Integer.parseInt(scanner.nextLine());
                System.out.println(Color.colorString("Please insert Enter to confirm or any key to cancel",Color.ANSI_GREEN));
                s=scanner.nextLine();
            }
            catch (NumberFormatException e){
                System.out.println(Color.colorString("Insert a NUMBER!",Color.ANSI_RED));
            }
        }while(!s.isEmpty());
        getCLIBuilder().selectOptionAtGlobalPosition(choice);
        getCLIBuilder().performLastChoice();

    }

    public void propertyChange(PropertyChangeEvent event) {

    }

}
