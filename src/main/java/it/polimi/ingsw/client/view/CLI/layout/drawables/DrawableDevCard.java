package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DrawableDevCard {


    private enum DevCardColorCLI {

       GREEN(NetworkDevelopmentCardColor.GREEN, Color.WHITE),
       BLUE(NetworkDevelopmentCardColor.BLUE,Color.SHIELD),
       PURPLE(NetworkDevelopmentCardColor.PURPLE,Color.STONE),
       YELLOW(NetworkDevelopmentCardColor.YELLOW,Color.GOLD);

        private final NetworkDevelopmentCardColor res;
        private final Color c;

        DevCardColorCLI(NetworkDevelopmentCardColor res, Color c) {
            this.res = res;
            this.c = c;
        }

        public Color getC() {
            return c;
        }

        public static DevCardColorCLI fromNetworkColor(NetworkDevelopmentCardColor asset){
            int rNum = asset.ordinal();
            DevCardColorCLI[] val = DevCardColorCLI.values();
            return rNum>val.length ? DevCardColorCLI.GREEN: val[rNum];
        }

    }

    public static Drawable fromDevCardAsset(DevelopmentCardAsset devCardAsset){

        if (devCardAsset==null) {
            Drawable drawable1 = new Drawable();
            drawable1.add(0, "No card");
            return drawable1;
        }

        Color c;
        NetworkDevelopmentCard networkDevCard = devCardAsset.getDevelopmentCard();

        if (networkDevCard.isSelectable())
            c = DevCardColorCLI.fromNetworkColor(networkDevCard.getCardType()).getC();
        else c = Color.BLACK;
        Background back = Background.DEFAULT;


        Drawable drawable= new Drawable();
        drawable.add(0,"line1",c, back);
        drawable.add(0,"cost1:",c, back);
        drawable.add(0,"|cost2",c, back);
        drawable.add(0,"|cost3",c, back);
        drawable.add(0,"|cost4",c, back);
        drawable.add(0,"|level :"+networkDevCard.getLevel(),c, back);
        drawable.add(0,"|victory points :"+networkDevCard.getVictoryPoints(),c, back);
        drawable.add(0,"elevation1",c, back);
        drawable.add(0,"elevation2",c, back);
        drawable.add(0,"elevation3",c, back);
        List<Pair<ResourceAsset,Integer>> cardCosts = networkDevCard.getCostList() != null ? networkDevCard.getCostList() : new ArrayList<>();
        for (Pair<ResourceAsset, Integer> d : cardCosts) {
            ResourceCLI res = ResourceCLI.fromAsset(d.getKey());
            DrawableLine dl = new DrawableLine(6,1+res.ordinal(), d.getValue()+" "+res.getFullName(),res.getC(),back);
            drawable.add(dl);
        }

        return drawable;
    }

    public static int width(){
        return fromDevCardAsset(new DevelopmentCardAsset()).getWidth();
    }
    public static int height(){
        return fromDevCardAsset(new DevelopmentCardAsset()).getHeight();
    }
}
