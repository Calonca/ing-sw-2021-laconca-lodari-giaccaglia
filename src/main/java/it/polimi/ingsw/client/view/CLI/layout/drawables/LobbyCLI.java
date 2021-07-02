package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.match.LobbyViewBuilderCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class LobbyCLI extends LobbyViewBuilderCLI {

    /**
     * Called for every player or any match update
     */

    public static void buildLobbyCLI() {

        getCLIView().setTitle("Players in lobby");

        Map<UUID, Pair<String[], String[]>> availableMatches = getCommonData().getAvailableMatchesData().get();

        UUID matchId = getClient().getCommonData().getMatchId().get();
        String[] onlinePlayers = availableMatches.get(matchId).getKey();
        String[] offlinePlayers = availableMatches.get(matchId).getValue();

        int leftPlayers = (int)(Arrays.stream(onlinePlayers).filter(player -> player.equals("available slot")).count());

        Column grid = new Column();
        Option lobby = Option.noNumber(lobbyBox(onlinePlayers, offlinePlayers));
        grid.addElem(lobby);
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        if(leftPlayers!=0)
            getCLIView().runOnInput("Waiting for other players to join, " + leftPlayers + " more to start!", () -> getClient().changeViewBuilder(new LobbyViewBuilderCLI()));
        else
            getCLIView().runOnInput("Waiting for your turn", () -> getClient().changeViewBuilder(new LobbyViewBuilderCLI()));
        getCLIView().show();

    }


    private static Drawable lobbyBox(String[] onlinePlayers, String[] offlinePlayers){

        String yellowText;

        Drawable drawable= new Drawable();
        int yPos = 1;

        if(!getCommonData().getCurrentPlayerNickname().isBlank()) {

            if (getCommonData().getCurrentPlayerIndex() == CommonData.getThisPlayerIndex()){
                if(getThisPlayerCache() != null && getThisPlayerCache().getCurrentState().equals("SETUP_PHASE"))
                    yellowText = "Your setup phase is starting!";
                else
                    yellowText = "   Your turn is starting!";
            }

            else
                yellowText = getCommonData().getCurrentPlayerNickname() + " is in Setup Phase";

            drawable.add(0, " " + yellowText, Color.BRIGHT_YELLOW, Background.DEFAULT);
            drawable.add(0, "");
            yPos = 3;

        }

        drawable.add(0,"╔════════════════════════════╗");
        drawable.add(0,"║                            ║");
        drawable.add(new DrawableLine(8, yPos, "Match Players", Color.BRIGHT_GREEN, Background.DEFAULT));
        drawable.add(0,"║ ────────────────────────── ║");


        int playerIndex;

        for(int i=0; i<onlinePlayers.length; i++){
            playerIndex = i+1;
            if(onlinePlayers[i].equals("available slot")){
                drawable.add(0, "║" + StringUtil.untilReachingSize(" Waiting for player", 28) + "║");
            }
            else
                drawable.add(0, "║ Player " + playerIndex + " is : " + StringUtil.untilReachingSize(onlinePlayers[i], 13) + "║");
                drawable.add(0, "║ ────────────────────────── ║");
        }

        if(offlinePlayers.length>0) {

            yPos = yPos + 2 + onlinePlayers.length * 2;

            drawable.add(0, "║                            ║");
            drawable.add(new DrawableLine(7, yPos, "Offline Players", Color.RED, Background.DEFAULT));

            for (int i = 0; i < offlinePlayers.length; i++) {
                String offlinePlayerString =  "× " + offlinePlayers[i] + " ×";
                drawable.add(0, "║ " + StringUtil.untilReachingSize(offlinePlayerString, 27) + "║");

                if (i != onlinePlayers.length - 1)
                    drawable.add(0, "║ ────────────────────────── ║");
            }

        }



        drawable.add(0,"╚════════════════════════════╝");

        return drawable;

    }


}
