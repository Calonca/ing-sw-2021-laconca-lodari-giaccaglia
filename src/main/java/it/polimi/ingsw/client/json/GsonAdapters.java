package it.polimi.ingsw.client.json;

import it.polimi.ingsw.client.messages.servertoclient.*;
import it.polimi.ingsw.network.jsonutils.RuntimeTypeAdapterFactory;

public class GsonAdapters {

    private GsonAdapters(){}

    public static final RuntimeTypeAdapterFactory<ClientMessage> gsonToClientAdapter = gsonToClientAdapter();

    private static RuntimeTypeAdapterFactory<ClientMessage> gsonToClientAdapter() {
        RuntimeTypeAdapterFactory<ClientMessage> gsonToClientAdapter = RuntimeTypeAdapterFactory.of(ClientMessage.class);

        gsonToClientAdapter.registerSubtype(CreatedMatchStatus.class);
        gsonToClientAdapter.registerSubtype(JoinStatus.class);
        gsonToClientAdapter.registerSubtype(MatchesData.class);
        gsonToClientAdapter.registerSubtype(StateInNetwork.class);
        gsonToClientAdapter.registerSubtype(EventNotValid.class);
        gsonToClientAdapter.registerSubtype(ElementsInNetwork.class);
        gsonToClientAdapter.registerSubtype(PingMessageFromServer.class);

        return gsonToClientAdapter;

    }

}
