package it.polimi.ingsw.client.json;

import it.polimi.ingsw.client.messages.servertoclient.*;
import it.polimi.ingsw.network.jsonUtils.RuntimeTypeAdapterFactory;

public class GsonAdapters {

    public static RuntimeTypeAdapterFactory<ClientMessage> gsonToClientAdapter = gsonToClientAdapter();

    private static RuntimeTypeAdapterFactory<ClientMessage> gsonToClientAdapter() {
        RuntimeTypeAdapterFactory<ClientMessage> gsonToClientAdapter = RuntimeTypeAdapterFactory.of(ClientMessage.class);

        gsonToClientAdapter.registerSubtype(CreatedMatchStatus.class);
        gsonToClientAdapter.registerSubtype(JoinStatus.class);
        gsonToClientAdapter.registerSubtype(MatchesData.class);
        gsonToClientAdapter.registerSubtype(StateInNetwork.class);
        gsonToClientAdapter.registerSubtype(EventNotValid.class);
        gsonToClientAdapter.registerSubtype(ElementsInNetwork.class);

        return gsonToClientAdapter;

    }

}
