package it.polimi.ingsw.network.jsonUtils;

import it.polimi.ingsw.network.assets.leaders.*;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.events.*;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseCardPositionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.cardshopevent.ChooseResourceForCardShopEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.marketboardevent.*;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ChooseResourcesForProductionEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.FinalProductionPhaseEvent;
import it.polimi.ingsw.network.messages.clienttoserver.events.productionevent.ToggleProductionAtPosition;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import it.polimi.ingsw.network.messages.servertoclient.*;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.*;

public class CommonGsonAdapters {

    public static final RuntimeTypeAdapterFactory<NetworkLeaderCard> gsonNetworkLeaderAdapter = gsonNetworkLeaderAdapter();
    public static RuntimeTypeAdapterFactory<ServerToClientMessage> gsonServerToClientMessageAdapter = gsonServerToClientMessageAdapter();
    public static RuntimeTypeAdapterFactory<ClientToServerMessage> gsonClientToServerMessageAdapter = gsonClientToServerMessageAdapter();
    public static RuntimeTypeAdapterFactory<SimpleModelElement> gsonElementAdapter = gsonElementAdapter();
    public static RuntimeTypeAdapterFactory<Event> gsonEventMessageAdapter = gsonEventMessageAdapter();

    private static RuntimeTypeAdapterFactory<NetworkLeaderCard> gsonNetworkLeaderAdapter() {


        RuntimeTypeAdapterFactory<NetworkLeaderCard> gsonToNetworkLeaderListAdapter = RuntimeTypeAdapterFactory.of(NetworkLeaderCard.class);

        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkDepositLeaderCard.class);
        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkMarketLeaderCard.class);
        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkProductionLeaderCard.class);
        gsonToNetworkLeaderListAdapter.registerSubtype(NetworkDevelopmentDiscountLeaderCard.class);

        return gsonToNetworkLeaderListAdapter;

    }

    private static RuntimeTypeAdapterFactory<ServerToClientMessage> gsonServerToClientMessageAdapter(){

        RuntimeTypeAdapterFactory<ServerToClientMessage> s2cAdapter = RuntimeTypeAdapterFactory.of(ServerToClientMessage.class);

        s2cAdapter.registerSubtype(CreatedMatchStatus.class);
        s2cAdapter.registerSubtype(JoinStatus.class);
        s2cAdapter.registerSubtype(MatchesData.class);
        s2cAdapter.registerSubtype(EventNotValid.class);
        s2cAdapter.registerSubtype(StateInNetwork.class);

        return s2cAdapter;

    }

    private static RuntimeTypeAdapterFactory<ClientToServerMessage> gsonClientToServerMessageAdapter(){

        RuntimeTypeAdapterFactory<ClientToServerMessage> clientToJsonAdapter = RuntimeTypeAdapterFactory.of(ClientToServerMessage.class);

        clientToJsonAdapter.registerSubtype(CreateMatchRequest .class);
        clientToJsonAdapter.registerSubtype(JoinMatchRequest .class);
        clientToJsonAdapter.registerSubtype(EventMessage .class);

        return clientToJsonAdapter;

    }


    private static RuntimeTypeAdapterFactory<Event> gsonEventMessageAdapter(){

        RuntimeTypeAdapterFactory<Event> eventMessageAdapter = RuntimeTypeAdapterFactory.of(Event.class);

        eventMessageAdapter.registerSubtype(ChooseCardEvent.class);
        eventMessageAdapter.registerSubtype(ChooseCardPositionEvent.class);
        eventMessageAdapter.registerSubtype(ChooseResourceForCardShopEvent.class);
        eventMessageAdapter.registerSubtype(MoveResourceEvent.class);

        eventMessageAdapter.registerSubtype(ChooseLineEvent.class);
        eventMessageAdapter.registerSubtype(ChooseWhiteMarbleConversionEvent.class);
        eventMessageAdapter.registerSubtype(DiscardResourcesEvent.class);
        eventMessageAdapter.registerSubtype(MarketBoardEvent.class);

        eventMessageAdapter.registerSubtype(ToggleProductionAtPosition.class);
        eventMessageAdapter.registerSubtype(ChooseResourcesForProductionEvent.class);
        eventMessageAdapter.registerSubtype(FinalProductionPhaseEvent.class);

        eventMessageAdapter.registerSubtype(SetupPhaseEvent.class);

        eventMessageAdapter.registerSubtype(EndMiddlePhaseEvent.class);
        eventMessageAdapter.registerSubtype(InitialOrFinalPhaseEvent.class);
        eventMessageAdapter.registerSubtype(MiddlePhaseEvent.class);

        return eventMessageAdapter;
    }

    private static RuntimeTypeAdapterFactory<SimpleModelElement> gsonElementAdapter(){

        RuntimeTypeAdapterFactory<SimpleModelElement> elemAdapter = RuntimeTypeAdapterFactory.of(SimpleModelElement.class);

        elemAdapter.registerSubtype(SimplePlayerLeaders.class);
        elemAdapter.registerSubtype(SimpleCardShop.class);
        elemAdapter.registerSubtype(SimpleCardCells.class);
        elemAdapter.registerSubtype(SimpleFaithTrack.class);
        elemAdapter.registerSubtype(SimpleMarketBoard.class);
        elemAdapter.registerSubtype(SimpleStrongBox.class);
        elemAdapter.registerSubtype(SimpleDiscardBox.class);
        elemAdapter.registerSubtype(SimpleWarehouseLeadersDepot.class);
        elemAdapter.registerSubtype(EndGameInfo.class);
        elemAdapter.registerSubtype(ActiveLeaderBonusInfo.class);
        elemAdapter.registerSubtype(SimpleProductions.class);
        elemAdapter.registerSubtype(PlayersInfo.class);
        elemAdapter.registerSubtype(SimpleSoloActionToken.class);
        elemAdapter.registerSubtype(SelectablePositions.class);

        return elemAdapter;
    }



}
