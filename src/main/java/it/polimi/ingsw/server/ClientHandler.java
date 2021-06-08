package it.polimi.ingsw.server;


import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.SessionController;
import it.polimi.ingsw.server.messages.clienttoserver.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;


/**
 * A class that represents the client inside the server.
 */
public class ClientHandler implements Runnable
{
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Optional<Match> match=Optional.empty();


    /**
     * Initializes a new handler using a specific socket connected to
     * a client.
     * @param client The socket connection to the client.
     */
    ClientHandler(Socket client)
    {
        this.client = client;
    }

    /**
     * Connects to the client and runs the event loop.
     */
    @Override
    public void run()
    {
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("could not open connection to " + client.getInetAddress());
            return;
        }

        System.out.println("Connected to " + client.getInetAddress());
        try {
            SessionController.getInstance().addPlayerToLobby(this);
            sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(this)));
        } catch (IOException e) {
            System.out.println("could not send matches data to " + client.getInetAddress());
            return;
        }

        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }

        try {
            client.close();
        } catch (IOException e) { }
    }


    /**
     * An event loop that receives messages from the client and processes
     * them in the order they are received.
     * @throws IOException If a communication error occurs.
     */
    private void handleClientConnection() throws IOException
    {
        try {
            while (true) {
                /* read commands from the client, process them, and send replies */
                String next = input.readObject().toString();
                ServerMessage command = ServerMessage.deserialize(next);
                command.processMessage(this);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from client" + e.toString());
        }
    }


    /**
     * The game instance associated with this client.
     * @return The game instance.
     */
    public Optional<Match> getMatch()
    {
        return match;
    }

    public void setMatch(Match match)
    {
        this.match = Optional.of(match);
    }

    /**
     * Sends a message to the client.
     * @param answerMsg The message to be sent.
     * @throws IOException If a communication error occurs.
     */
    public void sendAnswerMessage(ServerToClientMessage answerMsg) throws IOException
    {
        output.writeObject(answerMsg.serialized());
    }
}
