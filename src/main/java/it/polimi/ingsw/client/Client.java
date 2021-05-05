package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.ConnectToServer;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;
import it.polimi.ingsw.network.messages.servertoclient.state.StateMessage;

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
            //client.runViewStateMachine();
        }else {
            client.nextViewBuilder = new ConnectToServer();
            //client.runViewStateMachine();
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
            changeViewBuilder(new ConnectToServer(), null);
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
     * Changes the ViewBuilder.
     * Should only be called from another ViewBuilder.
     * @param newViewBuilder The ViewBuilder to transition to.
     * @param fromView the ViewBuilder from where you are making the transition.
     */
    public synchronized void changeViewBuilder(ViewBuilder newViewBuilder, ViewBuilder fromView)
    {
        cli.resetCLI();
        currentViewBuilder = newViewBuilder;
        currentViewBuilder.setClient(this);
        currentViewBuilder.setCommonData(commonData);
        currentViewBuilder.setCLIView(cli);
        newViewBuilder.run();
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

    /**
     * Returns the player cache for the current player
     */
    public Optional<PlayerCache> currentPlayerCache(){
        if (playersCache.size()==0)
            return Optional.empty();
        else return Optional.of(playersCache.get(commonData.getCurrentPlayerIndex()));
    }

    public <T extends StateMessage> Optional<T> getState(String state){
        Optional<StateMessage> o = currentPlayerCache().flatMap(cache->cache.getDataFromState(state));
        try {
            return (Optional<T>) o;
        } catch (Exception ignored){
            return Optional.empty();
        }

    }

    public void setState(int thisPlayerIndex, String state, StateMessage serializedObject){
        if (playersCache.size()==0){
            SETUP_PHASE setup_phase = (SETUP_PHASE) serializedObject;
            commonData.setStartData(setup_phase.getMatchID(),thisPlayerIndex);
            commonData.setCurrentPlayer(0);
            initializePlayerCache(setup_phase.getNickNames().length);
        }
        playersCache.get(thisPlayerIndex).update(state,serializedObject);
    }

    public void initializePlayerCache(int numberOfPlayers){
        playersCache = IntStream.range(0,numberOfPlayers)
                .mapToObj((i)->new PlayerCache(i,numberOfPlayers,this)).collect(Collectors.toList());
    }
}
