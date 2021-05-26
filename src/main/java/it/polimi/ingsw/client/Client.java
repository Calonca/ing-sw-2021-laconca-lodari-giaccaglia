package it.polimi.ingsw.client;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.GUI.SetupPhase;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.util.*;


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
        ViewBuilder.setCommonData(commonData);
        ViewBuilder.setClient(this);
        CLI cli = new CLI(this);
        if(isCli) {
            ViewBuilder.setCLIView(cli);
            CLIelem.setCli(cli);
        }
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
            removeFromListeners(currentViewBuilder, commonData.getThisPlayerIndex().orElse(0));
            addToListeners(newViewBuilder, commonData.getThisPlayerIndex().orElse(0));

            if (isCLI)
                ViewBuilder.getCLIView().resetCLI();

            currentViewBuilder = newViewBuilder;
            if (isCLI)
                newViewBuilder.run();
            else
                Platform.runLater(newViewBuilder);
        }else System.out.println("ViewBuilder was null");
    }

    public void addToListeners(PropertyChangeListener obj, int playerIndex){
        removeFromListeners(obj, playerIndex);//Added so that if the method is is called more than once it won't register two listeners.
        getCommonData().addPropertyChangeListener(obj);
        if (simpleModel!=null){
            simpleModel.addPropertyChangeListener(obj);
            simpleModel.getPlayerCache(playerIndex).addPropertyChangeListener(obj);
        }
    }

    public void removeFromListeners(PropertyChangeListener obj, int playerIndex){
        getCommonData().removePropertyChangeListener(obj);
        if (simpleModel!=null){
            simpleModel.removePropertyChangeListener(obj);
            simpleModel.getPlayerCache(commonData.getThisPlayerIndex().orElse(0)).removePropertyChangeListener(obj);
        }
    }

    public CommonData getCommonData() {
        return commonData;
    }

    /**
     * Terminates the application as soon as possible.
     */
    public synchronized void terminate()
    {
        if (ViewBuilder.getCLIView().stopASAP.get()) {
            ViewBuilder.getCLIView().resetCLI();
        }
    }

    public SimpleModel getSimpleModel() {
        return simpleModel;
    }

    public void setState(StateInNetwork stateInNetwork){
        if (simpleModel==null){
            try {
                int numOfPlayers = getCommonData().playersOfMatch().map(o->o.length).orElse(0);
                simpleModel = new SimpleModel(numOfPlayers);
            }catch (NoSuchElementException e){
                System.out.println("Received a state before entering a match");
            }
            commonData.setCurrentPlayer(0);
            ViewBuilder.setSimpleModel(simpleModel);
            simpleModel.updateSimpleModel(stateInNetwork);
            changeViewBuilder(SetupPhaseViewBuilder.getBuilder(isCLI));
        }else
            simpleModel.updateSimpleModel(stateInNetwork);
    }

}
