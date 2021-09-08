package it.polimi.ingsw.client.view.cli.endgame;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.cli.CLIBuilder;
import it.polimi.ingsw.client.view.cli.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.cli.CLIelem.body.PersonalBoardBody;
import it.polimi.ingsw.client.view.cli.commonViews.CLILeadersBuilder;
import it.polimi.ingsw.client.view.cli.commonViews.CLIPersonalBoardBuilder;
import it.polimi.ingsw.client.view.cli.commonViews.CLIReportInfoBuilder;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.cli.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.WinLooseBuilder;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.network.simplemodel.PlayersInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerLeaders;

import java.beans.PropertyChangeEvent;
import java.util.Map;

public class WinLooseCLI extends WinLooseBuilder implements CLIBuilder {

    @Override
    public void run() {

        getClient().saveViewBuilder(this);
        CLIReportInfoBuilder.showVaticanReportInfoBeforeTransition(new EndGameReportInfoCLI());

        Column grid = buildInfo();
        CanvasBody body = CanvasBody.centered(grid);
        getCLIView().setTitle("Game is over!");
        getCLIView().removeSubTitle();
        getCLIView().setBody(body);
        grid.selectInEnabledOption(getCLIView(), "Game is over!", () -> {});
        getCLIView().show();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //empty method because there's no need to handle property changes.
    }


    /**
     * @return a column displaying PlayersInfo data regarding the end of the game
     */
    private static Column buildInfo(){

        PlayersInfo playersInfo = getSimpleModel().getElem(PlayersInfo.class).get();
        int players = playersInfo.getSimplePlayerInfoMap().size();

        Column grid = new Column();
        Row row = new Row();

        for (int playerIndex = 0; playerIndex<players; playerIndex++) {

            Column playerInfo = row.addAndGetColumn();

            Option visibleInfo = Option.noNumber(fromPlayerInfo(playersInfo, playerIndex));
            playerInfo.addElem(visibleInfo);


            int finalPlayerIndex = playerIndex;
            Option personalBoardOption = Option.from("Personal Board",  () -> CLIPersonalBoardBuilder.buildViewForViewing(finalPlayerIndex, PersonalBoardBody.ViewMode.PLAYERS_INFO_END));
            Option line1 = Option.noNumber("──────────────────────────");
            Option line2 = Option.noNumber("──────────────────────────");
            Option playerLeadersOption = Option.from("Leaders", () -> CLILeadersBuilder.buildView(new WinLooseCLI(), finalPlayerIndex));

            SimplePlayerLeaders simplePlayerLeaders = getSimpleModel().getPlayerCache(playerIndex).getElem(SimplePlayerLeaders.class).orElseThrow();



            if(simplePlayerLeaders.getPlayerLeaders().isEmpty())
                playerLeadersOption = Option.noNumber(" No leaders available!");


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

    private static Drawable fromPlayerInfo(PlayersInfo playersInfo, int playerIndex){

        Background back = Background.DEFAULT;

        SimplePlayerInfo playerInfo = playersInfo.getSimplePlayerInfoMap().get(playerIndex);
        EndGameInfo endGameInfo = getSimpleModel().getElem(EndGameInfo.class).orElseThrow();
        endGameInfo.handleCauseOfEndString(playersInfo.getSimplePlayerInfoMap());
        Map<Integer, Boolean> matchOutcomesMap = endGameInfo.getMatchOutcomeMap();
        String nickname = playerInfo.getNickname();
        int position = playerInfo.getCurrentPosition();
        int victoryPoints = playerInfo.getCurrentVictoryPoints();
        int nicknameLength = nickname.length();
        String delimiter = "═".repeat(30-3-nicknameLength-1);

        Drawable drawable= new Drawable();
        drawable.add(0,"╔══");
        drawable.add(new DrawableLine(3,0,nickname, Color.BRIGHT_WHITE, back));
        drawable.add(new DrawableLine(3+nicknameLength,0, delimiter));
        drawable.add(new DrawableLine(29, 0, "╗"));

        drawable.add(0,"║ Player index : " + StringUtil.untilReachingSize(playerIndex + 1, 2)+"          ║");
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
        drawable.add(0,"║                            ║");


        boolean isInfoOfThisPlayer = playerIndex == CommonData.getThisPlayerIndex();
        String winString = isInfoOfThisPlayer ? "You won!" : nickname + " won!";
        String lostString = isInfoOfThisPlayer ? "You lost!" : nickname + " lost!";

        int yPos = playersInfo.getSimplePlayerInfoMap().size() == 1 ? 8 : 7;
        int xPos = isInfoOfThisPlayer ? 9 : 6;

        if(Boolean.TRUE.equals(matchOutcomesMap.get(playerIndex)))
            drawable.add(new DrawableLine(xPos, yPos, winString, Color.BRIGHT_GREEN, Background.DEFAULT));

        else
            drawable.add(new DrawableLine(xPos, yPos, lostString, Color.RED, Background.DEFAULT));

        drawable.add(0,"╚════════════════════════════╝");

        return drawable;


    }
}
