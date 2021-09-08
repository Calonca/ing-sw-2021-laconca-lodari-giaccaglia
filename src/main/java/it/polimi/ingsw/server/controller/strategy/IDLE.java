package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.controller.strategy.cardmarket.MarketCardsCheck;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.solo.SoloActionToken;
import it.polimi.ingsw.server.model.states.State;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * On turn start, if there is the possibility to play a Leader, the user will be able to decide wether to play
 * or a normal action
 */
public class IDLE implements GameStrategy {

    private final List<Element> elementsToUpdate = new ArrayList<>();
    GameModel gameModel;
    public static boolean isSinglePlayerInFirstIDLE = true;

    public Pair<State, List<Element>> execute(GameModel gameModel, Validable event)
    {

        this.gameModel = gameModel;

        if(gameModel.getMacroGamePhase().equals(GameModel.MacroGamePhase.LAST_TURN) && gameModel.getPlayerIndex(gameModel.getCurrentPlayer()) == gameModel.getIndexOfPlayerWithInkWell()) {
                gameModel.setMacroGamePhase(GameModel.MacroGamePhase.GAME_ENDED);
                elementsToUpdate.add(Element.END_GAME_INFO);
                return new Pair<>(State.END_PHASE, elementsToUpdate);
        }


        if(gameModel.getCurrentPlayer().anyLeaderPlayable())
           return handleIDLETransition(State.INITIAL_PHASE);

        else
          return handleIDLETransition(State.MIDDLE_PHASE);

    }

    private Pair<State, List<Element>> handleIDLETransition(State nextPossibleState){

        if(gameModel.isSinglePlayer() && !isSinglePlayerInFirstIDLE) {

            executeActionTokenStrategy(gameModel, elementsToUpdate);
            SoloActionToken executedToken = gameModel.getLastActivatedSoloActionToken();
            if(executedToken.equals(SoloActionToken.ADD2FAITH) || executedToken.equals(SoloActionToken.SHUFFLE_ADD1FAITH)) {

                Pair<State, List<Element>> tokenPair =  VaticanReportStrategy.addFaithPointsStrategy(gameModel, elementsToUpdate);
                if(tokenPair.getKey().equals(State.END_PHASE)) {
                    return tokenPair;
                }
            }
            else if(gameModel.isSinglePlayer() && (gameModel.isSomeDevCardColourOutOfStock() || gameModel.getCurrentPlayer().getPersonalBoard().playerHasSevenCards()))
                return MarketCardsCheck.checkMarketCards(gameModel, elementsToUpdate);

        }

        return new Pair<>(nextPossibleState, elementsToUpdate);

    }

    private void executeActionTokenStrategy(GameModel gameModel, List<Element> elementsToUpdate){

        gameModel.activateSoloActionToken();
        elementsToUpdate.add(Element.SIMPLE_SOLO_ACTION_TOKEN);
        elementsToUpdate.add(Element.SIMPLE_CARD_SHOP);
        elementsToUpdate.add(Element.SIMPLE_FAITH_TRACK);

    }

}