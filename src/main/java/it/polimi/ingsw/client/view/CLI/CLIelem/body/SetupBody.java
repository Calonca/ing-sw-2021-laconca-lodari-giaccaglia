package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;

public class SetupBody extends CLIelem {
    SETUP_PHASE setup_phase;
    public SetupBody(SETUP_PHASE setup_phase) {
        this.setup_phase = setup_phase;
    }

    @Override
    public String toString() {
        return  new GsonBuilder()
                .setPrettyPrinting()
                .create().toJson(setup_phase);
    }
}
