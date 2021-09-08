package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.controller.strategy.ExecuteLeaderAction;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.controller.strategy.Middle;
import it.polimi.ingsw.server.controller.strategy.Setup;
import it.polimi.ingsw.server.controller.strategy.cardmarket.AcquiringDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.ChoosingSpaceForDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.PayingResourcesForDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.production.ChoosingResourceForProduction;
import it.polimi.ingsw.server.controller.strategy.production.FinalProductionStrategy;
import it.polimi.ingsw.server.controller.strategy.production.TogglingForProduction;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.ChoosingMarketBonus;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.DiscardingResources;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.MovingResource;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.PuttingBallOnLine;
import it.polimi.ingsw.server.messages.clienttoserver.events.InitialOrFinalPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.MiddlePhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseLineEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.ChooseWhiteMarbleConversionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.DiscardResourcesEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.MoveResourceEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.utils.Deserializator;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class StatesTransitionTable {

    /**
     * The String is the name of the {@link Validable}
     */
    private Map<State, Map<String, GameStrategy>> table;
    public static final String SINGLE_PLAYER_TABLE_FILE = "SinglePlayerStatesTransitionTable.json";
    public static final String MULTI_PLAYER_TABLE_FILE = "MultiPlayerStatesTransitionTable.json";


    /**
     * Returns name of a class
     */
    private static String name(Class classInstance){
        return classInstance.getSimpleName();
    }


    /**
     * Returns the {@link GameStrategy} to use at the given {@link State} after the given {@link Validable event}.
     */
    public GameStrategy getStrategy(State state, Validable event){
        return table.get(state).get(name(event.getClass()));
    }

    /**
     * Returns the single player table when the game is single player and the multiplayer table when it is multiplayer<br>
     * @param isSinglePlayer a boolean indicating if the game is single player.
     */
    public static StatesTransitionTable fromIsSinglePlayer(boolean isSinglePlayer) {
        return isSinglePlayer ? Deserializator.deserializeSinglePlayerStatesTransitionTable()
                : Deserializator.deserializeMultiPlayerStatesTransitionTable();
    }


    public static StatesTransitionTable setupCommonStatesTransitionTable(){

        StatesTransitionTable statesTransitionTable = new StatesTransitionTable();
        statesTransitionTable.table = new EnumMap<>(State.class);
        HashMap<String, GameStrategy> eventsAndStrategy = new HashMap<>();

        // player can freely move warehouse resources during idle phase
        //Idle Phase
        eventsAndStrategy.put(name(MoveResourceEvent.class), new MovingResource());
        statesTransitionTable.table.put(State.IDLE, eventsAndStrategy);

        eventsAndStrategy = new HashMap<>();


        //Setup Phase
        eventsAndStrategy.put(name(SetupPhaseEvent.class), new Setup());
        statesTransitionTable.table.put(State.SETUP_PHASE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //Middle Phase
        eventsAndStrategy.put(name(MiddlePhaseEvent.class), new Middle());
        eventsAndStrategy.put(name(MoveResourceEvent.class), new MovingResource());
        statesTransitionTable.table.put(State.MIDDLE_PHASE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--- Activate the Production --//

        //Select/Deselect Production
        eventsAndStrategy.put(name(ToggleProductionAtPosition.class), new TogglingForProduction());

        //Produce or go back to Middle Phase
        eventsAndStrategy.put(name(FinalProductionPhaseEvent.class), new FinalProductionStrategy());

        statesTransitionTable.table.put(State.CHOOSING_PRODUCTION, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //Choosing Resource For Production
        eventsAndStrategy.put(name(ChooseResourcesForProductionEvent.class), new ChoosingResourceForProduction());
        statesTransitionTable.table.put(State.CHOOSING_RESOURCE_FOR_PRODUCTION, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--- Take Resources from Market --//

        //Choosing Market Line
        eventsAndStrategy.put(name(ChooseLineEvent.class),new PuttingBallOnLine());
        statesTransitionTable.table.put(State.CHOOSING_MARKET_LINE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing WhiteMarble Conversion
        eventsAndStrategy.put(name(ChooseWhiteMarbleConversionEvent.class), new ChoosingMarketBonus());
        statesTransitionTable.table.put(State.CHOOSING_WHITEMARBLE_CONVERSION, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Position For Resources
        eventsAndStrategy.put(name(MoveResourceEvent.class), new MovingResource());
        eventsAndStrategy.put(name(DiscardResourcesEvent.class), new DiscardingResources());
        statesTransitionTable.table.put(State.CHOOSING_POSITION_FOR_RESOURCES, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--- Buy one Development Card --//

        //Choosing Development Card
        eventsAndStrategy.put(name(ChooseCardEvent.class), new AcquiringDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_DEVELOPMENT_CARD, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Resources For Development Card
        eventsAndStrategy.put(name(ChooseResourceForCardShopEvent.class), new PayingResourcesForDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_RESOURCES_FOR_DEVCARD, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //Choosing Position For Development Card
        eventsAndStrategy.put(name(ChooseCardPositionEvent.class), new ChoosingSpaceForDevelopmentCard());
        statesTransitionTable.table.put(State.CHOOSING_POSITION_FOR_DEVCARD, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //---InitialOrFinalStrategy Leader Action --//

        eventsAndStrategy.put(name(InitialOrFinalPhaseEvent.class), new ExecuteLeaderAction());
        statesTransitionTable.table.put(State.INITIAL_PHASE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();


        //--Final Leader Action --//

        eventsAndStrategy.put(name(InitialOrFinalPhaseEvent.class), new ExecuteLeaderAction());
        statesTransitionTable.table.put(State.FINAL_PHASE, eventsAndStrategy);
        eventsAndStrategy = new HashMap<>();

        //--End Phase Action --//
        eventsAndStrategy.put(null, null);
        statesTransitionTable.table.put(State.END_PHASE, eventsAndStrategy);

        return statesTransitionTable;
    }


}
