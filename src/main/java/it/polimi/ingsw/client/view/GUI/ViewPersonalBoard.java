package it.polimi.ingsw.client.view.GUI;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewPersonalBoard extends it.polimi.ingsw.client.view.abstractview.SetupPhaseViewBuilder implements GUIView
{
    public AnchorPane menuPane;

    double controlButtonsY=750;
    double cardShopButtonX=900;
    double marketButtonX= 1000;

    Button faithBut;
    int tempFaith =0;



    List<Button> productions=new ArrayList<>();



    @Override
    public void run() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
