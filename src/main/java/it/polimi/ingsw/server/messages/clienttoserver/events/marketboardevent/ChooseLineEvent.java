package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.State;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event} created when player wants to pick resources from {@link it.polimi.ingsw.server.model.market.MarketBoard MarketBoard}
 * when {@link it.polimi.ingsw.server.model.player.State#SHOWING_MARKET_RESOURCES SHOWING_MARKET_RESOURCES} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class ChooseLineEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseLineEvent implements Validable {

    /**
     * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event} constructor invoked when {@link State#SHOWING_MARKET_RESOURCES SHOWING_MARKET_RESOURCES}
     * phase action is performed.
     *
     * @param rowNumber int value matching an encoded {@link MarketLine}
     */
    public ChooseLineEvent(int rowNumber) {
        super(rowNumber);
    }

    @Override
    public boolean validate(GameModel model) {
        initializeMiddlePhaseEventValidation(model);
        return isGameStarted(model) && validateRowNumber();
    }

    /**
     * @return true if {@link ChooseLineEvent#chosenRow} matches an existing {@link MarketLine}, otherwise false.
     */
    private boolean validateRowNumber(){
       return !MarketLine.fromInt(chosenRow).equals(MarketLine.INVALID_LINE);
    }

}
