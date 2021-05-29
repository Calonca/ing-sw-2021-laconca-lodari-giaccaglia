package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.CLIelem.DrawableLeader;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.*;
import it.polimi.ingsw.network.assets.CardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.messages.clienttoserver.events.EventMessage;
import it.polimi.ingsw.network.messages.clienttoserver.events.setupphaseevent.SetupPhaseEvent;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        Canvas canvas = Canvas.withBorder(CLI.width,11);
        String s = "Resources to choose: ";
        int CenterX = 1+CLI.getCenterX()-(ResourceCLI.width()-2)/2;
        int StartPositionX = CenterX-ResourceCLI.width()*chosenRes.size();
        int posX =StartPositionX;
        for (ResourceAsset resAsset:chosenRes)
        {
            DrawableList dl = ResourceCLI.fromAsset(resAsset).toBigDrawableList(false);
            dl.shift(posX,0);
            canvas.addDrawableList(dl);
            posX+=ResourceCLI.width();
        }
        for (int i =0;i<resToChoose;i++)
        {
            DrawableList dl = ResourceCLI.TO_CHOSE.toBigDrawableList(i==0);
            dl.shift(posX,0);
            canvas.addDrawableList(dl);
            posX+=ResourceCLI.width();
        }


        String choosingResString="";
        String leadersToChooseString="";
        if (!resAreChosen()) {
            HorizontalListBody resToChooseFrom = new HorizontalListBody(7);
            DrawableList dwl1 = new DrawableList();
            dwl1.add(StartPositionX-s.length(),s);
            canvas.addDrawableList(dwl1);

            DrawableList dwl = new DrawableList();
            dwl.addToCenter(CLI.width,"^");
            dwl.addToCenter(CLI.width,"|");
            dwl.addToCenter(CLI.width,"|");
            dwl.addToCenter(CLI.width,"|"+"-".repeat(1+(ResourceCLI.width()+6)*4)+"|");
            dwl.shift(0,7);
            canvas.addDrawableList(dwl);

            List<DrawableList> optionsDwList = ResourceCLI.toList();
            for (int i=0;i<optionsDwList.size();i++){
                int finalI = i;
                Option o = Option.from(optionsDwList.get(i),()->{
                    chosenRes.add(ResourceAsset.fromInt(finalI));
                    resToChoose-=1;
                    cli.refreshCLI();
                });
                resToChooseFrom.addOption(o);
            }
            choosingResString=resToChooseFrom.toString();
            resToChooseFrom.selectOption();
        }else if (!leadersAreChosen()){
            String s1= "Your chosen resources";
            DrawableList dwl = new DrawableList();
            dwl.addToCenter(CLI.width,s1);
            dwl.shift(4,ResourceCLI.height()-2);
            canvas.addDrawableList(dwl);
            //Add leaders list
            HorizontalListBody leadersToChooseFrom = new HorizontalListBody(9);
            for (int i=0;i<leadersToChoose.size();i++){
                int finalI1 = i;
                DrawableList leader = DrawableLeader.fromAsset(leadersToChoose.get(i),selected.get(i).getLeft());
                Option o = Option.from(leader,()->{
                    selected.get(finalI1).setLeft(!selected.get(finalI1).left);
                    cli.refreshCLI();
                });
                o.setSelectable(selected.get(i).left);
                o.setSelected(selected.get(i).left);
                leadersToChooseFrom.addOption(o);
                leadersToChooseFrom.selectOption();
                leadersToChooseString = leadersToChooseFrom.toString();
            }
        }else {
                    SetupPhaseEvent event = new SetupPhaseEvent(chosenRes.size(),2,client.getCommonData().getThisPlayerIndex());
                    selected.stream().filter(p->p.left.equals(true)).forEach(p->event.addDiscardedLeader(p.right));
                    if (chosenRes.size()>0)
                        event.addResource(new Pair<>(0,chosenRes.get(0).getResourceNumber()));
                    if (chosenRes.size()>1)
                        event.addResource(new Pair<>(1,chosenRes.get(1).getResourceNumber()));

                    client.getServerHandler().sendCommandMessage(new EventMessage(event));
        };

        return canvas+choosingResString+leadersToChooseString;
    }

    private boolean resAreChosen() {
        return resToChoose==0;
    }

    private boolean leadersAreChosen(){
        return selected.stream().filter(e->e.left.equals(true)).count()==2;
    }

}
