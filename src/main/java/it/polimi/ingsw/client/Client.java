package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.ConnectToServer;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Client for the game.
 */
public class Client implements Runnable
{
    private ServerHandler serverHandler;
    private ViewBuilder nextViewBuilder;
    private ViewBuilder currentViewBuilder;
    private String ip;
    private int port;
    private List<PlayerCache> playersCache=new ArrayList<>();
    private CommonData commonData = new CommonData();
    private CLI cli;


    /**
     * Initializes a client that asks the user for ip and port or if given arguments from the given arguments
     * @param args the first argument is the ip and the second the port.
     */
    public static void main(String[] args)
    {
        Client client = new Client();
        client.cli = new CLI(client);
        if (args.length==2)
        {
            client.setServerConnection(args[0],Integer.parseInt(args[1]));

            client.run();
            client.runViewStateMachine();
        }else {
            client.nextViewBuilder = new ConnectToServer();
            client.runViewStateMachine();
        }
    }

    public void setServerConnection(String ip,int port){
        this.ip = ip;
        this.port = port;
    }


    @Override
    public void run()
    {
        /* Open connection to the server and start a thread for handling
         * communication. */
        Socket server;
        try {
            server = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("server unreachable");
            transitionToView(new ConnectToServer());
            return;
        }
        serverHandler = new ServerHandler(server, this);
        Thread serverHandlerThread = new Thread(serverHandler, "server_" + server.getInetAddress().getHostAddress());
        serverHandlerThread.start();
    }


    /**
     * The handler object responsible for communicating with the server.
     * @return The server handler.
     */
    public ServerHandler getServerHandler()
    {
        return serverHandler;
    }


    public void runViewStateMachine()
    {
        boolean stop;

        synchronized (this) {
            stop = cli.stopASAP.get();
            currentViewBuilder = nextViewBuilder;
            nextViewBuilder = null;
        }
        while (!stop) {
            if (currentViewBuilder == null) {
                currentViewBuilder = new ViewBuilder() {
                    @Override
                    public void run() {
                        cli.setSpinner(new Spinner("Waiting"));
                    }
                };
            }
            currentViewBuilder.setClient(this);
            currentViewBuilder.setCommonData(commonData);
            currentViewBuilder.setCLIView(cli);
            currentViewBuilder.run();

            synchronized (this) {
                stop = cli.stopASAP.get();
                currentViewBuilder = nextViewBuilder;
                nextViewBuilder = null;
            }
        }
        serverHandler.stop();
    }



    /**
     * Transitions the view thread to a given view.
     * @param newViewBuilder The view to transition to.
     */
    public synchronized void transitionToView(ViewBuilder newViewBuilder)
    {
        this.nextViewBuilder = newViewBuilder;
        cli.resetCLI();
    }


    public CommonData getCommonData() {
        return commonData;
    }

    /**
     * Terminates the application as soon as possible.
     */
    public synchronized void terminate()
    {
        if (cli.stopASAP.get()) {
            cli.resetCLI();
        }
    }

    public ViewBuilder getCurrentViewBuilder() {
        return currentViewBuilder;
    }

    /**
     * Returns the player cache for the current player
     */
    public Optional<PlayerCache> currentPlayerCache(){
        return commonData.getCurrentPlayerIndex().map(i->playersCache.get(i));
    }

    public <T> Optional<T> getFromStateAndKey(String state, String key){
        return (Optional<T>) currentPlayerCache().map(cache->cache.<T>getFromStateAndKey(state, key));
    }

    public void setState(int thisPlayerIndex, String state, Map<String, Object> serializedObject){
        int numberOfPlayers = 4;//Todo Replace with true data
        UUID matchId = commonData.getMatchesData()
                .map((o)->o.keySet().stream().toArray(UUID[]::new)[0])
                .orElse(UUID.randomUUID());//Todo Replace with true data
        if (playersCache.size()==0){
            commonData.setStartData(matchId,thisPlayerIndex);
            initializePlayerCache(numberOfPlayers);
        }
        playersCache.get(thisPlayerIndex).update(state,serializedObject);
    }

    public void initializePlayerCache(int numberOfPlayers){
        playersCache = IntStream.range(0,numberOfPlayers)
                .mapToObj((i)->new PlayerCache(i,numberOfPlayers,this)).collect(Collectors.toList());
    }
}
