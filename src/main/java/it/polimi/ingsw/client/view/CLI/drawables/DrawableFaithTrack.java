package it.polimi.ingsw.client.view.CLI.drawables;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Characters;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.simplemodel.FaithCell;
import it.polimi.ingsw.network.simplemodel.FaithZone;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;

import java.util.List;

public class DrawableFaithTrack {
    private final Canvas canvas =Canvas.withBorder(CLI.width,6);
    private final SimpleFaithTrack simpleFaithTrack;

    public Drawable faithTile(int pos,FaithCell faithCell,boolean player,boolean lorenzo){
        int x = pos*5;
        int y =0;
        Background back;
        FaithZone zone = faithCell.getZone();
        if (faithCell.getPoints()>0)
            back=Background.ANSI_YELLOW_BACKGROUND;
        else if (!faithCell.isPopeSpace())
            back=Background.ANSI_WHITE_BACKGROUND;
        else
            back=Background.ANSI_PURPLE_BACKGROUND;

        Color zoneColor = zone.equals(FaithZone.NORMAL)?Color.BLACK :Color.PURPLE;

        Drawable tile = new Drawable();
        tile.add(0, Characters.TOP_LEFT_DIV.getString()+"  "+Characters.TOP_RIGHT_DIV.getString(), zoneColor,back);
        if (faithCell.getPoints()>0)
            tile.add(new DrawableLine(1,0,String.valueOf(faithCell.getPoints()),Color.BLACK,back));
        else tile.add(new DrawableLine(1,0,Characters.HOR_DIVIDER.getString(),zoneColor,back));
        if (player)
            tile.add(new DrawableLine(2,0,"P",Color.BLACK,back));
        else tile.add(new DrawableLine(2,0,Characters.HOR_DIVIDER.getString(),zoneColor,back));

        tile.add(new DrawableLine(0,1,Characters.BOTTOM_LEFT_DIV.getString()+Characters.HOR_DIVIDER.getString()+" "+Characters.BOTTOM_RIGHT_DIV.getString(), zoneColor,back));
        if (lorenzo)
            tile.add(new DrawableLine(2,1,"L",Color.BLACK,back));
        else tile.add(new DrawableLine(2,1,Characters.HOR_DIVIDER.getString(),zoneColor,back));

        tile.shift(x+2,y);
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
