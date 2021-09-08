package it.polimi.ingsw.client.view.gui.board;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.gui.BoardView3D;
import it.polimi.ingsw.client.view.gui.util.NodeAdder;
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

/**
 * Contains faith track tiles and player info tiles
 */
public class InfoTiles implements PropertyChangeListener {


    List<Rectangle> popeTiles=new ArrayList<>();
    Rectangle firstTile;
    Rectangle secondTile;
    Rectangle thirdTile;

    List<ImagePattern> tileImages;
    int playerNumber;
    Group infoGroup=new Group();
    private PlayerCache cache;

    /**
     * This method adds the infoGroup to the Board 3D for future updates, and preloads the faith track images
     * @param view3D is the Board 3D
     * @param parent is the Board 3D main group
     * @param board is the Board 3D image rectangle
     * @param playerNumber is the player index
     * @param cache is the player cache
     */
    public void infoBuilder(BoardView3D view3D, Group parent, Rectangle board, int playerNumber, PlayerCache cache){

        this.playerNumber =playerNumber;
        this.cache = cache;

        Group popeGroup=new Group();
        firstTile=new Rectangle(200,200);
        firstTile.setLayoutX(0);
        firstTile.setLayoutY(0);
        NodeAdder.addNodeToParent(popeGroup,firstTile,board.localToParent(new Point3D(560,235,-50)));
        ImagePattern tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_1_INACTIVE.png"));
        firstTile.setFill(tempImage);

        secondTile=new Rectangle(200,200);
        secondTile.setLayoutX(0);
        secondTile.setLayoutY(0);
        NodeAdder.addNodeToParent(popeGroup,secondTile,board.localToParent(new Point3D(1150,110,-50)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_2_INACTIVE.png"));
        secondTile.setFill(tempImage);


        thirdTile=new Rectangle(200,200);
        thirdTile.setLayoutX(0);
        thirdTile.setLayoutY(0);
        NodeAdder.addNodeToParent(popeGroup,thirdTile,board.localToParent(new Point3D(1860,235,-50)));

        tempImage = new ImagePattern(new Image("assets/track/FAVOUR_TILE_3_INACTIVE.png"));
        thirdTile.setFill(tempImage);


        popeTiles.add(firstTile);
        popeTiles.add(secondTile);
        popeTiles.add(thirdTile);

        parent.getChildren().add(popeGroup);
        parent.getChildren().add(infoGroup);

        tileImages=new ArrayList<>();
        tileImages.add(new ImagePattern(new Image("assets/track/FAVOUR_TILE_1_ACTIVE.png")));
        tileImages.add(new ImagePattern(new Image("assets/track/FAVOUR_TILE_2_ACTIVE.png")));
        tileImages.add(new ImagePattern(new Image("assets/track/FAVOUR_TILE_3_ACTIVE.png")));
    }


    /**
     * This is a converter to centralize the information display
     * @param playerInfo the corresponding PlayerInfo
     * @param state the corresponding State
     * @return the corresponding anchorpane
     */
    public AnchorPane playerStatsTile(SimplePlayerInfo playerInfo, String state)
    {
        AnchorPane anchor=new AnchorPane();

        Text text;
        text=new Text("NICK:  " + playerInfo.getNickname());
        text.setLayoutX(50);
        text.setLayoutY(50);
        anchor.getChildren().add(text);
        text.setStyle("-fx-font-size: 20;");

        anchor.setMinSize(400,200);

        if(playerInfo.isOnline())
            text=new Text("STATE:  "+state);
        else
            text=new Text("STATE: OFFLINE");
        text.setLayoutX(50);
        text.setLayoutY(20);
        text.setStyle("-fx-font-size: 20;");
        anchor.getChildren().add(text);

        text=new Text("FAITH:  " + playerInfo.getCurrentPosition());
        text.setLayoutX(50);
        text.setLayoutY(110);
        text.setStyle("-fx-font-size: 20;");

        anchor.getChildren().add(text);

        text=new Text("PLAYER INDEX:  " + playerInfo.getPlayerIndex());
        text.setLayoutX(50);
        text.setLayoutY(80);
        text.setStyle("-fx-font-size: 20;");

        anchor.getChildren().add(text);

        text=new Text("VICTORY POINTS:  " + playerInfo.getCurrentVictoryPoints());
        text.setLayoutX(50);
        text.setLayoutY(150);
        text.setStyle("-fx-font-size: 20;");



        anchor.getChildren().add(text);
        anchor.setId("background");

        return anchor;
    }


    /**
     * This method updates the information regarding the Vatican event and Faith track in real time
     * @param evt is not null
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayersInfo.class.getSimpleName()))
        {
            PlayersInfo playersInfo =  (PlayersInfo) evt.getNewValue();

            Runnable done = () -> {
                infoGroup.getChildren().clear();

                if(getSimpleModel().getElem(VaticanReportInfo.class).isPresent())
                {

                    VaticanReportInfo vaticanReportInfo;
                    vaticanReportInfo=getSimpleModel().getElem(VaticanReportInfo.class).get();
                    if(vaticanReportInfo.hasReportOccurred())
                    {
                        int tileNumber=vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()+1;

                        if(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getValue().equals(TileState.DISCARDED))
                            popeTiles.get(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()).setOpacity(0);

                        if(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getValue().equals(TileState.ACTIVE))
                            popeTiles.get(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()).setFill(new ImagePattern(new Image("assets/track/FAVOUR_TILE_"+tileNumber+"_ACTIVE.png")));


                    }

                    // if(vaticanReportInfo.getPlayersTriggeringVaticanReport().contains(numberOfPlayerSendingEvent))
                /*    if(!vaticanReportInfo.hasReportBeenShown())
                    {
                        if(vaticanReportInfo.getPopeTileStateMap().get(numberOfPlayerSendingEvent).getValue().equals(TileState.DISCARDED))
                            popeTiles.get(vaticanReportInfo.getPopeTileStateMap().get(numberOfPlayerSendingEvent).getKey()).setOpacity(0);
                        vaticanReportInfo.reportWillBeShown();
                    }*/

                }

                for(int i=0;i<playersInfo.getSimplePlayerInfoMap().size();i++)
                {
                        NodeAdder.addNodeToParent(infoGroup, infoGroup,playerStatsTile(playersInfo.getSimplePlayerInfoMap().get(i),getSimpleModel().getPlayerCache(i).getCurrentState()),
                                new Point3D(2100,-900+(210*i),1400));

                }

            };

            Platform.runLater(done);
        }
    }
}
