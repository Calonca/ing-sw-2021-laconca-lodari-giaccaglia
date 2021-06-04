package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;

import java.util.List;

public class DrawableDevCard {


    public static Drawable fromDevCardAsset(NetworkDevelopmentCard card){

        if (card==null) {
            Drawable drawable1 = new Drawable();
            drawable1.add(0, "No card");
            return drawable1;
        }

        Color c;

        c = DevCardCLI.fromNetworkColor(card.getCardType()).getC();;
        Background back = Background.DEFAULT;


        Drawable drawable= new Drawable();
        drawable.add(0,"╔════════════════════════╗");
        drawable.add(0,"║ Points: "+ StringUtil.untilReachingSize(card.getVictoryPoints(),2)+"             ║");
        drawable.add(0,"║═ Costs ════════════════║");

        int height = drawable.getHeight();

        List<Drawable> costPart = DrawableLeader.linesWithResRequirements(drawable,height,card.getCostList());

        costPart.forEach(drawable::add);
        drawable.add(0,"║═ Production ═══════════║");


        Drawable production = DrawableProduction.fromInputAndOutput(card.getInputResources(),card.getOutputResources());
        drawable.add(Drawable.copyShifted(0,drawable.getHeight(),production));

        drawable.add(0,"╚════════════════════════╝");
        //drawable.add(0,"elevation1",c, back);
        //drawable.add(0,"elevation2",c, back);
        //drawable.add(0,"elevation3",c, back);

        return drawable;
    }

    public static int width(){
        return fromDevCardAsset(new NetworkDevelopmentCard()).getWidth();
    }
    public static int height(){
        return fromDevCardAsset(new NetworkDevelopmentCard()).getHeight();
    }
}
