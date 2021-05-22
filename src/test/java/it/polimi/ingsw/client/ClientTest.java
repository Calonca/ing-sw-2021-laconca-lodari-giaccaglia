package it.polimi.ingsw.client;

public class ClientTest {


    public void setStage() {
        Client client = Client.getInstance();
        client.setCLIOrGUI(false);

    }
}