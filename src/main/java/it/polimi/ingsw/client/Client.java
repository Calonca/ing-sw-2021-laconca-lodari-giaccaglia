package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.GUI.SetupPhase;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
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
    private ViewBuilder currentViewBuilder;
    private String ip;
    private int port;
    private List<PlayerCache> playersCache=new ArrayList<>();
    private final CommonData commonData = new CommonData();
    private CLI cli;
    private Stage stage;
    private boolean isCLI;

    private static Client single_instance = null;

    public static Client getInstance()
    {
        if (single_instance == null)
            single_instance = new Client();

        return single_instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setCLIOrGUI(boolean isCli){
        this.isCLI = isCli;
        if(isCli)
            cli = new CLI(this);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public ViewBuilder getCurrentViewBuilder() {
        return currentViewBuilder;
    }

    public boolean isCLI() {
        return isCLI;
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
        if (ip==null||commonData.getCurrentnick()==null){
            changeViewBuilder(ConnectToServerViewBuilder.getBuilder(isCLI));
            return;}
        Socket server;
        try {
            server = new Socket(ip, port);
        } catch (IOException e) {
            System.out.println("server unreachable");
            changeViewBuilder(ConnectToServerViewBuilder.getBuilder(isCLI));
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
     * Changes the ViewBuilder.
     * Should only be called from another ViewBuilder.
     * @param newViewBuilder The ViewBuilder to transition to.
     *
     */
    public synchronized void changeViewBuilder(ViewBuilder newViewBuilder)
    {

        if (newViewBuilder!=null) {
            removeFromPublishers(currentViewBuilder);
            addToPublishers(newViewBuilder);

            if (isCLI)
                cli.resetCLI();

            currentViewBuilder = newViewBuilder;
            currentViewBuilder.setClient(this);
            currentViewBuilder.setCommonData(commonData);
            if (isCLI)
                currentViewBuilder.setCLIView(cli);
            if (isCLI)
                newViewBuilder.run();
            else
                Platform.runLater(newViewBuilder);
        }else System.out.println("ViewBuilder was null");
    }

    public void addToPublishers(PropertyChangeListener obj){
        removeFromPublishers(obj);//Added so that if the method is is called more than once it won't register two listeners.
        getCommonData().addPropertyChangeListener(obj);
        currentPlayerCache().ifPresent(playerCache -> playerCache.addPropertyChangeListener(obj));
    }

    public void removeFromPublishers(PropertyChangeListener obj){
        getCommonData().removePropertyChangeListener(obj);
        currentPlayerCache().ifPresent(playerCache -> playerCache.removePropertyChangeListener(obj));
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

    public <T extends StateInNetwork> Optional<T> getState(String state){
        Optional<StateInNetwork> o = currentPlayerCache().flatMap(cache->cache.getDataFromState(state));
        try {
            return (Optional<T>) o;
        } catch (Exception ignored){
            return Optional.empty();
        }

    }

    public void setState(int thisPlayerIndex, String state, StateInNetwork serializedObject){
        if (playersCache.size()==0){
            //TODO CHECK HERE COMMENTED OUT CODE
          /*  SETUP_PHASE setup_phase = (SETUP_PHASE) serializedObject;
            commonData.setStartData(setup_phase.getMatchID(),thisPlayerIndex);
            commonData.setCurrentPlayer(0);
            initializePlayerCache(setup_phase.getNickNames().length); */
            if (isCLI)
                cli.updateListeners();
        }
        playersCache.get(thisPlayerIndex).update(state,serializedObject);
        if (!isCLI)
            changeViewBuilder(new SetupPhase());
    }

    public void initializePlayerCache(int numberOfPlayers){
        playersCache = IntStream.range(0,numberOfPlayers)
                .mapToObj((i)->new PlayerCache(i,numberOfPlayers,this)).collect(Collectors.toList());
    }
}
