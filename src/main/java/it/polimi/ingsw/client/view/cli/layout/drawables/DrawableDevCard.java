package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;

import java.util.List;

public class DrawableDevCard {

    private DrawableDevCard(){}

    public static Drawable fromDevCardAsset(NetworkDevelopmentCard card,int stackHeight){

        if (card==null) {
            //Less code would be used if the string was generated from code but by writing the entire string
            //gives an immediate representation of its look in the cli
            final String noCardText = """
                       ╔════════════════════════╗
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║        No Cards        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ║                        ║
                       ╚════════════════════════╝
                    """;
            Drawable drawable1 = new Drawable();
            drawable1.add(0, noCardText);
            return drawable1;
        }


        DevCardCLI cliCard = DevCardCLI.fromNetworkColor(card.getCardType());


        Drawable drawable= new Drawable();
        drawable.add(0,"╔═        ═══════════════╗");
        drawable.add(new DrawableLine(3,0,cliCard.getNameWithSpaces(),Color.BRIGHT_WHITE,cliCard.getB()));
        drawable.add(0,"║ Points : "+ StringUtil.untilReachingSize(card.getVictoryPoints(),2) + "            ║");
        drawable.add(0,"║────────────────────────║");
        drawable.add(0,"║ Level  : "+ StringUtil.untilReachingSize(card.getLevel(),2) + "            ║");
        drawable.add(0,"║════════ Costs ═════════║");

        int height = drawable.getHeight();

        List<Drawable> costPart = DrawableLeader.linesWithResRequirements(drawable,height,card.getCostList());
        costPart.forEach(drawable::add);
        drawable.add(0,"║══════ Production ══════║");


       Drawable production = DrawableProduction.fromInputAndOutput(card.getProductionInputResources(), card.getProductionOutputResources());
       drawable.add(Drawable.copyShifted(0,drawable.getHeight(),production));

       for(int i = drawable.getHeight(); i<15; i++)
           drawable.add(0,"║                        ║");

        drawable.add(0,"╚════════════════════════╝");
        drawable = Drawable.copyShifted(stackHeight-1,0,drawable);
        for (int i = 0;i<stackHeight-1;i++)
            drawable.add(new DrawableLine(i,0,"""
                    ╔
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ║
                    ╚
                    """));

        return drawable;
    }

}
