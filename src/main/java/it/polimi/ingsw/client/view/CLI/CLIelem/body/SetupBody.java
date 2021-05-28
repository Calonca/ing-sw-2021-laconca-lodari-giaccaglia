package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.*;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        Canvas canvas = Canvas.withBorder(CLI.width,13);
        String s = "Resources to choose: ";
        int StartPositionX = StringUtil.startCenterWritingX(s,CLI.width)-ResourceCLI.width()*resToChoose/2;
        int posX =StartPositionX+s.length();
        for (ResourceAsset resAsset:chosenRes)
        {
            DrawableList dl = ResourceCLI.fromAsset(resAsset).toBigDrawableList(false);
            dl.shift(posX,0);
            canvas.draw(dl);
            posX+=dl.getWidth()+2;
        }
        for (int i =0;i<resToChoose;i++)
        {
            DrawableList dl = ResourceCLI.TO_CHOSE.toBigDrawableList(i==0);
            dl.shift(posX,0);
            canvas.draw(dl);
            posX+=dl.getWidth()+2;
        }


        String choosingResString="";
        if (!resAreChosen()) {
            HorizontalListBody resToChooseFrom = new HorizontalListBody(7);
            canvas.drawWithDefaultColor(StartPositionX,0,s);
            canvas.drawCenterXDefault(7,"^");
            canvas.drawCenterXDefault(8,"|");
            canvas.drawCenterXDefault(9,"|");
            canvas.drawCenterXDefault(10,"|"+"-".repeat(2+(ResourceCLI.width()+5)*4)+"|");


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

        }else if (!leadersAreChosen()){

        }

        return canvas+choosingResString;
    }

    private boolean resAreChosen() {
        return resToChoose==0;
    }

    private boolean leadersAreChosen(){
        return true;
        //return selected.stream().filter(e->e.equals(true)).count()==2;
    }

}
