package it.polimi.ingsw.client.view.CLI.IDLE;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;

public class PlayersInfoCLI extends IDLEViewBuilderCLI {

    @Override
    public void run() {
        buildView();
    }

    @Override
    protected void buildView(){
        getCLIView().setTitle("Players info");

        Column grid = buildInfo();
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        grid.selectInEnabledOption(getCLIView(), "Select an option or press ENTER to go back",  () -> getClient().changeViewBuilder(new IDLEViewBuilderCLI()));
        getCLIView().show();
    }

    private Column buildInfo(){

        PlayersInfo playersInfo = getSimpleModel().getElem(PlayersInfo.class).get();
        int players = playersInfo.getSimplePlayerInfoMap().size();

        Column grid = new Column();
        Row row = new Row();

        for (int i = 0; i<players; i++) {
            int playerIndex = i;
            Option o = Option.from(fromPlayerInfo(playersInfo, i),  () -> getClient().changeViewBuilder(new PersonalBoardCLI(playerIndex)));
            row.addElem(o);
            row.addElem(new SizedBox(2,0));
        }
        grid.addElem(row);
      return grid;

    }

    private Drawable fromPlayerInfo(PlayersInfo playersInfo, int playerIndex){

        Background back = Background.DEFAULT;

        PlayersInfo.SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        String nickname = playerInfo.getNickname();
        int position = playerInfo.getCurrentPosition();
        int index = playerInfo.getPlayerIndex();
        int victoryPoints = playerInfo.getCurrentVictoryPoints();

        String farthestPlayer = Color.colorString("Farthest player!", Color.RED);
        String online = Color.colorString("Online", Color.BIGHT_GREEN);
        String offline = Color.colorString("Offline", Color.RED);

        String connection = playerInfo.isOnline() ? online : offline;


        Drawable drawable= new Drawable();
        drawable.add(0,"╔═        ═══════════════════╗");
        drawable.add(new DrawableLine(3,0, nickname, Color.BRIGHT_WHITE, back));
        drawable.add(0,"║ Player index : " + StringUtil.untilReachingSize(index, 2)+"          ║");
        drawable.add(0,"║ ────────────────────────── ║");
        drawable.add(0,"║ Connection : " + connection+ "        ║");
        drawable.add(0,"║ ────────────────────────── ║");
        drawable.add(0,"║ Faith track position : "  + StringUtil.untilReachingSize(position,7)+"       ║");
        drawable.add(0,"║ ────────────────────────── ║");
        drawable.add(0,"║ Points : " + StringUtil.untilReachingSize(victoryPoints, 2) + "                ║");
        drawable.add(0,"║ ────────────────────────── ║");

        if(playersInfo.getFarthestPlayer().contains(index))
            drawable.add(0,"║ "+ farthestPlayer + "           ║");
        else
            drawable.add(0,"║                          ║");

        drawable.add(0,"╚════════════════════════════╝");

        return drawable;


    }


}
