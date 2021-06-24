package it.polimi.ingsw.client.view.CLI.middle;

import it.polimi.ingsw.client.view.CLI.commonViews.CLILeadersBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

public class MiddleLeadersCLI extends MiddlePhaseCLI{

        private ViewBuilder nextViewBuilder;
        private int playerIndex;

        public MiddleLeadersCLI(ViewBuilder nextViewBuilder, int playerIndex){
            this.nextViewBuilder = nextViewBuilder;
            this.playerIndex = playerIndex;
        }

        @Override
        public void run() {
            CLILeadersBuilder.buildView(nextViewBuilder, playerIndex);
        }
    }

