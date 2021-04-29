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
        //Loop for choices
        //Todo move some of the selection code to CLIBuilder to reduce code duplication
        while (!shouldStopInteraction()) {
            getCLIBuilder().resetCLIelems();
            //Adds matches and saved matches to the CLIBuilder
            getClient().getCommonData().getMatchesData().ifPresent(
                    (o)-> Arrays.stream(o).forEach(this::addOption)
            );
            //Adds new match options to the CLIBuilder
            Runnable r = ()->getClient().transitionToView(new CreateMatchView());
            getCLIBuilder().addOption(CLIPos.CENTER,new Option("New Match","",r));

            getCLIBuilder().display();

            if (shouldStopInteraction()) return;
            int choice;
            try {
                System.out.println("Select choice");
                choice = Integer.parseInt(scanner.nextLine());
                if (shouldStopInteraction()) return;
                getCLIBuilder().selectOptionAtGlobalPosition(choice);
                getCLIBuilder().display();
                try {
                    System.out.println("Press send to confirm or select another choice");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (shouldStopInteraction()) return;
                    getCLIBuilder().selectOptionAtGlobalPosition(choice);
                    getCLIBuilder().display();
                } catch (NumberFormatException e){
                    break;
                }
            } catch (NumberFormatException ignored){
            }
        }
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
