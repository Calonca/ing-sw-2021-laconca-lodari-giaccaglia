package it.polimi.ingsw.server.controller.strategy.actiontoken;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ActionTokenStrategy implements GameStrategy {

    @Override
    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        List<Element> elementsToUpdate = new ArrayList<>();
        gamemodel.activateSoloActionToken();
        elementsToUpdate.add(Element.VaticanReportInfo);
        return null;
    }


}
