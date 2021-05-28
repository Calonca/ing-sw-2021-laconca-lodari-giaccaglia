package it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent;

import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.states.State;

/**
 * Client side {@link it.polimi.ingsw.network.messages.clienttoserver.events.Event} created when player has to choose a resource for {@link it.polimi.ingsw.server.model.market.Marble#WHITE WHITEMARBLE} conversion
 * when {@link State#CHOOSING_WHITEMARBLE_CONVERSION CHOOSING_WHITEMARBLE_CONVERSION} game turn action is performed and
 * has to be processed to accomplish server-side client validation.
 */
public class ChooseWhiteMarbleConversionEvent extends it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent implements Validable {


    @Override
    public boolean validate(GameModel model) {
        return verifyWhiteMarbleInPickedLine(model) && validateResourceNumber();
    }

    /**
     * @return true if previously selected {@link it.polimi.ingsw.server.model.market.MarketLine MarketLine} contains any {@link Marble#WHITE WHITEMARBLE},
     * otherwise false.
     */
    private boolean verifyWhiteMarbleInPickedLine(GameModel gameModel){
        return gameModel.areThereWhiteMarblesInPickedLine() && gameModel.getNumberOfWhiteMarblesInPickedLine()>0;
    }

    /**
     * @return true if chosen {@link ChooseWhiteMarbleConversionEvent#resourceNumber} is a valid value corresponding to
     * an existing {@link Resource}, otherwise false.
     */
    private boolean validateResourceNumber(){
        return !Resource.fromIntFixed(resourceNumber).equals(Resource.EMPTY);
    }

    public int getResourceNumber(){
        return resourceNumber;
    }

}
