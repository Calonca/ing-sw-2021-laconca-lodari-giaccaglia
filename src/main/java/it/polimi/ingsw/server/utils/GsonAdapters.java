package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.network.jsonUtils.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.server.controller.strategy.*;
import it.polimi.ingsw.server.controller.strategy.cardmarket.AcquiringDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.ChoosingSpaceForDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.cardmarket.PayingResourcesForDevelopmentCard;
import it.polimi.ingsw.server.controller.strategy.leader.ActivatingLeader;
import it.polimi.ingsw.server.controller.strategy.leader.DiscardingLeader;
import it.polimi.ingsw.server.controller.strategy.production.ChoosingResourceForProduction;
import it.polimi.ingsw.server.controller.strategy.production.FinalProductionStrategy;
import it.polimi.ingsw.server.controller.strategy.production.TogglingForProduction;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.ChoosingMarketBonus;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.DiscardingResources;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.MovingResource;
import it.polimi.ingsw.server.controller.strategy.resourcemarket.PuttingBallOnLine;
import it.polimi.ingsw.server.messages.clienttoserver.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.marketboardevent.*;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent;
import it.polimi.ingsw.server.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.server.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.server.model.player.board.Depot;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import it.polimi.ingsw.server.model.player.board.WarehouseDepot;
import it.polimi.ingsw.server.model.player.leaders.*;

public class GsonAdapters {


    public static final RuntimeTypeAdapterFactory<Leader> gsonLeaderAdapter = gsonLeaderAdapter();
    public static final RuntimeTypeAdapterFactory<Depot> gsonDepotAdapter = gsonDepotAdapter();
    public static final RuntimeTypeAdapterFactory<GameStrategy> gsonStrategyAdapter = gsonStrategyAdapter();
    public static final RuntimeTypeAdapterFactory<Validable> gsonEventMessageAdapter = gsonEventMessageAdapter();
    public static final RuntimeTypeAdapterFactory<ServerMessage> gsonServerMessageAdapter = gsonServerMessageAdapter();

    private static RuntimeTypeAdapterFactory<Leader> gsonLeaderAdapter() {


        RuntimeTypeAdapterFactory<Leader> gsonToLeaderListAdapter = RuntimeTypeAdapterFactory.of(Leader.class);
        //Register here all the Leader types
        gsonToLeaderListAdapter.registerSubtype(DepositLeader.class);
        gsonToLeaderListAdapter.registerSubtype(MarketLeader.class);
        gsonToLeaderListAdapter.registerSubtype(ProductionLeader.class);
        gsonToLeaderListAdapter.registerSubtype(DevelopmentDiscountLeader.class);

        return gsonToLeaderListAdapter;

    }

    private static RuntimeTypeAdapterFactory<Depot> gsonDepotAdapter() {

        RuntimeTypeAdapterFactory<Depot> gsonToDepotAdapter = RuntimeTypeAdapterFactory.of(Depot.class);

        gsonToDepotAdapter.registerSubtype(LeaderDepot.class);
        gsonToDepotAdapter.registerSubtype(WarehouseDepot.class);

        return gsonToDepotAdapter;
    }

    private static RuntimeTypeAdapterFactory<GameStrategy> gsonStrategyAdapter() {

        RuntimeTypeAdapterFactory<GameStrategy> strategyAdapter = RuntimeTypeAdapterFactory.of(GameStrategy.class);

        strategyAdapter.registerSubtype(IDLE.class);
        strategyAdapter.registerSubtype(ExecuteLeaderAction.class);
        strategyAdapter.registerSubtype(Middle.class);
        strategyAdapter.registerSubtype(ActivatingLeader.class);
        strategyAdapter.registerSubtype(DiscardingLeader.class);
        strategyAdapter.registerSubtype(AcquiringDevelopmentCard.class);
        strategyAdapter.registerSubtype(ChoosingSpaceForDevelopmentCard.class);
        strategyAdapter.registerSubtype(PayingResourcesForDevelopmentCard.class);
        strategyAdapter.registerSubtype(ChoosingResourceForProduction.class);
        strategyAdapter.registerSubtype(MovingResource.class);
        strategyAdapter.registerSubtype(ChoosingMarketBonus.class);
        strategyAdapter.registerSubtype(DiscardingResources.class);
        strategyAdapter.registerSubtype(PuttingBallOnLine.class);
        strategyAdapter.registerSubtype(TogglingForProduction.class);
        strategyAdapter.registerSubtype(Setup.class);
        strategyAdapter.registerSubtype(EndGameStrategy.class);
        strategyAdapter.registerSubtype(FinalProductionStrategy.class);

        return strategyAdapter;
    }

    private static RuntimeTypeAdapterFactory<Validable> gsonEventMessageAdapter(){

        RuntimeTypeAdapterFactory<Validable> eventMessageAdapter = RuntimeTypeAdapterFactory.of(Validable.class);

        eventMessageAdapter.registerSubtype(TestEvent.class);

        eventMessageAdapter.registerSubtype(MiddlePhaseEvent.class);
        eventMessageAdapter.registerSubtype(InitialOrFinalPhaseEvent.class);
        eventMessageAdapter.registerSubtype(EndMiddlePhaseEvent.class);

        eventMessageAdapter.registerSubtype(SetupPhaseEvent.class);

        eventMessageAdapter.registerSubtype(ToggleProductionAtPosition.class);
        eventMessageAdapter.registerSubtype(ChooseResourcesForProductionEvent.class);
        eventMessageAdapter.registerSubtype(FinalProductionPhaseEvent.class);

        eventMessageAdapter.registerSubtype(ChooseLineEvent.class);
        eventMessageAdapter.registerSubtype(ChooseWhiteMarbleConversionEvent.class);
        eventMessageAdapter.registerSubtype(DiscardResourcesEvent.class);
        eventMessageAdapter.registerSubtype(MarketBoardEvent.class);
        eventMessageAdapter.registerSubtype(MoveResourceEvent.class);

        eventMessageAdapter.registerSubtype(ChooseCardEvent.class);
        eventMessageAdapter.registerSubtype(ChooseCardPositionEvent.class);
        eventMessageAdapter.registerSubtype(ChooseResourceForCardShopEvent.class);



        return eventMessageAdapter;
    }

    private static RuntimeTypeAdapterFactory<ServerMessage> gsonServerMessageAdapter(){

        RuntimeTypeAdapterFactory<ServerMessage> gsonToServerMessageAdapter  = RuntimeTypeAdapterFactory.of(ServerMessage.class);

        gsonToServerMessageAdapter.registerSubtype(CreateMatchRequest.class);
        gsonToServerMessageAdapter.registerSubtype(JoinMatchRequest.class);
        gsonToServerMessageAdapter.registerSubtype(EventMessage.class);
        gsonToServerMessageAdapter.registerSubtype(SendNickname.class);

        return gsonToServerMessageAdapter;

    }



}