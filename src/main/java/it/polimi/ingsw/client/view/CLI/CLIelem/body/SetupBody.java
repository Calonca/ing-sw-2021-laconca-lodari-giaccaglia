package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.layout.*;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLeader;
import it.polimi.ingsw.client.view.CLI.layout.drawables.ResourceCLI;
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

        layout.addElem(selectedResourcesRow());

        if (!resAreChosen()) {
            layout.addElem(arrow());
            Row choosingResourceRow = choosingResourceRow();
            layout.addElem(choosingResourceRow);
            choosingResourceRow.setFirstIdx(0);
            choosingResourceRow.selectAndRunOption(cli);
        }else if (!leadersAreChosen()){
            Row selectingLeaderRow = choosingLeadersRow();
            layout.addElem(selectingLeaderRow);
            selectingLeaderRow.setFirstIdx(0);
            selectingLeaderRow.selectAndRunOption(cli);
        }else {
             SetupPhaseEvent event = new SetupPhaseEvent(chosenRes.size(),2,client.getCommonData().getThisPlayerIndex());
             selected.stream().filter(p->p.left.equals(true)).forEach(p->event.addDiscardedLeader(p.right));
             if (chosenRes.size()>0)
                 event.addResource(new Pair<>(0,chosenRes.get(0).getResourceNumber()));
             if (chosenRes.size()>1)
                 event.addResource(new Pair<>(1,chosenRes.get(1).getResourceNumber()));

             client.getServerHandler().sendCommandMessage(new EventMessage(event));
        };
        return CanvasBody.fromGrid(layout).toString();
    }

    private Row selectedResourcesRow(){
        String resToChooseString = "Resources to choose: ";
        String chosenResString= "Your chosen resources";
        int CenterX = 1+CLI.getCenterX()-(ResourceCLI.width())/2;
        int startPositionX = CenterX-resToChooseString.length()-(ResourceCLI.width()+1)*chosenRes.size();

        Row row = new Row();
        row.addElem(new SizedBox(startPositionX,0));

        if (!resAreChosen())
            row.addElem(Option.noNumber(resToChooseString));
        else
            row.addElem(new SizedBox(resToChooseString.length(),0));

        for (ResourceAsset resAsset:chosenRes)
        {
            Drawable dl = ResourceCLI.fromAsset(resAsset).toBigDrawableList(false);
            row.addElem(Option.noNumber(dl));
            row.addElem(new SizedBox(1,0));
        }
        for (int i =0;i<resToChoose;i++)
        {
            Drawable dl = ResourceCLI.TO_CHOSE.toBigDrawableList(i==0);
            row.addElem(Option.noNumber(dl));
            row.addElem(new SizedBox(1,0));
        }
        if (resAreChosen())
            row.addElem(Option.noNumber(chosenResString));

        return row;
    }

    private Row choosingResourceRow(){
        Row resToChooseFrom = new Row();

        List<Drawable> optionsDwList = ResourceCLI.toList();
        for (int i=0;i<optionsDwList.size();i++){
            int finalI = i;
            Option o = Option.from(optionsDwList.get(i),()->{
                chosenRes.add(ResourceAsset.fromInt(finalI));
                resToChoose-=1;
                cli.show();
            });
            o.setMode(Option.VisMode.NUMBER_TO_BOTTOM);
            resToChooseFrom.addElem(o);
        }

        resToChooseFrom.setAlignment(GridElem.Alignment.CANVAS_CENTER_VERTICAL);
        return resToChooseFrom;
    }

    private Row choosingLeadersRow() {

        Row leadersToChooseFrom = new Row();
        for (int i=0;i<leadersToChoose.size();i++) {
            int finalI1 = i;
            Drawable leader = DrawableLeader.fromAsset(leadersToChoose.get(i), false);
            Drawable selectedLeader = DrawableLeader.fromAsset(leadersToChoose.get(i), true);
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

    private Option arrow(){
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.width,"^");
        dwl.addToCenter(CLI.width,"|");
        dwl.addToCenter(CLI.width,"|");
        dwl.addToCenter(CLI.width,"|"+"-".repeat(1+(ResourceCLI.width()+6)*4)+"|");
        return Option.noNumber(dwl);
    }

    private boolean resAreChosen() {
        return resToChoose==0;
    }

    private boolean leadersAreChosen(){
        return selected.stream().filter(e->e.left.equals(true)).count()==2;
    }

}
