package it.polimi.ingsw.network.messages.servertoclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.NetworkMessage;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.*;

import java.nio.file.Path;
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
        s2cAdapter.registerSubtype(EventNotValid.class);
        s2cAdapter.registerSubtype(StateInNetwork.class);

        Gson gson1 = new GsonBuilder()
                .registerTypeAdapterFactory(s2cAdapter)
                .registerTypeAdapterFactory(elementAdapter()).registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter())
                .create();

        return serialize(this, ServerToClientMessage.class, gson1);

    }


    public static RuntimeTypeAdapterFactory<SimpleModelElement> elementAdapter(){
        RuntimeTypeAdapterFactory<SimpleModelElement> elemAdapter = RuntimeTypeAdapterFactory.of(SimpleModelElement.class);

        elemAdapter.registerSubtype(SimplePlayerLeaders.class);
        elemAdapter.registerSubtype(SimpleCardShop.class);
        elemAdapter.registerSubtype(SimpleCardCells.class);
        elemAdapter.registerSubtype(SimpleFaithTrack.class);
        elemAdapter.registerSubtype(SimpleMarketBoard.class);
        elemAdapter.registerSubtype(SimpleStrongBox.class);
        elemAdapter.registerSubtype(SimpleDiscardBox.class);
        elemAdapter.registerSubtype(SimpleWarehouseLeadersDepot.class);

        return elemAdapter;
    }



}
