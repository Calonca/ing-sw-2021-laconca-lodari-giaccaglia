package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class WinLooseGUI extends WinLooseBuilder implements GUIView {
    @Override
    public void run() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

/*ObservableList<Node> workingCollection = FXCollections.observableArrayList(((Pane)getClient().getStage().getScene().getRoot()).getChildren());
            Collections.swap(workingCollection, 0, 1);
            ((Pane)getClient().getStage().getScene().getRoot()).getChildren().setAll(workingCollection);

 */
