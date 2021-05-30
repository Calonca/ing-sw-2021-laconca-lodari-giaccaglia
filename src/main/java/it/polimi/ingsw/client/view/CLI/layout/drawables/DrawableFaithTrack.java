package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Characters;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.simplemodel.FaithCell;
import it.polimi.ingsw.network.simplemodel.FaithZone;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;

import java.util.List;

public class DrawableFaithTrack {
    private final Canvas canvas =Canvas.withBorder(CLI.width,6);
    private final SimpleFaithTrack simpleFaithTrack;

    public Drawable faithTile(int pos,FaithCell faithCell,boolean player,boolean lorenzo){
        int x = faithCell.getX_pos()*7;
        int y = faithCell.getY_pos()*2;
        Background back;
        FaithZone zone = faithCell.getZone();
        if (faithCell.getPoints()>0)
            back=Background.ANSI_YELLOW_BACKGROUND;
        else if (!faithCell.isPopeSpace())
                back=Background.ANSI_WHITE_BACKGROUND;
        else
            back=Background.ANSI_PURPLE_BACKGROUND;

        Color zoneColor = zone.equals(FaithZone.NORMAL)?Color.BLACK :Color.DISABLED;

        Drawable tile = new Drawable();
        //tile.add(0, Characters.TOP_LEFT_DIV.getString()+Characters.HOR_DIVIDER.getString().repeat(5), zoneColor,back);
        tile.add(0, Characters.VERT_DIVIDER.getString()+(player?"P ":"  ")+(lorenzo?"L ":"  "), zoneColor,back);
        int faithPoints = faithCell.getPoints();
        String faithString = faithPoints>0? StringUtil.stringUntilReachingSize(String.valueOf(faithPoints),2):"  ";
        DrawableLine fp = new DrawableLine(5,0,faithString,zoneColor,back);
        tile.add(0, Characters.BOTTOM_LEFT_DIV.getString()+Characters.HOR_DIVIDER.getString().repeat(6), zoneColor,back);
        tile.add(fp);


        tile.shift(x+1,y);
        return tile;
    }

    public DrawableFaithTrack(SimpleFaithTrack simpleFaithTrack){
        this.simpleFaithTrack = simpleFaithTrack;
    }

    @Override
    public String toString() {
        List<FaithCell> track = simpleFaithTrack.getTrack();
        for (int i = 0, trackSize = track.size(); i < trackSize; i++) {
            FaithCell c = track.get(i);
            canvas.addDrawable(faithTile(i,c,simpleFaithTrack.getPlayerPosition()==i,simpleFaithTrack.getLorenzoPosition()==i));
        }
        return canvas.toString();
    }
}
