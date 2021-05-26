package it.polimi.ingsw.client;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.TestViewBuilder;
import it.polimi.ingsw.client.view.GUI.SetupPhase;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
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
    private final CommonData commonData = new CommonData();
    private SimpleModel simpleModel;
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
            //changeViewBuilder(new TestViewBuilder());
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
        //currentPlayerCache().ifPresent(playerCache -> playerCache.addPropertyChangeListener(obj));
    }

    public void removeFromPublishers(PropertyChangeListener obj){
        getCommonData().removePropertyChangeListener(obj);
        //currentPlayerCache().ifPresent(playerCache -> playerCache.removePropertyChangeListener(obj));
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

    public SimpleModel getSimpleModel() {
        return simpleModel;
    }

    public void setState(StateInNetwork stateInNetwork){
        if (simpleModel==null){
            try {
                int numOfPlayers = getCommonData().playersOfMatch().map(o->o.length).orElseThrow();
                simpleModel = new SimpleModel(numOfPlayers);
                commonData.setCurrentPlayer(0);
                if (isCLI)
                    cli.updateListeners();
            }catch (NoSuchElementException e){
                System.out.println("Received a state before entering a match");
            }
        }
        simpleModel.updateSimpleModel(stateInNetwork);
        if (!isCLI)
            changeViewBuilder(new SetupPhase());
    }

}
