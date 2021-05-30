package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.MiddlePhaseCLI;
import it.polimi.ingsw.client.view.GUI.MiddlePhaseGUI;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.MiddlePhaseEvent;

import java.beans.PropertyChangeEvent;

import static it.polimi.ingsw.client.simplemodel.State.IDLE;

public abstract class MiddlePhaseViewBuilder extends ViewBuilder {
    //Todo should be new MiddlePhaseEvent(1),2,3
    public enum Choice{
        RESOURCE_MARKET(()->getClient().getServerHandler().sendCommandMessage(new EventMessage(new MiddlePhaseEvent(0)))),
        CARD_SHOP(()->getClient().getServerHandler().sendCommandMessage(new EventMessage(new MiddlePhaseEvent(1)))),
        PRODUCTION(()->getClient().getServerHandler().sendCommandMessage(new EventMessage(new MiddlePhaseEvent(2))));
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

    public void sendMessage(Choice c){
        c.getR().run();
    }

    public static void middlePhaseCommonTransition(PropertyChangeEvent evt){
        String propertyName = evt.getPropertyName();
        if (IDLE.name().equals(propertyName)) {
            getClient().changeViewBuilder(IDLEViewBuilder.getBuilder(getClient().isCLI()));
        }else if (State.FINAL_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(InitialOrFinalPhaseViewBuilder.getBuilder(getClient().isCLI(), false));
        }else if (State.END_PHASE.name().equals(propertyName)) {
            getClient().changeViewBuilder(WinLooseBuilder.getBuilder(getClient().isCLI()));
        }else
            System.out.println(getThisPlayerCache().getCurrentState()+" received: " + evt.getPropertyName() + JsonUtility.serialize(evt.getNewValue()));

    }
}
