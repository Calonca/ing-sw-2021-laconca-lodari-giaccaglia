package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.MiddlePhaseViewBuilder;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class MiddlePhaseGUI extends MiddlePhaseViewBuilder implements GUIView {
    @Override
    public void run() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Todo use MiddlePhaseViewBuilder.sendMessage(Choice.PRODUCTION);
        // MiddlePhaseViewBuilder.sendMessage(Choice.RESOURCE_MARKET);
        // MiddlePhaseViewBuilder.sendMessage(Choice.CARD_SHOP);
        // to send the messages to the client

    }
}
