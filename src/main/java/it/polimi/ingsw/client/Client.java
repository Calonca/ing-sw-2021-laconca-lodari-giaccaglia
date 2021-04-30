package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.ConnectToServerView;
import it.polimi.ingsw.client.view.CLI.GenericWait;
import it.polimi.ingsw.client.view.CLI.WaitForServerConnection;
import it.polimi.ingsw.client.view.abstractview.View;
import it.polimi.ingsw.server.model.State;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Client for the game.
 */
public class Client implements Runnable
{
    private ServerHandler serverHandler;
    private boolean shallTerminate;
    private View nextView;
    private View currentView;
    private String ip;
    private int port;
    private List<PlayerCache> playersCache=new ArrayList<>();
    private CommonData commonData = new CommonData();
    private CLIBuilder cliBuilder = new CLIBuilder();



    public static void main(String[] args)
    {
        /* Instantiate a new Client. The main thread will become the
         * thread where user interaction is handled. */
        Client client = new Client();
        /* Run the state machine handling the views */
        if (args.length==2)
        {
            client.setServerConnection(args[0],Integer.parseInt(args[1]));
            client.nextView = new WaitForServerConnection();
            client.run();
            client.runViewStateMachine();
        }else {
            client.nextView = new ConnectToServerView();
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
            transitionToView(new ConnectToServerView());
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


    /**
     * Calls the run() method on the current view until the application
     * must be stopped.
     * When no view should be displayed, and the application is not yet
     * terminating, the IdleView is displayed.
     * @apiNote The current view can be changed at any moment by using
     * transitionToView().
     */
    public void runViewStateMachine()
    {
        boolean stop;

        synchronized (this) {
            stop = shallTerminate;
            commonData.removePropertyChangeListener(currentView);
            playersCache.forEach(c->c.removePropertyChangeListener(currentView));
            currentView = nextView;
            nextView = null;
        }
        while (!stop) {
            if (currentView == null) {
                currentView = new GenericWait(() -> {},"Waiting");
            }
            currentView.setOwner(this);
            commonData.addPropertyChangeListener(currentView);
            playersCache.forEach(c->c.addPropertyChangeListener(currentView));
            currentView.setCommonData(commonData);
            currentView.setCliBuilder(cliBuilder);
            currentView.run();

            synchronized (this) {
                stop = shallTerminate;
                currentView = nextView;
                nextView = null;
            }
        }
        /* We are going to stop the application, so ask the server thread
         * to stop as well. Note that we are invoking the stop() method on
         * ServerHandler, not on Thread */
        serverHandler.stop();
    }


    /**
     * Transitions the view thread to a given view.
     * @param newView The view to transition to.
     */
    public synchronized void transitionToView(View newView)
    {
        this.nextView = newView;
        currentView.stopInteraction();
    }


    public CommonData getCommonData() {
        return commonData;
    }

    /**
     * Terminates the application as soon as possible.
     */
    public synchronized void terminate()
    {
        if (!shallTerminate) {
            /* Signal to the view handler loop that it should exit. */
            shallTerminate = true;
            currentView.stopInteraction();
        }
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setState(int player, String state, Map<String, Object> serializedObject){
        if (playersCache.size()==0)
            initializePlayerCache(player,serializedObject);
        playersCache.get(player).update(state,serializedObject);
        Optional<Boolean> a = playersCache.get(0).getFromStateAndKey("Setup","o1");
    }

    public void initializePlayerCache(int player,Map<String, Object> serializedObject){
        int numberOfPlayers = 4; //Todo take from serializedObject
        playersCache = Stream.generate(()->new PlayerCache(player,numberOfPlayers)).limit(numberOfPlayers).collect(Collectors.toList());
    }
}
