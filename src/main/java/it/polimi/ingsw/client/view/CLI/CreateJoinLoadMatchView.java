package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.abstractview.View;
import it.polimi.ingsw.server.messages.clienttoserver.JoinMatchRequest;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class CreateJoinLoadMatchView extends it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchView implements CLIView {


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

        //Todo this sleep exists to wait for the match data message, replace with listener
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Adds matches and saved matches to the CLIBuilder
        getCommonData().matchesData.ifPresent(
                (o)-> Arrays.stream(o).forEach(this::addOption)
        );

        //Adds new match options to the CLIBuilder
        Runnable r = ()->getClient().transitionToView(new CreateMatchView());
        getCliBuilder().addOption(CLIPos.CENTER,new Option("New Match","",r));

        getCliBuilder().display();

        String s="a";
        //Loop for choices
        //Todo move some of the selection code to CLIBuilder to reduce code duplication
        int choice=0;
        do  {
                try
                {
                    System.out.println(choice);
                    System.out.println("Select choice");
                    choice = Integer.parseInt(scanner.nextLine());
                    getCliBuilder().display();

                    if(getCommonData().matchesData.isPresent()&&choice>getCommonData().matchesData.get().length)
                            System.out.println("Please insert an existing match number");
                    else
                    {
                            System.out.println("Press send to confirm or you will be asked to select another choice");
                            s=scanner.nextLine();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println("Insert a NUMBER!");
                }
        }while(!s.isEmpty());
        getCliBuilder().selectOptionAtGlobalPosition(choice);
        getCliBuilder().performLastChoice();

    }

    private void addOption(Pair<UUID, String[]> uuidPair) {
        Runnable r = () -> getClient().transitionToView(new JoinMatchView(uuidPair.getKey()));
        getCliBuilder().addOption(
                CLIPos.CENTER,
                new Option(
                        uuidPair.getKey().toString(),
                        Arrays.toString(uuidPair.getValue()),
                        r)
        );
    }

}
