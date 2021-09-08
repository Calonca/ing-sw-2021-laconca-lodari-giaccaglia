package it.polimi.ingsw.client.view.gui.board;

import it.polimi.ingsw.client.view.gui.BoardView3D;
import it.polimi.ingsw.client.view.gui.Playground;
import it.polimi.ingsw.client.view.gui.util.NodeAdder;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.simplemodel.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;

/**
 * Represents the action token in the single player game
 */
public class ActionToken implements PropertyChangeListener {

    Group tokenGroup = null;

    public void actionTokenBuilder(BoardView3D view3D){
        if (tokenGroup==null) {
            tokenGroup = new Group();
            NodeAdder.addNodeToParent(view3D.parent, view3D.boardRec, tokenGroup, new Point3D(70, 70, -10));
            SimpleSoloActionToken actionToken = getSimpleModel().getPlayerCache(0).getElem(SimpleSoloActionToken.class).orElseThrow();
            setActionToken(actionToken);

        }

    }

    /**
     * This method updates the information regarding the Action token
     * @param evt is not null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(SimpleSoloActionToken.class.getSimpleName())){
            SimpleSoloActionToken actionToken =  (SimpleSoloActionToken) evt.getNewValue();
            setActionToken(actionToken);
            Playground.refreshCardShop();

        }
    }


    public void setActionToken(SimpleSoloActionToken actionToken){
        if (getSimpleModel().getPlayersCaches().length==1)
        {
            ActionTokenAsset tokenAsset = actionToken.getSoloActionToken();

            Rectangle actionTokenRectangle=new Rectangle(200,200);
            actionTokenRectangle.setFill(new ImagePattern(new Image(tokenAsset.getFrontPath().toString().replace("\\","/"))));
            tokenGroup.getChildren().setAll(actionTokenRectangle);
            Playground.refreshCardShop();


        }
    }


}
