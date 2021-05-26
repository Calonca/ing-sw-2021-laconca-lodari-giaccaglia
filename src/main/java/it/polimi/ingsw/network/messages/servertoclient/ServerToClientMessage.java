package it.polimi.ingsw.network.messages.servertoclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.UUIDTypeAdapter;
import it.polimi.ingsw.network.messages.NetworkMessage;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.messages.servertoclient.state.StateMessage;
import it.polimi.ingsw.network.simplemodel.SimpleCardShop;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.util.UUID;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.serialize;

/**
 * A network message from the server to the client visible to both.

 * Can be send even without being an answer to a command.<br>
 * Each subclass message has it's divided into two.
 * One class in the network package that contains common methods, constructors and attributes between client and server.<br>
 * A subclass of the class in the network package defined in the client that implements an interface with methods only working in the client.
 * All the subclasses defined in the network package are registered in serialized().
 * The class in the network and the class in the client packages have the same name to ease the serialization.
 */
public abstract class ServerToClientMessage extends NetworkMessage
{
    public UUID parentIdentifier;

    /**
     * Initializes a message answering a command.
     * @param command The command being answered.
     */
    public ServerToClientMessage(ClientToServerMessage command)
    {
        this.parentIdentifier = command.getIdentifier();
    }

    /**
     * Initializes a ServerToClientMessage that doesn't answer to any commands.
     */
    public ServerToClientMessage() {}


    /**
     * Returns the identifier of the message being answered if it has one, otherwise returns null.
     * @return The UUID of the answered message.
     */
    public UUID getParentIdentifier()
    {
        return parentIdentifier;
    }

    /**
     * Method used for serialization.
     * @return a json string representing the message and it's type.
     */
    public String serialized()
    {

        RuntimeTypeAdapterFactory<ServerToClientMessage> s2cAdapter = RuntimeTypeAdapterFactory.of(ServerToClientMessage.class);

        //Register here all the message types
        s2cAdapter.registerSubtype(CreatedMatchStatus.class);
        s2cAdapter.registerSubtype(JoinStatus.class);
        s2cAdapter.registerSubtype(MatchesData.class);
        s2cAdapter.registerSubtype(StateMessage.class);
        s2cAdapter.registerSubtype(EventNotValid.class);
        s2cAdapter.registerSubtype(StateInNetwork.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(s2cAdapter)
                .create();

        return serialize(this, ServerToClientMessage.class, gson1);

    }


}
