package it.polimi.ingsw.client.view.GUI.board;

import it.polimi.ingsw.client.simplemodel.PlayerCache;
import it.polimi.ingsw.client.view.GUI.BoardView3D;
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
import javafx.scene.shape.Shape3D;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;

public class InfoTiles implements PropertyChangeListener {


    List<Rectangle> popeTiles=new ArrayList<>();
    Rectangle firstTile;
    Rectangle secondTile;
    Rectangle thirdTile;

    List<ImagePattern> tileImages;
    int playerNumber;
    Group infoGroup=new Group();
    private PlayerCache cache;

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




    public AnchorPane playerStatsTile(SimplePlayerInfo playerInfo, String state)
    {
        AnchorPane anchor=new AnchorPane();

        Text text;
        text=new Text("NICK:  " + playerInfo.getNickname());
        text.setLayoutX(50);
        text.setLayoutY(50);
        anchor.getChildren().add(text);

        anchor.setMinSize(200,200);

        if(playerInfo.isOnline())
            text=new Text("STATUS:  "+state);
        else
            text=new Text("STATUS: OFFLINE");
        text.setLayoutX(50);
        text.setLayoutY(20);

        anchor.getChildren().add(text);

        text=new Text("FAITH:  " + playerInfo.getCurrentPosition());
        text.setLayoutX(50);
        text.setLayoutY(110);

        anchor.getChildren().add(text);

        text=new Text("PLAYER INDEX:  " + playerInfo.getPlayerIndex());
        text.setLayoutX(50);
        text.setLayoutY(80);

        anchor.getChildren().add(text);

        text=new Text("VICTORY POINTS:  " + playerInfo.getCurrentVictoryPoints());
        text.setLayoutX(50);
        text.setLayoutY(150);



        anchor.getChildren().add(text);
        anchor.setId("background");

        return anchor;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PlayersInfo.class.getSimpleName()))
        {
            PlayersInfo playersInfo =  (PlayersInfo) evt.getNewValue();


            Runnable done = () -> {
                infoGroup.getChildren().clear();

                if (getSimpleModel().getPlayersCaches().length==1)
                {
                    SimpleSoloActionToken actionToken = getSimpleModel().getPlayerCache(0).getElem(SimpleSoloActionToken.class).orElseThrow();
                    ActionTokenAsset tokenAsset = actionToken.getSoloActionToken();

                    Rectangle actionTokenRectangle=new Rectangle(100,100);
                    System.out.println(tokenAsset.getFrontPath().toString().replace("\\","/"));
                    actionTokenRectangle.setFill(new ImagePattern(new Image(tokenAsset.getFrontPath().toString().replace("\\","/"))));
                    NodeAdder.addNodeToParent(infoGroup,infoGroup,actionTokenRectangle,infoGroup.localToParent(-100,-100,1000));
                }

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

                                System.out.println(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getValue());

                        }

                        // if(vaticanReportInfo.getPlayersTriggeringVaticanReport().contains(playerNumber))
                    /*    if(!vaticanReportInfo.hasReportBeenShown())
                        {
                            if(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getValue().equals(TileState.DISCARDED))
                                popeTiles.get(vaticanReportInfo.getPopeTileStateMap().get(playerNumber).getKey()).setOpacity(0);
                            vaticanReportInfo.reportWillBeShown();
                        }*/

                    }


                for(int i=0;i<playersInfo.getSimplePlayerInfoMap().size();i++)
                {
                        NodeAdder.addNodeToParent(infoGroup, infoGroup,playerStatsTile(playersInfo.getSimplePlayerInfoMap().get(i),getSimpleModel().getPlayerCache(i).getCurrentState()),new Point3D(-400,210*i,1100));

                }

            };


            Platform.runLater(done);
        }

    }
}
