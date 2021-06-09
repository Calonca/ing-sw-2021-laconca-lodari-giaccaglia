package it.polimi.ingsw.client.view.GUI;

import it.polimi.ingsw.client.view.GUI.GUIelem.ButtonSelectionModel;
import it.polimi.ingsw.server.model.Resource;
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

    Button faithBut;
    int tempFaith =0;



    List<Button> productions=new ArrayList<>();




    private static ButtonSelectionModel boardController=new ButtonSelectionModel();



    public static ButtonSelectionModel getController()
    {
        return boardController;
    }
    @Override
    public void run() {
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().remove(0);

        getClient().getStage().setResizable(true);
        SubScene root=getRoot();
        root.setId("BOARD");
        ((Pane)getClient().getStage().getScene().getRoot()).getChildren().add(root);

        getClient().getStage().setHeight(800);
        getClient().getStage().setWidth(1200);
        getClient().getStage().setResizable(false);

        CardShopGUI cardshop=new CardShopGUI(true);
        cardshop.addMarket();
        ResourceMarketGUI market=new ResourceMarketGUI();
        market.addMatrix();







    }

    public SubScene getRoot() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ViewPersonalBoard.fxml"));
        Parent root = null;
        try {
            root = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SubScene(root,1200,800);


    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void moveFaithX(boolean foward){
        if(foward)
            faithBut.setLayoutX(faithBut.getLayoutX()+41.5);
        else
            faithBut.setLayoutX(faithBut.getLayoutX()-40);
    }

    public void moveFaithY(boolean downward){
        if(downward)
            faithBut.setLayoutY(faithBut.getLayoutY()+35);
        else
            faithBut.setLayoutY(faithBut.getLayoutY()-35);


    }

    public void moveFaith() {
        if (tempFaith <2)
        {
            tempFaith++;
            moveFaithX(true);
        } else
        if (tempFaith <4)
        {
            tempFaith++;
            moveFaithY(false);
        }
        else if (tempFaith < 9)
            {
                tempFaith++;
                moveFaithX(true);
            }
        else if (tempFaith < 11)
        {
            tempFaith++;
            moveFaithY(true);
        }
        else if (tempFaith < 16)
        {
            tempFaith++;
            moveFaithX(true);
        }
        else if (tempFaith <18)
        {
            tempFaith++;
            moveFaithY(false);
        } else
            moveFaithX(true);
    }

    public ButtonSelectionModel getBoardController() {
        return boardController;
    }



    public void addDevelopmentCards()
    {
        Button productionButton;

        for(int i=0;i<3;i++)
        {
            productionButton=new Button();
            productionButton.setLayoutX(740+150*i);
            productionButton.setLayoutY(550);
            ImageView devCardImage=new ImageView(new Image("assets/devCards/raw/FRONT/Masters of Renaissance_Cards_FRONT_BLUE_2.png", true));
            devCardImage.setFitWidth(100);
            devCardImage.setFitHeight(100);
            productionButton.setGraphic(devCardImage);
            menuPane.getChildren().add(productionButton);
            productions.add(productionButton);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

      //  ImageView tempImageView;
      //  tempImageView=new ImageView(new Image("assets/board/smallerboard.png", true));
      //  tempImageView.setFitHeight(500);
      //  tempImageView.setFitWidth(750);
      //  tempImageView.setLayoutX(445);
      //  tempImageView.setLayoutY(280);

      //  menuPane.getChildren().add(tempImageView);
      //  StackPane.setAlignment(tempImageView, Pos.BOTTOM_CENTER);

      //  initializeDepositAndWarehouse();

      //  initializeFaithTrack();

      //  validationButton();




        List<Image> res=new ArrayList<>();
        res.add(new Image("assets/resources/GOLD.png"));
        res.add(new Image("assets/resources/SERVANT.png"));
        res.add(new Image("assets/resources/SHIELD.png"));
        res.add(new Image("assets/resources/STONE.png"));









        //bindForStandardProduction();
        getClient().getStage().show();
    }
}
