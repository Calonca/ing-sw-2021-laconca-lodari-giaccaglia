package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * On turn start, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or a normal action
 */
public class IDLE implements GameStrategy {

    List<Element> elementsToUpdate = new ArrayList<>();

    public Pair<State, List<Element>> execute(GameModel gamemodel, Validable event)
    {

        if(gamemodel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LastTurn))
            return new Pair<>(State.IDLE, elementsToUpdate);

        else if(gamemodel.getMacroGamePhase().equals(GameModel.MacroGamePhase.GameEnded)) {
            elementsToUpdate.add(Element.EndGameInfo);
            return new Pair<>(State.END_PHASE, elementsToUpdate);
        }


        if(gamemodel.getCurrentPlayer().anyLeaderPlayable()) {

            if(gamemodel.isSinglePlayer()) {
                executeActionTokenStrategy(gamemodel, elementsToUpdate);
                return  VaticanReportStrategy.addFaithPointsToPlayer(gamemodel, elementsToUpdate);
            }

            gamemodel.getCurrentPlayer().setCurrentState(State.INITIAL_PHASE);
            return new Pair<>(State.INITIAL_PHASE, elementsToUpdate);
        }

        else{

            if(gamemodel.isSinglePlayer()) {
                executeActionTokenStrategy(gamemodel, elementsToUpdate);
                return  VaticanReportStrategy.addFaithPointsToPlayer(gamemodel, elementsToUpdate);
            }

            gamemodel.getCurrentPlayer().setCurrentState(State.MIDDLE_PHASE);
            return new Pair<>(State.MIDDLE_PHASE, elementsToUpdate);
        }

    }

    private void executeActionTokenStrategy(GameModel gameModel, List<Element> elementsToUpdate){
        gameModel.activateSoloActionToken();
        elementsToUpdate.add(Element.SimpleSoloActionToken);
        elementsToUpdate.add(Element.SimpleCardShop);
        elementsToUpdate.add(Element.SimpleFaithTrack);
        elementsToUpdate.add(Element.VaticanReportInfo);
    }

}