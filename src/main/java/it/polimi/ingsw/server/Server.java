package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.SessionController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Server for a game of Maestri del Rinascimento.
 */
public class Server
{
    /**
     * The socket port where the server listens to client connections.
     */
    public static int SOCKET_PORT = 7831;

    /**
     * Timeout for the socket.
     */
    public static final int SOCKET_TIMEOUT_S = 20;

    /**
     * Main Server loop
     * @param args are the command line or manually inserted at run time arguments
     */
    public static void main(String[] args)
    {
        if (args.length==2){
            SOCKET_PORT=Integer.parseInt(args[1]);
        }
        ServerSocket socket;
        System.out.println("My port is : "+ SOCKET_PORT);
        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        while (true) {
            try {

                //debug mode
                Thread inputThread = new Thread(() -> {
                    Scanner scanner = new Scanner(System.in);
                    SessionController controller = SessionController.getInstance();
                    while (true) {

                        String input = scanner.nextLine();

                        if(input.isEmpty()) {
                            controller.toggleDebugMode();


                            if (controller.getDebugMode())
                                System.out.println("Enabled Debug Mode");
                            else
                                System.out.println("Disabled Debug Mode");
                        }
                    }
                });
                inputThread.start();

                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                Socket client = socket.accept();
                client.setSoTimeout(SOCKET_TIMEOUT_S * 1000);
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();



            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }

}
