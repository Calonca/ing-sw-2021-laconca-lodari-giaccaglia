package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.VaticanReportInfo;
import javafx.util.Pair;

public class DrawableReportInfo {

    public static Drawable fromVaticanReportStatus(VaticanReportInfo vaticanReportInfo, int playerIndex, PlayersInfo.SimplePlayerInfo playerInfo) {

        Background back = Background.DEFAULT;

        boolean hasTriggeredVaticanReport = vaticanReportInfo.getPlayersTriggeringVaticanReport().contains(playerIndex);
        String nickname = playerInfo.getNickname();

        int position = playerInfo.getCurrentPosition();
        Pair<Integer, Boolean> tile = vaticanReportInfo.getPopeTileStatusMap().get(playerIndex);
        int tileNumber = tile.getKey();
        String state = tile.getValue() ? "ACTIVE" : "DISCARDED";

        Drawable drawable = new Drawable();
        drawable.add(0, "╔═        ═════════════════╗");
        drawable.add(new DrawableLine(3, 0, nickname, Color.BRIGHT_WHITE, back));
        drawable.add(0, "║ Faith Track Position : " + StringUtil.untilReachingSize(position, 2) + "               ║");

        if (hasTriggeredVaticanReport) {
            drawable.add(0, "║═══════ Triggered ════════║");
            drawable.add(0, "║════ Vatican Report ══════║");
        } else {
            drawable.add(0, "║                          ║");
            drawable.add(0, "║                          ║");
        }


        drawable.add(0, "║═══ Pope Tile Status ═════║");

        drawable.add(0, "║ Tile Number : " + StringUtil.untilReachingSize(tileNumber, 2) + "║");
        drawable.add(0, "║ Tile State : " + StringUtil.untilReachingSize(state, 2) + "     ║");

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
            PlayersInfo.SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(i);
            Option o = Option.noNumber(fromVaticanReportStatus(vaticanReportInfo, i, playerInfo));
            row.addElem(o);
            row.addElem(new SizedBox(0, 2));
        }

        grid.addElem(row);

        return grid;
    }
}
