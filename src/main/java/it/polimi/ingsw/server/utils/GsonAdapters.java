package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.network.assets.leaders.*;
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
import it.polimi.ingsw.server.model.player.board.Depot;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import it.polimi.ingsw.server.model.player.board.WarehouseDepot;
import it.polimi.ingsw.server.model.player.leaders.*;

public class GsonAdapters {


    public static final RuntimeTypeAdapterFactory<Leader> gsonLeaderAdapter = gsonLeaderAdapter();
    public static final RuntimeTypeAdapterFactory<NetworkLeaderCard> gsonNetworkLeaderAdapter = gsonNetworkLeaderAdapter();
    public static final RuntimeTypeAdapterFactory<Depot> gsonDepotAdapter = gsonDepotAdapter();
    public static final RuntimeTypeAdapterFactory<GameStrategy> gsonStrategyAdapter = gsonStrategyAdapter();


    private static RuntimeTypeAdapterFactory<Leader> gsonLeaderAdapter() {


        RuntimeTypeAdapterFactory<Leader> gsonToLeaderListAdapter = RuntimeTypeAdapterFactory.of(Leader.class, "type");

        //Register here all the Leader types
        gsonToLeaderListAdapter.registerSubtype(DepositLeader.class, DepositLeader.class.getName());
        gsonToLeaderListAdapter.registerSubtype(MarketLeader.class, MarketLeader.class.getName());
        gsonToLeaderListAdapter.registerSubtype(ProductionLeader.class, ProductionLeader.class.getName());
        gsonToLeaderListAdapter.registerSubtype(DevelopmentDiscountLeader.class, DevelopmentDiscountLeader.class.getName());

        return gsonToLeaderListAdapter;

    }


    private static RuntimeTypeAdapterFactory<NetworkLeaderCard> gsonNetworkLeaderAdapter() {


        RuntimeTypeAdapterFactory<NetworkLeaderCard> gsonToNetworkLeaderListAdapter = RuntimeTypeAdapterFactory.of(NetworkLeaderCard.class);

        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkDepositLeaderCard.class);
        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkMarketLeaderCard.class);
        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkProductionLeaderCard.class);
        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkDevelopmentDiscountLeaderCard.class);

        return gsonToNetworkLeaderListAdapter;

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


}