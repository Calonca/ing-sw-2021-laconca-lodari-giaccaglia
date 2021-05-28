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
        }

        return canvas+choosingResString;
    }

    private boolean resAreChosen() {
        return resToChoose==0;
    }

    private boolean leadersAreChosen(){
        return selected.stream().filter(e->e.equals(true)).count()==2;
    }

}
