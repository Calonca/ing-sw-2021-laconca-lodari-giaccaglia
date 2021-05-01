package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.abstractview.View;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;

import java.beans.PropertyChangeEvent;
import java.util.Scanner;

public class CreateMatchView extends it.polimi.ingsw.client.view.abstractview.CreateMatchView implements CLIView {


    /**
     * The user will be asked to insert a valid number and a nickname. There are no checks for nicknames yet
     *
     * @implNote This method shall exit as soon as possible after stopInteraction()
     * is called (from another thread).
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers=0;
        String nickname;
        CLIBuilder.scroll();

        boolean acquisition=false;
        do
        {
            try
            {
                System.out.println("Creating a match between 0 and 4 players\nPlease insert the number of players: ");
                numberOfPlayers = Integer.parseInt(scanner.nextLine());
                if (numberOfPlayers<5&&numberOfPlayers>0)
                    acquisition=true;
                else
                    System.out.println(Color.colorString("Insert a number between 0 and 4!",Color.ANSI_RED));
                CLIBuilder.scroll();

            }
            catch (NumberFormatException e)
            {
                System.out.println(Color.colorString("Insert a NUMBER!",Color.ANSI_RED));
                CLIBuilder.scroll();

            }
        }while (!acquisition);




        System.out.println("Please insert your nickname: ");
        nickname = scanner.nextLine();



        getClient().getServerHandler().sendCommandMessage(new CreateMatchRequest(numberOfPlayers,nickname));
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
