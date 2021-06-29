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
import it.polimi.ingsw.client.view.CLI.middle.MiddlePlayersInfoCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.*;

public final class CLIPlayersInfoBuilder{

     public static void buildView(ViewBuilder nextViewBuilder){
        getCLIView().setTitle("Players info");

        Column grid = buildInfo();
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        grid.selectInEnabledOption(getCLIView(), "Select an option or press ENTER to go back",  () -> getClient().changeViewBuilder(nextViewBuilder));
        getCLIView().show();

    }

    /**
     * @return a column displaying PlayersInfo class informations
     */
    private static Column buildInfo(){

        PlayersInfo playersInfo = getSimpleModel().getElem(PlayersInfo.class).get();
        int players = playersInfo.getSimplePlayerInfoMap().size();

        Column grid = new Column();
        Row row = new Row();

        ViewBuilder boardViewBuilder, leadersViewBuilder;

        for (int playerIndex = 0; playerIndex<players; playerIndex++) {

            Column playerInfo = row.addAndGetColumn();

            if(getThisPlayerCache().getCurrentState().equals(State.IDLE.toString())) {
                boardViewBuilder = new PersonalBoardCLI(playerIndex, false, PersonalBoardBody.ViewMode.PLAYERS_INFO_IDLE);
                leadersViewBuilder = new LeadersCLI(new PlayersInfoCLI(), playerIndex);
            }
            else {
                boardViewBuilder = new MiddlePersonalBoardCLI(playerIndex, false, PersonalBoardBody.ViewMode.PLAYERS_INFO_MIDDLE);
                leadersViewBuilder = new LeadersCLI(new MiddlePlayersInfoCLI(), playerIndex);
            }


            ViewBuilder finalBoardViewBuilder = boardViewBuilder;
            ViewBuilder finalLeadersViewBuilder = leadersViewBuilder;
            Option visibleInfo = Option.noNumber(fromPlayerInfo(playersInfo, playerIndex));
            playerInfo.addElem(visibleInfo);


            Option personalBoardOption = Option.from("Personal Board",  () -> getClient().changeViewBuilder(finalBoardViewBuilder));
            Option line1 = Option.noNumber("──────────────────────────");
            Option line2 = Option.noNumber("──────────────────────────");
            Option playerLeadersOption = Option.from("Leaders", () -> getClient().changeViewBuilder(finalLeadersViewBuilder));



            String state = getSimpleModel().getPlayerCache(playerIndex).getCurrentState();
            SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(playerIndex).getElem(SimplePlayerLeaders.class).orElseThrow();



            if(simplePlayerLeaders.getPlayerLeaders().isEmpty())
                playerLeadersOption = Option.noNumber(" No leaders available!");

            if(state.equals(State.SETUP_PHASE.toString())) {
                playerLeadersOption = Option.noNumber(" Player is choosing match Leaders!");
                personalBoardOption = Option.noNumber(" Player is setting up the Board");
            }
            else if(state.equals("BEFORE_SETUP")) {
                personalBoardOption = Option.noNumber(" Player is waiting for his setup turn");
                playerLeadersOption = Option.noNumber(" Player is waiting for his setup turn");
            }




            Row boardRow = playerInfo.addAndGetRow();
            boardRow.addElem(personalBoardOption);

            Row lineRow1 = playerInfo.addAndGetRow();
            lineRow1.addElem(line1);

            Row leadersRow = playerInfo.addAndGetRow();
            leadersRow.addElem(playerLeadersOption);

            Row lineRow2 = playerInfo.addAndGetRow();
            lineRow2.addElem(line2);

            playerInfo.addElem(new SizedBox(3, 0 ));

            boardRow.setFirstIdx(playerIndex*2);
        }
        grid.addElem(row);
        return grid;

    }


    /**
     * @param playersInfo is a SimpleNodel element
     * @param playerIndex is the corresponding player's index
     * @return the corresponding Drawable
     */
    private static Drawable fromPlayerInfo(PlayersInfo playersInfo, int playerIndex){

        Background back = Background.DEFAULT;

        SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        String nickname = playerInfo.getNickname();
        int position = playerInfo.getCurrentPosition();
        int victoryPoints = playerInfo.getCurrentVictoryPoints();


        int nicknameLength = nickname.length();
        String spaces = StringUtil.spaces(nicknameLength + 1);

        String delimiter = "═".repeat(30-3-nicknameLength-1);

        Drawable drawable= new Drawable();
        drawable.add(0,"╔══");
        drawable.add(new DrawableLine(3,0,nickname, Color.BRIGHT_WHITE, back));
        drawable.add(new DrawableLine(3+nicknameLength,0, delimiter));
        drawable.add(new DrawableLine(29, 0, "╗"));

        drawable.add(0,"║ Player index : " + StringUtil.untilReachingSize(playerIndex + 1, 2)+"          ║");
        drawable.add(0,"║ ────────────────────────── ║");
        drawable.add(0,"║ Connection :               ║");

        if(playerInfo.isOnline())
            drawable.add(new DrawableLine(14, 3, " Online", Color.BRIGHT_GREEN, Background.DEFAULT));
        else
            drawable.add(new DrawableLine(14, 3, " Offline", Color.RED, Background.DEFAULT));


        drawable.add(0,"║ ────────────────────────── ║");
        drawable.add(0,"║ Faith track position : "  + StringUtil.untilReachingSize(position,2)+"  ║");

        if(playersInfo.getSimplePlayerInfoMap().size()==1) {

            int lorenzoPosition = playersInfo.getLorenzoPosition();
            drawable.add(0, "║ Lorenzo position     : " + StringUtil.untilReachingSize(lorenzoPosition, 2) + "  ║");

        }
        drawable.add(0,"║ ────────────────────────── ║");
        drawable.add(0,"║ Points : " + StringUtil.untilReachingSize(victoryPoints, 3) + "               ║");
        drawable.add(0,"║ ────────────────────────── ║");

        drawable.add(0,"║                            ║");

        int yPos;

        if(playersInfo.getSimplePlayerInfoMap().size()==1)
            yPos = 10;
        else
            yPos = 9;

        if(playersInfo.getSimplePlayerInfoMap().size()>1 && playersInfo.getFarthestPlayer().contains(playerIndex)) {
            drawable.add(0,"║                            ║");
            drawable.add(new DrawableLine(2, yPos, "     Farthest player!", Color.RED, Background.DEFAULT));


        }
        else if(playersInfo.getSimplePlayerInfoMap().size()==1){
            drawable.add(0,"║                            ║");
            if(playerInfo.getCurrentPosition()>playerInfo.getLorenzoPosition())
                drawable.add(new DrawableLine(2, yPos, "  You are ahead Lorenzo!", Color.BRIGHT_RED, Background.DEFAULT));
            else if(playerInfo.getCurrentPosition()<playerInfo.getLorenzoPosition())
                drawable.add(new DrawableLine(2, yPos, "  Lorenzo is ahead you!", Color.RED, Background.DEFAULT));
            else
                drawable.add(new DrawableLine(2, yPos, "Same position of Lorenzo!", Color.YELLOW, Background.DEFAULT));
        }

        else
            drawable.add(0,"║                            ║");

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

        String online = Color.colorString("Online", Color.BRIGHT_GREEN);
        String offline = Color.colorString("Offline", Color.RED);

        DrawableLine playerConnectionStatus;
        if(playerInfo.isOnline())
            playerConnectionStatus = new DrawableLine(1, 0, "Online", Color.BRIGHT_GREEN, Background.DEFAULT);
        else
            playerConnectionStatus = new DrawableLine(1, 0, "Offline", Color.RED, Background.DEFAULT);

         */


    }
}
