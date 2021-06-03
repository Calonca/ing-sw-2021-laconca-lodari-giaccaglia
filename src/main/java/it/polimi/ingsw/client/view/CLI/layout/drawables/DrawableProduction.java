package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawableProduction {
    public static Drawable fromInputAndOutput(List<Integer> in, List<Integer> out){
        Drawable toReturn = new Drawable();

        List<Pair<Integer, Integer>> inOut = IntStream.range(0,7)
                .mapToObj(i->new Pair<>(i>=in.size()?0:in.get(i),i>=out.size()?0:out.get(i)))
                .collect(Collectors.toList());
        inOut.remove(5);

        for (int i = 0, inOutSize = inOut.size(); i < inOutSize; i++) {
            ResourceCLI res = ResourceCLI.fromInt(i);
            Drawable dw = new Drawable();
            dw.add(new DrawableLine(0, res.ordinal(), "║ "));
            boolean hasInput = inOut.get(i).getKey()!=0;
            DrawableLine dl = new DrawableLine(2, res.ordinal(),hasInput?res.getSymbol():"  ", Color.BRIGHT_WHITE,hasInput? res.getB(): Background.DEFAULT);
            dw.add(dl);
            dw.add(new DrawableLine(4, res.ordinal(), inOut.get(i).getKey()!=0?" x " + inOut.get(i).getKey():"    "));
            dw.add(new DrawableLine(8, res.ordinal(), i==2?"    -> ":"       "));
            boolean hasOutput = inOut.get(i).getValue()!=0;
            DrawableLine dlo = new DrawableLine(15, res.ordinal(),hasOutput?res.getSymbol():"  ", Color.BRIGHT_WHITE,hasOutput? res.getB(): Background.DEFAULT);
            dw.add(dlo);
            dw.add(new DrawableLine(17, res.ordinal(), inOut.get(i).getValue()!=0?" x " + inOut.get(i).getValue():"    "));
            dw.add(new DrawableLine(21, res.ordinal(), "    ║"));
            toReturn.add(dw);
        }

        return toReturn;
    }


}
