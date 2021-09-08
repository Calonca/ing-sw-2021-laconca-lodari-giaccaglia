package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.layout.GridElem;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Characters;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;
import it.polimi.ingsw.network.simplemodel.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FaithTrackGridElem extends GridElem {
    private final SimpleFaithTrack simpleFaithTrack;

    public Drawable faithTile(int xShift, int yShift, FaithCell faithCell, boolean player, boolean lorenzo){
        int x = faithCell.getXPos()*7;
        int y = faithCell.getYPos()*2;
        Background back;
        FaithZone zone = faithCell.getZone();
        if (faithCell.getPoints()>0 && faithCell.isPopeSpace())
            back=Background.ANSI_YELLOW_BACKGROUND;
        else if (faithCell.isPopeSpace())
                back=Background.ANSI_WHITE_BACKGROUND;
        else {
            PopeFavourTile popeFavourTile = simpleFaithTrack.getTiles().get(faithCell.getZone().getZoneNumber());
            TileState tileState = popeFavourTile.getTileState();
            if(tileState.equals(TileState.INACTIVE))
                back = Background.ANSI_PURPLE_BACKGROUND;
            else if(tileState.equals(TileState.DISCARDED))
                back = Background.ANSI_BRIGHT_RED_BACKGROUND;
            else
                back = Background.ANSI_BRIGHT_GREEN_BACKGROUND;
        }

        Color zoneColor = zone.equals(FaithZone.NORMAL)?Color.BLACK :Color.DISABLED;

        Drawable tile = new Drawable();
        tile.add(0, Characters.VERT_DIVIDER.getString()+(player?"P ":"  ")+(lorenzo?"L ":"  "), zoneColor,back);
        int faithPoints = faithCell.getPoints();
        String faithString = faithPoints>0? StringUtil.untilReachingSize(String.valueOf(faithPoints),2):"  ";
        DrawableLine fp = new DrawableLine(5,0,faithString,zoneColor,back);
        tile.add(0, Characters.BOTTOM_LEFT_DIV.getString()+Characters.HOR_DIVIDER.getString().repeat(6), zoneColor,back);
        tile.add(fp);

        return Drawable.copyShifted(x+xShift+1,y+yShift,tile);
    }

    public FaithTrackGridElem(SimpleFaithTrack player){
        this.simpleFaithTrack = player;
    }

    @Override
    public int getNextElemIndex() {
        return getFirstIdx();
    }

    @Override
    public Optional<Option> getOptionWithIndex(int i) {
        return Optional.empty();
    }

    @Override
    public List<Option> getAllEnabledOption() {
        return new ArrayList<>();
    }

    @Override
    public void addToCanvas(Canvas canvas, int x, int y) {
        List<FaithCell> track = simpleFaithTrack.getTrack();
        for (int i = 0, trackSize = track.size(); i < trackSize; i++) {
            FaithCell c = track.get(i);
            canvas.addDrawable(faithTile(x,y, c, simpleFaithTrack.getPlayerPosition()==i, simpleFaithTrack.getLorenzoPosition()==i));
        }
    }

    @Override
    public int getMinWidth() {
        return 150;
    }

    @Override
    public int getMinHeight() {
        return 9;
    }
}
