package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.match.LobbyViewBuilderCLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import javafx.util.Pair;

import java.beans.PropertyChangeEvent;
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
        String[] players = availableMatches.get(matchId).getKey();  // picking online players !!

        int leftPlayers = (int)(Arrays.stream(players).filter(player -> player.equals("available slot")).count());

        Column grid = new Column();
        Option lobby = Option.noNumber(lobbyBox(players));
        grid.addElem(lobby);
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);
        if(leftPlayers!=0)
            getCLIView().runOnInput("Waiting for other players to join, " + leftPlayers + " more to start!", () -> getClient().changeViewBuilder(new LobbyViewBuilderCLI()));
        else
            getCLIView().runOnInput("Waiting for your turn", () -> getClient().changeViewBuilder(new LobbyViewBuilderCLI()));
        getCLIView().show();

    }


    private static Drawable lobbyBox(String[] players){


        int playerindex = getCommonData().getCurrentPlayerIndex();
        String playerInSetup;

        Drawable drawable= new Drawable();
        int yPosMatchPlayerText = 1;
        if(playerindex!=-1 && !players[playerindex].equals("available slot")) {

            if(playerindex == getCommonData().getThisPlayerIndex())
                playerInSetup = "Your Setup Phase is starting!";
            else
                playerInSetup = players[playerindex] + " is in Setup Phase";

            drawable.add(0, " " + playerInSetup, Color.BRIGHT_YELLOW, Background.DEFAULT);
            drawable.add(0, "");
            yPosMatchPlayerText = 3;
        }



        drawable.add(0,"╔════════════════════════════╗");
        drawable.add(0,"║                            ║");
        drawable.add(new DrawableLine(8, yPosMatchPlayerText, "Match Players", Color.BRIGHT_GREEN, Background.DEFAULT));
        drawable.add(0,"║ ────────────────────────── ║");


        for(int i=0; i<players.length; i++){
            int playerIndex = i+1;
            if(players[i].equals("available slot")){
                drawable.add(0, "║" + StringUtil.untilReachingSize(" Waiting for player", 28) + "║");
            }
            else
                drawable.add(0, "║ Player " + playerIndex + " is : " + StringUtil.untilReachingSize(players[i], 13) + "║");

            if(i!=players.length -1)
                drawable.add(0, "║ ────────────────────────── ║");
        }

        drawable.add(0,"╚════════════════════════════╝");

        return drawable;

    }


    @Override
    public void run() {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
