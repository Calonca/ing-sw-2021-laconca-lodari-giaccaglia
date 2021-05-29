package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.MiddlePhaseCLI;
import it.polimi.ingsw.client.view.GUI.MiddlePhaseGUI;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;

public abstract class MiddlePhaseViewBuilder extends ViewBuilder {
    //Todo should be new MiddlePhaseEvent(1),2,3
    public enum Choice{
        PRODUCTION(()->getClient().getServerHandler().sendCommandMessage(new EventMessage(new MiddlePhaseEvent()))),
        RESOURCE_MARKET(()->getClient().getServerHandler().sendCommandMessage(new EventMessage(new MiddlePhaseEvent()))),
        CARD_SHOP(()->getClient().getServerHandler().sendCommandMessage(new EventMessage(new MiddlePhaseEvent())));
        Runnable r;

        public Runnable getR() {
            return r;
        }

        Choice(Runnable r){
            this.r = r;
        }
    }

    public static ViewBuilder getBuilder(boolean isCLI) {
        if (isCLI) return new MiddlePhaseCLI();
        else return new MiddlePhaseGUI();
    }

    public static void sendMessage(Choice c){
        c.getR().run();
    }
}
