package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.textUtil.DrawableList;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkLeaderCard;

public class DrawableLeader {
    public static DrawableList fromAsset(LeaderCardAsset ld,boolean isSelected){
        NetworkLeaderCard ldCard = ld.getNetworkLeaderCard();
        DrawableList dwl = new DrawableList();
        if (!isSelected) {
            dwl.addEmptyLine();
            dwl.addEmptyLine();
        }
        dwl.add(0," ___________________");
        dwl.add(0,"|requirements: ");
        dwl.add(0,"|Victory points: "+ldCard.getVictoryPoints());
        dwl.add(0,"|Ability: ");
        dwl.add(0,"|___________________|");
        if (isSelected) {
            dwl.addEmptyLine();
            dwl.addEmptyLine();
        }
        return dwl;
    }

    public static int height(){
        return 7;
    }
}
