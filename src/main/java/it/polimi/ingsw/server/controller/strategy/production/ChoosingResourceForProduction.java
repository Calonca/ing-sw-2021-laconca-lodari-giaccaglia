package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This implementation allows the user to sequentially select the input and output optional choices. The inputs are
 * chosen first, and it has already been calculated that there will be enough basic resources to complete the choice.
 * The user can reset mid-choice, allowing them to perform it from the start again.
 */
public class ChoosingResourceForProduction implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event){

        int[] input={1,2,3,4};
        int[] output={1,2,3,4};

        if(gamemodel.getCurrentPlayer().getPersonalBoard().firstProductionSelectedWithChoice().get().choiceCanBeMadeOnInput())
            for(int i=0;i< input.length;i++)
                for(int k=0;k<input[k];k++)
                    gamemodel.getCurrentPlayer().getPersonalBoard().performChoiceOnInput(i);
        for(int i=0;i< output.length;i++)
            for(int k=0;k<output[k];k++)
                gamemodel.getCurrentPlayer().getPersonalBoard().performChoiceOnInput(i);

        //Todo if no elements left return final strategy
        elementsToUpdate.add(Element.SimpleProductions);

        return new Pair<>(State.CHOOSING_PRODUCTION, elementsToUpdate);


    }
}
