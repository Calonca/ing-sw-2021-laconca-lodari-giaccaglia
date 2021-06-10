package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Characters;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.simplemodel.FaithCell;
import it.polimi.ingsw.network.simplemodel.FaithZone;
import it.polimi.ingsw.network.simplemodel.SimpleFaithTrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FaithTrackGridElem extends GridElem {
    private final SimpleFaithTrack simpleFaithTrack;
    private final String allPlayers;

    public Drawable faithTile(int xShift, int yShift, FaithCell faithCell, boolean player, boolean lorenzo){
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
        String faithString = faithPoints>0? StringUtil.untilReachingSize(String.valueOf(faithPoints),2):"  ";
        DrawableLine fp = new DrawableLine(5,0,faithString,zoneColor,back);
        tile.add(0, Characters.BOTTOM_LEFT_DIV.getString()+Characters.HOR_DIVIDER.getString().repeat(6), zoneColor,back);
        tile.add(fp);

        return Drawable.copyShifted(x+xShift+1,y+yShift,tile);
    }

    public FaithTrackGridElem(SimpleFaithTrack player, SimpleFaithTrack[] simpleFaithTrack){
        this.simpleFaithTrack = player;
        allPlayers = Arrays.stream(simpleFaithTrack).map(t->String.valueOf(t.getPlayerPosition())).reduce(",",(a,b)->a+b);
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
