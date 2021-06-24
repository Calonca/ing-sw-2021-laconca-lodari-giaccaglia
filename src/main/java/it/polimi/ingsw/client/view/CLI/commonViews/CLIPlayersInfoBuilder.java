package it.polimi.ingsw.client.view.CLI.commonViews;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.CLI.idle.LeadersCLI;
import it.polimi.ingsw.client.view.CLI.idle.PersonalBoardCLI;
import it.polimi.ingsw.client.view.CLI.idle.PlayersInfoCLI;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.middle.MiddlePersonalBoardCLI;
import it.polimi.ingsw.client.view.CLI.middle.MiddlePhaseCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.beans.PropertyChangeEvent;

public final class CLIPlayersInfoBuilder extends ViewBuilder {

     public static void buildView(ViewBuilder nextViewBuilder){
        getCLIView().setTitle("Players info");

        Column grid = buildInfo();
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        grid.selectInEnabledOption(getCLIView(), "Select an option or press ENTER to go back",  () -> getClient().changeViewBuilder(nextViewBuilder));
        getCLIView().show();

    }

    private static Column buildInfo(){

        PlayersInfo playersInfo = getSimpleModel().getElem(PlayersInfo.class).get();
        int players = playersInfo.getSimplePlayerInfoMap().size();

        Column grid = new Column();
        Row row = new Row();

        ViewBuilder boardViewBuilder, leadersViewBuilder;

        for (int playerIndex = 0; playerIndex<players; playerIndex++) {

            Column leaderCard = row.addAndGetColumn();

            if(getThisPlayerCache().getCurrentState().equals(State.IDLE.toString())) {
                boardViewBuilder = new PersonalBoardCLI(playerIndex, false, PersonalBoardBody.ViewMode.PLAYERS_INFO_IDLE);
                leadersViewBuilder = new LeadersCLI(new PlayersInfoCLI(), playerIndex);
            }
            else {
                boardViewBuilder = new MiddlePersonalBoardCLI(playerIndex, false, PersonalBoardBody.ViewMode.PLAYERS_INFO_MIDDLE);
                leadersViewBuilder = new LeadersCLI(new MiddlePhaseCLI(), playerIndex);
            }


            ViewBuilder finalBoardViewBuilder = boardViewBuilder;
            ViewBuilder finalLeadersViewBuilder = leadersViewBuilder;
            Option visibleInfo = Option.noNumber(fromPlayerInfo(playersInfo, playerIndex));
            leaderCard.addElem(visibleInfo);


            Option personalBoardOption = Option.from("Personal Board",  () -> getClient().changeViewBuilder(finalBoardViewBuilder));
            Option line1 = Option.noNumber("──────────────────────────");
            Option line2 = Option.noNumber("──────────────────────────");
            Option playerLeadersOption = Option.from("Leaders", () -> getClient().changeViewBuilder(finalLeadersViewBuilder));



            String state = getSimpleModel().getPlayerCache(playerIndex).getCurrentState();
            SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(playerIndex).getElem(SimplePlayerLeaders.class).orElseThrow();


            if(simplePlayerLeaders.getPlayerLeaders().isEmpty())
                playerLeadersOption = Option.noNumber(" No leaders available!");
            if(state.equals(State.SETUP_PHASE.toString()))
                playerLeadersOption = Option.noNumber(" Player is choosing match Leaders!");
            else if(state.equals("BEFORE_SETUP"))
                playerLeadersOption = Option.noNumber(" Player is waiting for his setup turn");

            Row boardRow = leaderCard.addAndGetRow();
            boardRow.addElem(personalBoardOption);
            boardRow.addElem(new SizedBox(2,0));

            Row lineRow1 = leaderCard.addAndGetRow();
            lineRow1.addElem(new SizedBox(2, 0));
            lineRow1.addElem(line1);

            Row leadersRow = leaderCard.addAndGetRow();
            leadersRow.addElem(playerLeadersOption);
            leadersRow.addElem(new SizedBox(2,0));

            Row lineRow2 = leaderCard.addAndGetRow();
            lineRow2.addElem(new SizedBox(2, 0));
            lineRow2.addElem(line2);

            boardRow.setFirstIdx(playerIndex*2);
        }
        grid.addElem(row);
        return grid;

    }

    private static Drawable fromPlayerInfo(PlayersInfo playersInfo, int playerIndex){

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


        int nicknameLength = nickname.length();
        String spaces = StringUtil.spaces(nicknameLength + 1);

        String delimiter = "═".repeat(30-3-nicknameLength-1);

        Drawable drawable= new Drawable();
        drawable.add(0,"╔══");
        drawable.add(new DrawableLine(3,0,nickname, Color.BRIGHT_WHITE, back));
        drawable.add(new DrawableLine(3+nicknameLength,0, delimiter));
        drawable.add(new DrawableLine(29, 0, "╗"));

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


        /*
                Background back = Background.DEFAULT;

        PlayersInfo.SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        String nickname = playerInfo.getNickname();
        int position = playerInfo.getCurrentPosition();
        int index = playerInfo.getPlayerIndex();
        int victoryPoints = playerInfo.getCurrentVictoryPoints();

        DrawableLine farthestPlayer = new DrawableLine(1, 0, "Farthest player!", Color.RED, Background.DEFAULT);

        String online = Color.colorString("Online", Color.BIGHT_GREEN);
        String offline = Color.colorString("Offline", Color.RED);

        DrawableLine playerConnectionStatus;
        if(playerInfo.isOnline())
            playerConnectionStatus = new DrawableLine(1, 0, "Online", Color.BIGHT_GREEN, Background.DEFAULT);
        else
            playerConnectionStatus = new DrawableLine(1, 0, "Offline", Color.RED, Background.DEFAULT);

         */


    }


    @Override
    public void run() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
