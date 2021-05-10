package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.TestViewBuilder;
import it.polimi.ingsw.client.view.GUI.GUIStarter;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {


    public void setStage() {
        Client client = Client.getInstance();
        client.setGUI();
        GUIStarter.main(null);

    }
}