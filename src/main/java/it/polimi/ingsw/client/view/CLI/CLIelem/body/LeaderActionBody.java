package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLeader;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;


import java.util.List;

public class LeaderActionBody extends CLIelem {
    List<LeaderCardAsset> leaders;
    private final Client client;

    public LeaderActionBody(List<LeaderCardAsset> leaders, Client client) {
        this.leaders = leaders;
        this.client = client;
    }

    @Override
    public String toString() {
        HorizontalList leadersList = new HorizontalList(cli.getMaxBodyHeight());
        leadersList.setShowNumber(false);

        HorizontalList hiddenOptionList = new HorizontalList(cli.getMaxBodyHeight());

        for (int i = 0, leadersSize = leaders.size(); i < leadersSize; i++) {
            LeaderCardAsset l = leaders.get(i);

            Option activateOption = Option.from("",()-> {
                InitialOrFinalPhaseEvent activate = new InitialOrFinalPhaseEvent(1, l.getCardId());
                client.getServerHandler().sendCommandMessage(new EventMessage(activate));
            });
            Option discardOption = Option.from("",()-> {
                InitialOrFinalPhaseEvent discard = new InitialOrFinalPhaseEvent(0, l.getCardId());
                client.getServerHandler().sendCommandMessage(new EventMessage(discard));
            });
            hiddenOptionList.addOption(activateOption);
            hiddenOptionList.addOption(discardOption);

            Drawable listEntry = DrawableLeader.fromAsset(l,false);
            listEntry.add(0,i*2+": Activate "+(1+i*2)+": Discard", Color.OPTION, Background.DEFAULT);
            Option visOpt = Option.from(listEntry,()->{});
            leadersList.addOption(visOpt);

        }

        Drawable skipLeadersDw = new Drawable();
        skipLeadersDw.add(0, leaders.size()*2+": Skip",Color.OPTION,Background.DEFAULT);
        skipLeadersDw.shift(0,DrawableLeader.height());
        Option skipLeaderOpt = Option.from(skipLeadersDw,()->{
            client.getServerHandler().sendCommandMessage(new EventMessage(new InitialOrFinalPhaseEvent(2)));
        });
        hiddenOptionList.addOption(skipLeaderOpt);
        leadersList.addOption(skipLeaderOpt);

        hiddenOptionList.selectAndRunOption(cli);

        return leadersList.toString();
    }

}
