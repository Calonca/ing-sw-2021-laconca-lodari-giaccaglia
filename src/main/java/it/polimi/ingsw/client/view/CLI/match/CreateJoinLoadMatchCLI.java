package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Timer;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import it.polimi.ingsw.network.util.Util;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Stream;

public class CreateJoinLoadMatchCLI extends CreateJoinLoadMatchViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        getCLIView().setTitle(new Title("Hey " + getCommonData().getThisPlayerNickname() + ", what do you want to do?" , Color.BLUE));
        Stream<GridElem> optionsToAdd = getNewOptionList();
        Row initialRow = new Row(optionsToAdd);
        CanvasBody horizontalListBody = CanvasBody.centered(initialRow);
        getCLIView().setBody(horizontalListBody);
        initialRow.selectInEnabledOption(getCLIView() , "Select an active option");
        getCLIView().show();
    }



    private Stream<GridElem> getNewOptionList(){

        Option loadMatch = Option.from("Load Match" , () -> getClient().changeViewBuilder(new LoadableMatchesCLI()));
        if(getCommonData().getSavedMatchesData().isEmpty())
            loadMatch.setEnabled(false);

        Option joinMatch = Option.from("Join Match" , () -> getClient().changeViewBuilder(new JoinableMatchesCLI()));
        if(getCommonData().getAvailableMatchesData().isEmpty())
            joinMatch.setEnabled(false);

        Option newMatch = Option.from("New Match",()->getClient().changeViewBuilder(new CreateMatchCLI()));

        List<Option> optionList = new ArrayList<>();
        Collections.addAll(optionList, loadMatch, joinMatch, newMatch);

        return optionList.stream().flatMap(o->Stream.of(o, new SizedBox(1,0)));
    }

    /**
     * @param matchesData is the matches data map
     * @return the corresponding Option stream
     */
    protected Column buildMatchInfo(Map<UUID,  Pair<String[], String[]>> matchesData){

        Column grid = new Column();
        int i=0;

        List<Map<UUID,  Pair<String[], String[]>>> listOfMaps = Util.getListOfSmallerMaps(matchesData, 5);

        listOfMaps.forEach(map -> {

            Row row = new Row();

            map.forEach((matchId , matchPlayersPair) -> {

                Column lobbyInfo = row.addAndGetColumn();
                Option lobby = Option.from(
                        buildLobbyBox(matchPlayersPair.getKey() , matchPlayersPair.getValue() , matchId),
                        getRunnableForMatch(matchId));

                lobbyInfo.addElem(lobby);
                lobbyInfo.addElem(new SizedBox(3 , 0));

            });

            grid.addElem(row);

        });


        return grid;

    }

    private Runnable getRunnableForMatch(UUID matchId){

        LobbyViewBuilderCLI joinMatch = new LobbyViewBuilderCLI();
        joinMatch.setMatchId( matchId );

        return () -> {

            getClient().changeViewBuilder( joinMatch );
            Timer.showSecondsOnCLI(getCLIView(), "Loading data, seconds left : ", 3);
            getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,getCommonData().getThisPlayerNickname()));

        };
    }

    private static Drawable buildLobbyBox(String[] onlinePlayers, String[] offlinePlayers, UUID matchId){

        Drawable drawable= new Drawable();


        System.out.println(onlinePlayers.length + " " + offlinePlayers.length);


        drawable.add(0,"╔════════════════════════════╗");
        drawable.add(0,"║                            ║");
        drawable.add(new DrawableLine(8, 1, "Lobby Players", Color.BRIGHT_GREEN, Background.DEFAULT));
        drawable.add(0,"║ Match ID : " + StringUtil.untilReachingSize(matchId.toString().substring(0,8), 16)  + "║");
        drawable.add(0,"║════════════════════════════║");


        int posY = 0;

        for(int i=0; i<onlinePlayers.length; i++){

            posY = 3 + i*3 + 2;
            int playerIndex = i+1;
            if(onlinePlayers[i].equals("available slot")){
                drawable.add(0,"║                            ║");
                drawable.add(0,"║" + StringUtil.untilReachingSize("     × Available slot × ", 28) + "║");
            }
            else {

                drawable.add(0,"║ Player " + playerIndex + " is : " + StringUtil.untilReachingSize(onlinePlayers[i], 13) + "║");
                drawable.add(0,"║ Connection :               ║");
                drawable.add(new DrawableLine(15, posY, "Online", Color.BRIGHT_GREEN, Background.DEFAULT));

            }

            drawable.add(0, "║ ────────────────────────── ║");
        }


        if(posY == 0)
            posY = 2;


        for(int i=0; i<offlinePlayers.length; i++) {


            posY = posY + 3;
            int playerIndex = i + 1;
            if (offlinePlayers[i].equals("available slot")) {
                drawable.add(0, "║                            ║");
                drawable.add(0, "║" + StringUtil.untilReachingSize("     × Available slot × ", 28) + "║");
            } else {

                drawable.add(0, "║ Player " + playerIndex + " is : " + StringUtil.untilReachingSize(offlinePlayers[i], 13) + "║");
                drawable.add(0, "║ Connection :               ║");
                drawable.add(new DrawableLine(14, posY, " Offline", Color.RED, Background.DEFAULT));

                if (i != offlinePlayers.length - 1)
                    drawable.add(0, "║ ────────────────────────── ║");

            }
        }

        drawable.add(0,"╚════════════════════════════╝");

        return drawable;
    }


}
