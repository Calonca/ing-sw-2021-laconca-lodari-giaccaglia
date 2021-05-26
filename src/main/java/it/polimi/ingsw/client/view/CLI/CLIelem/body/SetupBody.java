package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetupBody extends CLIelem {
    List<LeaderCardAsset> leadersToChoose;
    List<Boolean> selected;
    int resToChoose;
    List<ResourceAsset> chosenRes;


    public SetupBody(List<LeaderCardAsset> leaders,int numOfResourcesToChoose) {
        leadersToChoose = leaders;
        resToChoose = numOfResourcesToChoose;
        selected = Stream.generate(()->false).limit(leaders.size()).collect(Collectors.toList());
        chosenRes = new ArrayList<>();
        //chosenRes.add(ResourceAsset.STONE);
        //chosenRes.add(ResourceAsset.GOLD);
    }

    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,cli.getMaxBodyHeight());
        String repeated = "?".repeat(resToChoose);
        String s = "Resources to choose: ";
        int StartPositionX = StringUtil.startCenterWritingX(s+repeated,CLI.width);
        canvas.drawWithDefaultColor(StartPositionX,0,s);
        canvas.draw(StartPositionX+s.length(),0,repeated, Color.ANSI_BLACK, Background.ANSI_WHITE_BACKGROUND);
        canvas.drawWithDefaultColor(StartPositionX,1,"   Chosen resources: ");

        String chosen = chosenRes.stream().map(Enum::name).reduce("",(a, b)->a+b+",");
        if (chosen.length()>0)
            canvas.drawWithDefaultColor(StartPositionX+s.length(),1,chosen.substring(0,chosen.length()-1));

        if (leadersAreChosen()) {
            //
        }else if (resAreChosen()){

        }

        return canvas.toString();
    }

    private boolean resAreChosen() {
        return resToChoose==0;
    }

    private boolean leadersAreChosen(){
        return true;
        //return selected.stream().filter(e->e.equals(true)).count()==2;
    }

}
