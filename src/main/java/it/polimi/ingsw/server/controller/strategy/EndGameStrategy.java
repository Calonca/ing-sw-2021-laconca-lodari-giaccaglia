package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class EndGameStrategy implements GameStrategy{

    private final List<Element> elementsToUpdate = new ArrayList<>();

    @Override
    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event) {

        elementsToUpdate.add(Element.END_GAME_INFO);
        
        return new Pair<>(State.END_PHASE, elementsToUpdate);

    }


}
