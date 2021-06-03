package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;
import it.polimi.ingsw.network.assets.leaders.*;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawableLeader {
    public static Drawable fromAsset(NetworkLeaderCard ldCard, boolean isSelected){
        Drawable dwl = new Drawable();

        if (!isSelected) {
            dwl.addEmptyLine();
            dwl.addEmptyLine();
        }

        dwl.add(0,"╔════════════════════════╗");
        dwl.add(0,"║ Points: "+ldCard.getVictoryPoints()+"              ║");
        dwl.add(0,"║═ requirements ═════════║");
        int heightUntilNow = 3+(isSelected?0:2);

        List<Drawable> reqLines = IntStream.range(0,4)
                .mapToObj(i->{
                    Drawable dw = new Drawable();
                    dw.add(new DrawableLine(0, heightUntilNow +i,StringUtil.emptyLineWithBorder(dwl.getWidth())));
                    return dw;
                })
                .collect(Collectors.toList());

        List<Pair<ResourceAsset,Integer>> resCost = ldCard.getRequirementsResources() != null ? ldCard.getRequirementsResources() : new ArrayList<>();
        for (Pair<ResourceAsset, Integer> d : resCost) {
            ResourceCLI res = ResourceCLI.fromAsset(d.getKey());
            Drawable dw = new Drawable();
            dw.add(new DrawableLine(0,heightUntilNow+res.ordinal(), "║ "));
            DrawableLine dl = new DrawableLine(2,heightUntilNow+res.ordinal(), res.getSymbol(), Color.BRIGHT_WHITE,res.getB());
            dw.add(new DrawableLine(4,heightUntilNow+res.ordinal(), " x "+d.getValue()));
            dw.add(new DrawableLine(8,heightUntilNow+res.ordinal(), "                 ║"));
            dw.add(dl);
            reqLines.set(res.ordinal(),dw);
        }
        List<Pair<NetworkDevelopmentCardColor,Integer>> cardCosts = ldCard.getRequirementsCards() != null ? ldCard.getRequirementsCards() : new ArrayList<>();
        for (Pair<NetworkDevelopmentCardColor, Integer> d : cardCosts) {
            Drawable dw = new Drawable();
            DevCardCLI CLICard = DevCardCLI.fromNetworkColor(d.getKey());
            dw.add(new DrawableLine(0,heightUntilNow+CLICard.ordinal(), "║ "));
            String cardNameLine = CLICard.getNameWithSpaces();
            dw.add(new DrawableLine(2,heightUntilNow+CLICard.ordinal(), cardNameLine,Color.BRIGHT_WHITE, CLICard.getB()));
            dw.add(new DrawableLine(8,heightUntilNow+CLICard.ordinal(), " x "+d.getValue()));
            dw.add(new DrawableLine(12,heightUntilNow+CLICard.ordinal(), "             ║"));
            reqLines.set(CLICard.ordinal(),dw);
        }


        for (Drawable e : reqLines) {
            dwl.add(e);
        }

        dwl.add(0,"║═ Ability ══════════════║");

        castWithOptional(NetworkMarketLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e));
        castWithOptional(NetworkProductionLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e));
        castWithOptional(NetworkDepositLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e));
        castWithOptional(NetworkDevelopmentDiscountLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e));


        dwl.add(0,"╚════════════════════════╝");
        if (isSelected) {
            dwl.addEmptyLine();
            dwl.addEmptyLine();
        }

        if (isSelected)
            return Drawable.selectedDrawableList(dwl);
        else
            return dwl;
    }

    public static <T extends NetworkLeaderCard> Optional<T> castWithOptional(Class<? extends T> s,NetworkLeaderCard cart){
        Optional<T> result;
        try {
           result = Optional.ofNullable(s.cast(cart));
        } catch (ClassCastException e){
            result = Optional.empty();
        }
        return result;

    }

    public static void addAbility(Drawable top, NetworkMarketLeaderCard ldCard){
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
        int height = top.getHeight();
        top.add(Drawable.copyShifted(1,height,MarbleCLI.WHITE.toSmallDrawable()));
        top.add(new DrawableLine(0,height,"║"));
        top.add(new DrawableLine(12,height,"             ║"));
        top.add(new DrawableLine(12,height+2,"             ║"));

        top.add(new DrawableLine(0,height+1,"║"));
        top.add(new DrawableLine(0,height+2,"║"));
        top.add(new DrawableLine(12,height+1,"-> "));
        ResourceCLI res = ResourceCLI.fromAsset(ldCard.getMarketBonusNetworkResource());
        String fullName = res.getFullName();
        int spacesLength = top.getWidth()-MarbleCLI.WHITE.toSmallDrawable().getWidth()-fullName.length()-4;
        top.add(new DrawableLine(top.getWidth()-spacesLength,height+1,StringUtil.spaces(spacesLength-1)+"║"));
        top.add(new DrawableLine(15,height+1,fullName,Color.BRIGHT_WHITE,res.getB()));
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
    }

    public static void addAbility(Drawable top, NetworkProductionLeaderCard ldCard){
        Drawable production = DrawableProduction.fromInputAndOutput(ldCard.getProductionInputResources(),ldCard.getProductionOutputResources());
        top.add(Drawable.copyShifted(0,top.getHeight(),production));
    }

    public static void addAbility(Drawable top, NetworkDepositLeaderCard ldCard){
        Color c = ResourceCLI.fromAsset(ldCard.getResourcesTypeInDepot()).getC();
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
        int height = top.getHeight();
        top.add(0,"║ ");
        top.add(0,"║ ");
        top.add(0,"║ ");
        top.add(0,"║ ");
        top.add(new DrawableLine(2,0+height," ╔═══════╗  ╔═══════╗ ",c, Background.DEFAULT));
        top.add(new DrawableLine(2,1+height," ║       ║══║       ║ ",c,Background.DEFAULT));
        top.add(new DrawableLine(2,2+height," ║       ║══║       ║ ",c,Background.DEFAULT));
        top.add(new DrawableLine(2,3+height," ╚═══════╝  ╚═══════╝ ",c,Background.DEFAULT));
        top.add(new DrawableLine(top.getWidth()-2,0+height," ║"));
        top.add(new DrawableLine(top.getWidth()-2,1+height," ║"));
        top.add(new DrawableLine(top.getWidth()-2,2+height," ║"));
        top.add(new DrawableLine(top.getWidth()-2,3+height," ║"));
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));

    }

    public static void addAbility(Drawable top, NetworkDevelopmentDiscountLeaderCard ldCard){
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
        top.add(0,"║ Market Discount        ║");
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
        int height = top.getHeight();
        top.add(0,"║   ");
        ResourceCLI res = ResourceCLI.fromAsset(ldCard.getResourcesDiscount().getKey());
        String toDisplay = res.getFullName()+" -"+ldCard.getResourcesDiscount().getValue();
        top.add(new DrawableLine(4,height,toDisplay,Color.BRIGHT_WHITE, res.getB()));
        int start = toDisplay.length()+4;
        top.add(new DrawableLine(start,height,StringUtil.spaces(top.getWidth()-start-1)+"║"));
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
        top.add(0,StringUtil.emptyLineWithBorder(top.getWidth()));
    }


    public static int height(){
        return 7;
    }
}
