package it.polimi.ingsw.server;


import it.polimi.ingsw.network.messages.servertoclient.PingMessageFromServer;
import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.controller.SessionController;
import it.polimi.ingsw.server.messages.clienttoserver.PingMessageFromClient;
import it.polimi.ingsw.server.messages.clienttoserver.ServerMessage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Objects;
import java.util.Optional;


/**
 * A class that represents the client inside the server.
 */
public class ClientHandler implements Runnable
{
    private transient Socket client;
    private transient ObjectOutputStream output;
    private transient ObjectInputStream input;
    private transient Match match;
    private InetAddress clientAddress;
    private String nickname;
    private transient Thread ping;

    /**
     * Initializes a new handler using a specific socket connected to
     * a client.
     * @param client The socket connection to the client.
     */
    ClientHandler(Socket client)
    {
        this.client = client;
        clientAddress = client.getInetAddress();
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
        SessionController.getInstance().addPlayerToLobby(this);

        try {
            handleClientConnection();

        } catch (IOException e) {
            notifyDisconnection();
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * An event loop that receives messages from the client and processes
     * them in the order they are received.
     * @throws IOException If a communication error occurs.
     */
    private void handleClientConnection() throws IOException
    {

        startPing();

        try {
            while (true) {


                /* read commands from the client, process them, and send replies */
                String next = input.readObject().toString();
                ServerMessage command = ServerMessage.deserialize(next);

                if (Objects.nonNull(command)) {
                    if (command instanceof PingMessageFromClient) {
                        if (false) {
                            String message = ((PingMessageFromClient) command).getPingMessage();
                            System.out.println(message + " from " + client.getInetAddress());
                        }
                    }
                    else
                        command.processMessage(this);
                    }
                }

            } catch(ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from client" + e.toString());

            }catch (SocketTimeoutException | SocketException | EOFException to){ //no message from client
            System.out.println("No more messages from : " + client.getInetAddress());
            notifyDisconnection();
        }
}


    /**
     * it starts a pinging service from the server to the client writing a ping event to the socket every 5000 millis
     */
    private void startPing() {

        ping = new Thread(() -> {

            try {
                int counter = 0;
                while (true) {
                    Thread.sleep(5000);  // send a ping every SOCKET_TIMEOUT/2 seconds
                    ServerToClientMessage pingMessage = (new PingMessageFromServer("Ping #" + counter));
                    sendAnswerMessage(pingMessage);
                    counter++;
                }
            } catch (InterruptedException e) {
                System.out.println("Ping Interrupted");
            } catch (IOException e) {
                System.out.println("Unable to send event to client");
            } finally {
                Thread.currentThread().interrupt();
            }
        });

        ping.start();
    }


    /**
     * The game instance associated with this client.
     * @return The game instance.
     */
    public Optional<Match> getMatch()
    {
        return Optional.ofNullable(match);
    }

    public void setMatch(Match match)
    {
        this.match = match;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
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

    private void stopPing(){
        ping.interrupt();
    }

    private void notifyDisconnection()
    {
        if(ping.isAlive())
            stopPing();


        if(Objects.nonNull(match)) {
            SessionController session = SessionController.getInstance();
            session.setPlayerOffline(this);

            session.sendUpdatedAvailableMatches(); // updates matches info to clients not in game;
            match.notifyPlayerDisconnection(nickname);
            session.saveSessionController();
        }

    }


}
