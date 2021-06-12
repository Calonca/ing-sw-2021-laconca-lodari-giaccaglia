package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrawableProduction {
    public static Drawable fromInputAndOutput(Map<ResourceAsset,Integer> in, Map<ResourceAsset,Integer> out){
        Drawable toReturn = new Drawable();

        Map<ResourceCLI,Pair<Integer, Integer>> inOutMap = Arrays.stream(ResourceCLI.values())
                .collect(Collectors.toMap(
                        resCLI->resCLI,
                        resCLI->new Pair<>(
                        in.getOrDefault(resCLI.getRes(),0),
                        out.getOrDefault(resCLI.getRes(),0))
                ));

        inOutMap.forEach((res, inOut) -> {
            Drawable dw = new Drawable();
            dw.add(new DrawableLine(0, res.ordinal(), "║ "));
            boolean hasInput = inOut.getKey() != 0;
            DrawableLine dl = new DrawableLine(2, res.ordinal(), hasInput ? res.getSymbol() : "  ", Color.BRIGHT_WHITE, hasInput ? res.getB() : Background.DEFAULT);
            dw.add(dl);
            dw.add(new DrawableLine(4, res.ordinal(), inOut.getKey() != 0 ? " x " + inOut.getKey() : "    "));
            dw.add(new DrawableLine(8, res.ordinal(), res.ordinal() == 2 ? "    -> " : "       "));
            boolean hasOutput = inOut.getValue() != 0;
            DrawableLine dlo = new DrawableLine(15, res.ordinal(), hasOutput ? res.getSymbol() : "  ", Color.BRIGHT_WHITE, hasOutput ? res.getB() : Background.DEFAULT);
            dw.add(dlo);
            dw.add(new DrawableLine(17, res.ordinal(), inOut.getValue() != 0 ? " x " + inOut.getValue() : "    "));
            dw.add(new DrawableLine(21, res.ordinal(), "    ║"));
            toReturn.add(dw);
        });

        return toReturn;
    }


}
