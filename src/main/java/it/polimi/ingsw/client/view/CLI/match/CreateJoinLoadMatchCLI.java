package it.polimi.ingsw.client.view.CLI.match;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIBuilder;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.client.view.abstractview.CreateJoinLoadMatchViewBuilder;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.SendNickname;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Stream;

public class CreateJoinLoadMatchCLI extends CreateJoinLoadMatchViewBuilder implements CLIBuilder {

    @Override
    public void run() {

        SpinnerBody spinnerBody = new SpinnerBody();
        getCLIView().setTitle("Waiting for matches data");

        spinnerBody.performWhenReceiving(CommonData.matchesDataString);
        spinnerBody.setPerformer(()->

                {
                    getCLIView().clearScreen();
                    getCLIView().setTitle(new Title("Hey "+ getCommonData().getCurrentNick()+", what do you want to do?"));

                    Stream<GridElem> optionsToAdd = getNewOptionList();
                    Row initialRow = new Row(optionsToAdd);

                    CanvasBody horizontalListBody = CanvasBody.centered(initialRow);

                    horizontalListBody.performWhenReceiving(CommonData.matchesDataString);
                    Runnable performer = ()->{

                        Stream<GridElem> updatedOptionsToAdd = getNewOptionList();
                        Row updatedRow = new Row((updatedOptionsToAdd));
                        getCLIView().setBody(CanvasBody.centered(updatedRow));
                        updatedRow.selectInEnabledOption(getCLIView(), "", () -> getClient().changeViewBuilder(new CreateJoinLoadMatchCLI()));
                        getCLIView().show();

                    };

                    horizontalListBody.setPerformer(performer);
                    getCLIView().setBody(horizontalListBody);
                    initialRow.selectAndRunOption(getCLIView());
                    getCLIView().show();
                }

        );

        getCLIView().setBody(spinnerBody);
        getCLIView().show();

    }

    protected Option getOption(Map.Entry<UUID,Pair<String[], String[]>> uuidPair) {

        LobbyViewBuilderCLI joinMatch = new LobbyViewBuilderCLI();
        joinMatch.setMatchId( uuidPair.getKey() );
        Runnable r = () -> {
            getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(uuidPair.getKey(),getCommonData().getCurrentNick()));
            getClient().changeViewBuilder( joinMatch );
        };

        return Option.from(
                idAndNames(uuidPair).getKey(),
                idAndNames(uuidPair).getValue(),
                r );
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
    protected Stream<Option> getMatchesOptionList(Map<UUID, Pair<String[], String[]>> matchesData){

        Option loadMatch = Option.from("Go back" , () -> {
            getClient().getServerHandler().sendCommandMessage(new SendNickname(getCommonData().getCurrentNick()));
            getClient().changeViewBuilder(new CreateJoinLoadMatchCLI());
        });

        Stream<Option> optionsToAdd = matchesData.entrySet().stream().map(this::getOption);
        return Stream.concat( optionsToAdd,Stream.of(loadMatch));

    }

    protected Column buildMatchInfo(Map<UUID,  Pair<String[], String[]>> match){

        Column grid = new Column();
        Row row = new Row();

        match.forEach((key, matchPlayersPair) -> {
            Column lobbyInfo = row.addAndGetColumn();
            Option lobby = Option.from(
                    buildLobbyBox(matchPlayersPair.getKey(), matchPlayersPair.getValue(), key), getRunnableForMatch(key));

            lobbyInfo.addElem(lobby);
            lobbyInfo.addElem(new SizedBox(3, 0 ));
        });

        grid.addElem(row);
        return grid;

    }

    private Runnable getRunnableForMatch(UUID matchId){

        LobbyViewBuilderCLI joinMatch = new LobbyViewBuilderCLI();
        joinMatch.setMatchId( matchId );

        return () -> {
            getClient().getServerHandler().sendCommandMessage(new JoinMatchRequest(matchId,getCommonData().getCurrentNick()));
            getClient().changeViewBuilder( joinMatch );
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
