package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;

/**
 * Displays a list of leaders to choose from.
 * Todo make selection work.
 */
public class LeadersBody extends Body {
    SETUP_PHASE setup_phase;
    public LeadersBody(SETUP_PHASE setup_phase) {
        this.setup_phase = setup_phase;
    }

    @Override
    public String toString() {
        return  new GsonBuilder()
                .setPrettyPrinting()
                .create().toJson(setup_phase);
    }
}
