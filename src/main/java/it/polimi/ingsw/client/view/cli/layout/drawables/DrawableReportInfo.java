package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import it.polimi.ingsw.network.simplemodel.TileState;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;
import javafx.util.Pair;

public class DrawableReportInfo {

    private DrawableReportInfo(){}

    public static Drawable fromVaticanReportStatus(VaticanReportInfo vaticanReportInfo, int playerIndex, SimplePlayerInfo playerInfo) {


        boolean hasTriggeredVaticanReport = vaticanReportInfo.getPlayersTriggeringVaticanReport().contains(playerIndex);
        boolean lorenzoTriggeredVaticanReport = vaticanReportInfo.getPlayersTriggeringVaticanReport().size() == 1 &&  vaticanReportInfo.getPlayersTriggeringVaticanReport().contains(-1) && !hasTriggeredVaticanReport;

        boolean isPlayerViewingAndReporting = vaticanReportInfo.getPlayersTriggeringVaticanReport().contains(playerIndex);
        String nickname = playerInfo.getNickname();
        int nicknameLength = nickname.length();

        Pair<Integer, TileState> tile = vaticanReportInfo.getPopeTileStateMap().get(playerIndex);
        int tileNumber = tile.getKey() + 1;
        TileState state = tile.getValue();

        String delimiter = "═".repeat(28-3-nicknameLength-1);
        Drawable drawable = new Drawable();
        drawable.add(0,"╔══");
        drawable.add(new DrawableLine(3,0, nickname, Color.BRIGHT_WHITE, Background.DEFAULT));
        drawable.add(new DrawableLine(3+nicknameLength,0, delimiter));
        drawable.add(new DrawableLine(27, 0, "╗"));
        drawable.add(0, "║                          ║");


        if (hasTriggeredVaticanReport || lorenzoTriggeredVaticanReport) {

            if(lorenzoTriggeredVaticanReport)
                drawable.add(new DrawableLine(9, 1, " Lorenzo", Color.BRIGHT_RED, Background.DEFAULT));
            else if(isPlayerViewingAndReporting && CommonData.getThisPlayerIndex() == playerIndex)
                drawable.add(new DrawableLine(9, 1, "   You", Color.BRIGHT_RED, Background.DEFAULT));

            drawable.add(0, "║═══════           ════════║");
            drawable.add(new DrawableLine(9, 2, "Triggered", Color.BRIGHT_YELLOW, Background.DEFAULT));
            drawable.add(0, "║════                  ════║");
            drawable.add(new DrawableLine(7,3, "Vatican Report  ", Color.BRIGHT_YELLOW, Background.DEFAULT));
            drawable.add(0, "║                          ║");
            drawable.add(0, "║═══                  ═════║");

        }


        else {

            drawable.add(0, "║                          ║");
            drawable.add(0, "║════                  ════║");
            drawable.add(0, "║                          ║");
            drawable.add(0, "║═══                  ═════║");
            drawable.add(new DrawableLine(7,3, "Vatican Report  ", Color.BRIGHT_YELLOW, Background.DEFAULT));

        }
        drawable.add(new DrawableLine(4, 5, " Pope Tile Status", Color.YELLOW, Background.DEFAULT));


        drawable.add(0, "║    Tile Number : " + StringUtil.untilReachingSize(tileNumber, 2) + "      ║");
        drawable.add(0, "║  Tile State : " + StringUtil.untilReachingSize(state.toString(), 9) + "  ║");

        drawable.add(0, "╚══════════════════════════╝");

        return drawable;

    }

    public static Column buildReports(SimpleModel simpleModel) {

        VaticanReportInfo vaticanReportInfo = simpleModel.getElem(VaticanReportInfo.class).get();
        PlayersInfo playersInfo = simpleModel.getElem(PlayersInfo.class).get();
        int players = playersInfo.getSimplePlayerInfoMap().size();

        Column grid = new Column();
        Row row = new Row();

        for (int i = 0; i < players; i++) {
            SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(i);
            Option o = Option.noNumber(fromVaticanReportStatus(vaticanReportInfo, i, playerInfo));
            row.addElem(o);
            row.addElem(new SizedBox(0, 2));
        }

        grid.addElem(row);

        return grid;
    }
}
