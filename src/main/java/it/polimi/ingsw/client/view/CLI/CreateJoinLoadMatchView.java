package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
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

        //Adds matches and saved matches to the CLIBuilder
        getCommonData().matchesData.ifPresent(
                (o)-> Arrays.stream(o).forEach(this::addOption)
        );
        //Adds new match options to the CLIBuilder
        Runnable r = ()->getClient().transitionToView(new CreateMatchView());
        getCliBuilder().addOption(CLIPos.CENTER,new Option("New Match","",r));

        getCliBuilder().display();

        //Loop for choices
        //Todo move some of the selection code to CLIBuilder to reduce code duplication
        while (true) {
            if (shouldStopInteraction()) return;
            int choice;
            try {
                System.out.println("Select choice");
                choice = Integer.parseInt(scanner.nextLine());
                if (shouldStopInteraction()) return;
                getCliBuilder().selectOptionAtGlobalPosition(choice);
                getCliBuilder().display();
                try {
                    System.out.println("Press send to confirm or select another choice");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (shouldStopInteraction()) return;
                    getCliBuilder().selectOptionAtGlobalPosition(choice);
                    getCliBuilder().display();
                } catch (NumberFormatException e){
                    break;
                }
            } catch (NumberFormatException ignored){
            }
        }
        if (shouldStopInteraction()) return;
        getCliBuilder().performLastChoice();

    }

    /**
     * Update observed values
     */
    @Override
    public void update() {
        super.update();
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
