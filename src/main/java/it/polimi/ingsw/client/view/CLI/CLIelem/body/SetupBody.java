package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLeader;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetupBody extends CLIelem {
    List<LeaderCardAsset> leadersToChoose;
    List<MutablePair<Boolean, UUID>> selected;
    int resToChoose;
    final Client client;
    List<ResourceAsset> chosenRes;


    public SetupBody(List<LeaderCardAsset> leaders, int numOfResourcesToChoose, Client client) {
        leadersToChoose = leaders;
        resToChoose = numOfResourcesToChoose;
        selected = leadersToChoose.stream().map(leaderCardAsset -> new MutablePair<>(false, leaderCardAsset.getCardId()))
                .collect(Collectors.toList());
        chosenRes = new ArrayList<>();
        this.client = client;
    }

    @Override
    public String toString() {
        Column layout = new Column();
        List<ResourceAsset> outputs =
                Stream.concat(
                        chosenRes.stream(),
                        Stream.generate(()->ResourceAsset.TOCHOOSE).limit(resToChoose)
                        ).collect(Collectors.toList());
        ResChoiceRow resChoiceRow = new ResChoiceRow(chosenRes.size(),new ArrayList<>(), outputs);
        resChoiceRow.setOnChosenOutput(()->{
            chosenRes.add(ResourceAsset.fromInt(cli.getLastInt()));
            resToChoose-=1;
            cli.show();
        });
        resChoiceRow.addToGrid(layout);
        Row selectingLeaderRow = new Row();
        if (!resAreChosen()) {
            layout.selectAndRunOption(cli);
        }else {
            selectingLeaderRow = choosingLeadersRow();
            layout.addElem(selectingLeaderRow);
            selectingLeaderRow.setFirstIdx(0);
        }

        if (resAreChosen() && !leadersAreChosen()){
            selectingLeaderRow.selectAndRunOption(cli);
        }

        if (resAreChosen() && leadersAreChosen())
        {
            Thread animation = new Thread(()->{
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SetupPhaseEvent event = new SetupPhaseEvent(chosenRes.size(),2,client.getCommonData().getThisPlayerIndex());
                selected.stream().filter(p->p.left.equals(true)).forEach(p->event.addDiscardedLeader(p.right));
                if (chosenRes.size()>0)
                    event.addResource(new Pair<>(0,chosenRes.get(0).getResourceNumber()));
                if (chosenRes.size()>1)
                    event.addResource(new Pair<>(1,chosenRes.get(1).getResourceNumber()));

                client.getServerHandler().sendCommandMessage(new EventMessage(event));
            });

            animation.start();
        }

        return CanvasBody.fromGrid(layout).toString();
    }

    private Row choosingLeadersRow() {

        Row leadersToChooseFrom = new Row();
        for (int i=0;i<leadersToChoose.size();i++) {
            int finalI1 = i;
            Drawable leader = DrawableLeader.fromAsset(leadersToChoose.get(i).getNetworkLeaderCard(), false);
            Drawable selectedLeader = DrawableLeader.fromAsset(leadersToChoose.get(i).getNetworkLeaderCard(), true);
            Option o = Option.from(leader, selectedLeader, () -> {
                selected.get(finalI1).setLeft(!selected.get(finalI1).left);
                cli.show();
            });
            o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
            o.setSelected(selected.get(i).left);
            leadersToChooseFrom.addElem(o);
        }
        leadersToChooseFrom.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return leadersToChooseFrom;
    }

    private boolean resAreChosen() {
        return resToChoose==0;
    }

    private boolean leadersAreChosen(){
        return selected.stream().filter(e->e.left.equals(true)).count()==2;
    }

}
