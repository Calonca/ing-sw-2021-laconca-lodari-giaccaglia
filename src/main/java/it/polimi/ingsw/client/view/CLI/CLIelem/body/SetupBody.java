package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;

public class SetupBody extends CLIelem {
    String setup_phase;
    public SetupBody(String setup_phase) {
        this.setup_phase = setup_phase;
    }

    @Override
    public String toString() {
        return setup_phase;
    }
    /*
        @Override
    public String toString() {
        return  new GsonBuilder()
                .setPrettyPrinting()
                .create().toJson(setup_phase);
    }
     */

    @Override
    public int horizontalSize() {
        return -1;
    }
}
