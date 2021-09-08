package it.polimi.ingsw.client.view.cli.CLIelem.body;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.cli.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.SizedBox;
import it.polimi.ingsw.client.view.cli.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.cli.layout.drawables.DrawableLeader;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Row;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.InitialOrFinalPhaseEvent;

import java.util.List;
import java.util.Objects;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getClient;

public class LeaderBody extends CLIelem {

    private final List<LeaderCardAsset> leaders;
    private final Client client;
    private ViewBuilder requestBuilder = null;


    public LeaderBody(List<LeaderCardAsset> leaders, Client client) {
        this.leaders = leaders;
        this.client = client;

    }

    public LeaderBody(List<LeaderCardAsset> leaders, Client client, ViewBuilder nextViewBuilder) {
        this.leaders = leaders;
        this.client = client;
        requestBuilder = nextViewBuilder;

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



            Option discardOption = Option.from("Discard ", () -> {
                InitialOrFinalPhaseEvent discard = new InitialOrFinalPhaseEvent(0, l.getCardId());
                client.getServerHandler().sendCommandMessage(new EventMessage(discard));
            });
            discardOption.setEnabled(!l.getNetworkLeaderCard().isLeaderActive());


            if(Objects.isNull(requestBuilder)) {
                optionList.addElem(activateOption);
                optionList.addElem(discardOption);
                optionList.setFirstIdx(i * 2);
            }
            else{
                Drawable leaderDw = new Drawable();

                if(!l.getNetworkLeaderCard().isLeaderActive() && l.getNetworkLeaderCard().isPlayable()) {
                    leaderDw.add(0, "Leader is inactive,",Color.YELLOW, Background.DEFAULT);
                    leaderDw.add(0, " but it's playable",Color.YELLOW, Background.DEFAULT);
                    optionList.addElem(new SizedBox(3, -2));
                }
                else if(!l.getNetworkLeaderCard().isLeaderActive()) {
                    leaderDw.add(0, "Leader is inactive" ,Color.RED, Background.DEFAULT);
                    optionList.addElem(new SizedBox(3, -2));
                }
                else {
                    leaderDw.add(0, "Leader is active" ,Color.GREEN, Background.DEFAULT);
                    optionList.addElem(new SizedBox(3, -2));
                }


                Option isActive = Option.from(leaderDw, () ->{});
                isActive.setMode(Option.VisMode.NO_NUMBER);

                optionList.addElem(isActive);

            }
        }

        if(Objects.isNull(requestBuilder)) {
            Column skipColumn = leadersList.addAndGetColumn();
            skipColumn.addElem(new SizedBox(0, DrawableLeader.height() + 1));

            Option skipLeaderOpt = Option.from("Skip", () -> client.getServerHandler().sendCommandMessage(new EventMessage(new InitialOrFinalPhaseEvent(2))));
            skipColumn.addElem(skipLeaderOpt);
            skipLeaderOpt.setFirstIdx(leaders.size() * 2);
        }

        if(Objects.isNull(requestBuilder))
            leadersList.selectInEnabledOption(cli,"Select a possible action");
        else {

            leadersList.selectInEnabledOption(cli, "Press ENTER to go back", () -> getClient().changeViewBuilder(requestBuilder));

        }

        return CanvasBody.centered(leadersList).toString();
    }

}
