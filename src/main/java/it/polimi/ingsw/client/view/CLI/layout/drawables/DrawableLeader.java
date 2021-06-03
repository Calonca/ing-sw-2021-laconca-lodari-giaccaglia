package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
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
        String emptyLineWithBorders="|                  |";
        if (!isSelected) {
            dwl.addEmptyLine();
            dwl.addEmptyLine();
        }
        dwl.add(0," __________________ ");
        dwl.add(0,"|"+(ldCard.isPlayable()?"Playable    ":"Not Playable")+ StringUtil.spaces(6)+"|");
        dwl.add(0,"|requirements:     |");
        int heightUntilNow = 3+(isSelected?0:2);

        List<Drawable> reqLines = IntStream.range(0,4)
                .mapToObj(i->{
                    Drawable dw = new Drawable();
                    dw.add(new DrawableLine(0,heightUntilNow+i,emptyLineWithBorders));
                    return dw;
                })
                .collect(Collectors.toList());


        List<Pair<ResourceAsset,Integer>> resCost = ldCard.getRequirementsResources() != null ? ldCard.getRequirementsResources() : new ArrayList<>();
        for (Pair<ResourceAsset, Integer> d : resCost) {
            ResourceCLI res = ResourceCLI.fromAsset(d.getKey());
            Drawable dw = new Drawable();
            DrawableLine dl = new DrawableLine(6,heightUntilNow+res.ordinal(), d.getValue()+" "+res.getFullName(),res.getC(), Background.DEFAULT);
            dw.add(dl);
            reqLines.set(res.ordinal(),dw);
        }
        List<Pair<NetworkDevelopmentCardColor,Integer>> cardCosts = ldCard.getRequirementsCards() != null ? ldCard.getRequirementsCards() : new ArrayList<>();
        for (Pair<NetworkDevelopmentCardColor, Integer> d : cardCosts) {
            Drawable dw = new Drawable();
            DrawableDevCard.DevCardColorCLI res = DrawableDevCard.DevCardColorCLI.fromNetworkColor(d.getKey());
            dw.add(new DrawableLine(0,heightUntilNow+res.ordinal(), "| "));
            String cardNameLine = d.getValue()+" "+res.getNameWithSpaces();
            dw.add(new DrawableLine(2,heightUntilNow+res.ordinal(), cardNameLine,res.getC(), Background.DEFAULT));
            dw.add(new DrawableLine(10,heightUntilNow+res.ordinal(), "         |"));
            reqLines.set(res.ordinal(),dw);
        }


        for (Drawable e : reqLines) {
            dwl.add(e);
        }

        dwl.add(0,"|Victory points: "+ldCard.getVictoryPoints()+" |");


        castWithOptional(NetworkMarketLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e,isSelected));
        castWithOptional(NetworkProductionLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e,isSelected));
        castWithOptional(NetworkDepositLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e,isSelected));
        castWithOptional(NetworkDevelopmentDiscountLeaderCard.class,ldCard).ifPresent(e-> addAbility(dwl, e,isSelected));

        if (isSelected)
            return Drawable.selectedDrawableList(dwl);
        else
            return  dwl;
    }

    public static <T extends NetworkLeaderCard> Optional<T> castWithOptional(Class<T> s,NetworkLeaderCard cart){
        if (cart==null)
            return Optional.empty();
        if (cart.getClass().getSimpleName().equals(s.getSimpleName())) {
            T casted = (T) cart;
            return Optional.of(casted);
        }
        else return Optional.empty();

    }

    public static void addAbility(Drawable top, NetworkMarketLeaderCard ldCard, boolean isSelected){
        top.add(0,"|Ability: Transform|");
        top.add(0,"|   White=>        |");
        top.add(0,"|__________________|");
        if (isSelected) {
            top.addEmptyLine();
            top.addEmptyLine();
        }
    }

    public static void addAbility(Drawable top, NetworkProductionLeaderCard ldCard, boolean isSelected){
        top.add(0,"|Ability: Produce  |");
        top.add(0,"| Res1+R2=>F+H     |");
        top.add(0,"|__________________|");
        if (isSelected) {
            top.addEmptyLine();
            top.addEmptyLine();
        }
    }

    public static void addAbility(Drawable top, NetworkDepositLeaderCard ldCard, boolean isSelected){
        top.add(0,"|Ability: Deposit  |");
        top.add(0,"|Ability: Produce  |");
        top.add(0,"|__________________|");
        if (isSelected) {
            top.addEmptyLine();
            top.addEmptyLine();
        }
    }

    public static void addAbility(Drawable top, NetworkDevelopmentDiscountLeaderCard ldCard, boolean isSelected){
        top.add(0,"|Ability: Discount |");
        top.add(0,"|Ability: Produce  |");
        top.add(0,"|__________________|");
        if (isSelected) {
            top.addEmptyLine();
            top.addEmptyLine();
        }
    }


    public static int height(){
        return 7;
    }
}
