package it.polimi.ingsw.client.view.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
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

        String s="a";
        //Todo move some of the selection code to CLIBuilder to reduce code duplication
        int choice=0;
        do  {
            getCLIBuilder().resetCLIelems();

            //Adds matches and saved matches to the CLIBuilder
            getClient().getCommonData().getMatchesData().ifPresent(
                    (o)-> Arrays.stream(o).forEach(this::addOption));


            //Adds new match options to the CLIBuilder
            Runnable r = ()->getClient().transitionToView(new CreateMatchView());
            getCLIBuilder().addOption(CLIPos.CENTER,new Option("New Match","",r));

            getCLIBuilder().display();
                try
                {
                    System.out.println(Color.colorString("Please insert a match number",Color.ANSI_GREEN));
                    choice = Integer.parseInt(scanner.nextLine());
                    if (shouldStopInteraction()) return;
                    getCLIBuilder().display();

                    if(getCommonData().getMatchesData().isPresent()&&choice>getCommonData().getMatchesData().get().length)
                        System.out.println(Color.colorString("Please insert an existing match number",Color.ANSI_RED));
                    else
                    {
                        getCLIBuilder().selectOptionAtGlobalPosition(choice);
                        getCLIBuilder().display();
                        System.out.println(Color.colorString("Please insert Enter to confirm or any key to cancel",Color.ANSI_GREEN));
                        s=scanner.nextLine();
                    }
                }
                catch (NumberFormatException e){
                    System.out.println(Color.colorString("Insert a NUMBER!",Color.ANSI_RED));
                }
        }while(!s.isEmpty()&&!shouldStopInteraction());
        if (shouldStopInteraction()) return;
        getCLIBuilder().performLastChoice();
    }

    private void addOption(Pair<UUID, String[]> uuidPair) {
        Runnable r = () -> getClient().transitionToView(new JoinMatchView(uuidPair.getKey()));
        getCLIBuilder().addOption(
                CLIPos.CENTER,
                new Option(
                        uuidPair.getKey().toString(),
                        Arrays.toString(uuidPair.getValue()),
                        r)
        );
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("matchesData")) {
            getClient().transitionToView(new CreateJoinLoadMatchView());
        }
    }


}
