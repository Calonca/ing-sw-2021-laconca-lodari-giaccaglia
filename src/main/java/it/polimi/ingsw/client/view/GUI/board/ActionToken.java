package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.view.GUI.BoardView3D;
import it.polimi.ingsw.client.view.GUI.Playground;
import it.polimi.ingsw.client.view.GUI.util.NodeAdder;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;

public class ActionToken implements PropertyChangeListener {

    Group tokenGroup = null;

    public void actionTokenBuilder(BoardView3D view3D){
        if (tokenGroup==null)
            tokenGroup = new Group();
            NodeAdder.addNodeToParent(view3D.parent, view3D.boardRec, tokenGroup, new Point3D(70,70,-10));

    }

    /**
     * This method updates the information regarding the Action token
     * @param evt is not null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(SimpleSoloActionToken.class.getSimpleName())){
            SimpleSoloActionToken actionToken =  (SimpleSoloActionToken) evt.getNewValue();
            if (getSimpleModel().getPlayersCaches().length==1)
            {
                ActionTokenAsset tokenAsset = actionToken.getSoloActionToken();

                Rectangle actionTokenRectangle=new Rectangle(200,200);
                System.out.println(tokenAsset.getFrontPath().toString().replace("\\","/"));
                actionTokenRectangle.setFill(new ImagePattern(new Image(tokenAsset.getFrontPath().toString().replace("\\","/"))));
                tokenGroup.getChildren().setAll(actionTokenRectangle);
                if (tokenAsset.isDiscardingCard())
                    Playground.refreshCardShop();
            }
        }
    }
}
