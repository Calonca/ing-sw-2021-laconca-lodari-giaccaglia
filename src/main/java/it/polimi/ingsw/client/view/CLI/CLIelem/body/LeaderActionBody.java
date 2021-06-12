package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.CLI.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.CLI.layout.SizedBox;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLeader;
import it.polimi.ingsw.client.view.CLI.layout.Option;
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
        Row leadersList = new Row();

        for (int i = 0, leadersSize = leaders.size(); i < leadersSize; i++) {
            LeaderCardAsset l = leaders.get(i);
            Column leaderCard = leadersList.addAndGetColumn();

            Drawable listEntry = DrawableLeader.fromAsset(l.getNetworkLeaderCard(), false);
            Option visOpt = Option.noNumber(listEntry);
            leaderCard.addElem(visOpt);

            Row optionList = leaderCard.addAndGetRow();

            Option activateOption = Option.from("Activate ", () -> {
                InitialOrFinalPhaseEvent activate = new InitialOrFinalPhaseEvent(1, l.getCardId());
                client.getServerHandler().sendCommandMessage(new EventMessage(activate));
            });
            activateOption.setEnabled(l.getNetworkLeaderCard().isPlayable());
            optionList.addElem(activateOption);


            Option discardOption = Option.from("Discard ", () -> {
                InitialOrFinalPhaseEvent discard = new InitialOrFinalPhaseEvent(0, l.getCardId());
                client.getServerHandler().sendCommandMessage(new EventMessage(discard));
            });
            discardOption.setEnabled(!l.getNetworkLeaderCard().isLeaderActive());
            optionList.addElem(discardOption);

            optionList.setFirstIdx(i*2);
        }

        Column skipColumn = leadersList.addAndGetColumn();
        skipColumn.addElem(new SizedBox(0,DrawableLeader.height()+1));

        Option skipLeaderOpt = Option.from("Skip",()->{
            client.getServerHandler().sendCommandMessage(new EventMessage(new InitialOrFinalPhaseEvent(2)));
        });
        skipColumn.addElem(skipLeaderOpt);
        skipLeaderOpt.setFirstIdx(leaders.size()*2);

        leadersList.selectInEnabledOption(cli,"Select a possible action");
        return CanvasBody.centered(leadersList).toString();
    }

}
