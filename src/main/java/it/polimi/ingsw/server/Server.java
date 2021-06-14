package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server for a game of Maestri del Rinascimento.
 */
public class Server
{
    /**
     * The socket port where the server listens to client connections.
     */
    public static int SOCKET_PORT = 7901;


    public static void main(String[] args)
    {
        if (args.length==2){
            SOCKET_PORT=Integer.parseInt(args[1]);
        }
        ServerSocket socket;
        System.out.println("My port is : "+SOCKET_PORT);
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }

}
