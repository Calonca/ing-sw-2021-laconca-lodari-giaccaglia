package it.polimi.ingsw.client.abstractview.CLI;

/**
 * Common methods for all CLI classes
 */
public interface CLIView {

    static void cleanConsole() {
        for (int i = 0; i < 40; i++) {
            System.out.println();
        }
    }
}
