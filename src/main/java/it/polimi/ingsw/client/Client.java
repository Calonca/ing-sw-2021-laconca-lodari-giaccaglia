package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.messages.servertoclient.StateInNetwork;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.TestGridBody;
import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.abstractview.ConnectToServerViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ResourceMarketViewBuilder;
import it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Path;
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
            changeViewBuilder(ConnectToServerViewBuilder.getBuilder(isCLI));
            //changeViewBuilder(new TestGridBody());
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
            removeFromListeners(currentViewBuilder);
            addToListeners(newViewBuilder);

            if (isCLI)
                ViewBuilder.getCLIView().clearScreen();

            currentViewBuilder = newViewBuilder;
            if (isCLI)
                newViewBuilder.run();
            else
                Platform.runLater(newViewBuilder);
        }else System.out.println("ViewBuilder was null");
    }

    public void addToListeners(PropertyChangeListener obj){
        removeFromListeners(obj);//Added so that if the method is is called more than once it won't register two listeners.
        getCommonData().addPropertyChangeListener(obj);
        if (simpleModel!=null){
            simpleModel.addPropertyChangeListener(obj);
            simpleModel.getPlayerCache(commonData.getThisPlayerIndex()).addPropertyChangeListener(obj);
        }
    }

    public void removeFromListeners(PropertyChangeListener obj){
        getCommonData().removePropertyChangeListener(obj);
        if (simpleModel!=null){
            simpleModel.removePropertyChangeListener(obj);
            simpleModel.getPlayerCache(commonData.getThisPlayerIndex()).removePropertyChangeListener(obj);
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
            ViewBuilder.getCLIView().clearScreen();
        }
    }

    public SimpleModel getSimpleModel() {
        return simpleModel;
    }

    public void setState(StateInNetwork stateInNetwork){
        System.out.println("Client " +commonData.getThisPlayerIndex()+" received: "+"client "+stateInNetwork.getPlayerNumber()+" "+stateInNetwork.getState());
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Path.class, new JsonUtility.PathConverter()).setPrettyPrinting().create();
        System.out.println(JsonUtility.serialize(stateInNetwork, StateInNetwork.class, gson));
        commonData.setCurrentPlayer(stateInNetwork.getPlayerNumber());
        if (simpleModel==null){
            try {
                int numOfPlayers = getCommonData().playersOfMatch().map(o->o.length).orElse(0);
                simpleModel = new SimpleModel(numOfPlayers);
            }catch (NoSuchElementException e){
                System.out.println("Received a state before entering a match");
            }
            ViewBuilder.setSimpleModel(simpleModel);
        }
        simpleModel.updateSimpleModel(stateInNetwork);
        if(stateInNetwork.getPlayerNumber()==commonData.getThisPlayerIndex()
        && stateInNetwork.getState().equals(State.SETUP_PHASE.name()))
            //SetupPhaseViewBuilder.getBuilder(isCLI);
        //changeViewBuilder(new BoardView3D());
            changeViewBuilder(SetupPhaseViewBuilder.getBuilder(isCLI));
    }

}
