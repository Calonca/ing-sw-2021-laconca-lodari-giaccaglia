package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

        inOutMap = inOutMap.entrySet().stream().filter(entry -> {

            int i = entry.getValue().getKey();
            int o = entry.getValue().getValue();
            return !(i == 0 && o == 0);
        }).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));

        inOutMap.remove(ResourceCLI.EMPTY);

        AtomicInteger index = new AtomicInteger();
        inOutMap.forEach((res, inOut) -> {
            Drawable dw = new Drawable();
            dw.add(new DrawableLine(0, index.get(), "║ "));
            boolean hasInput = inOut.getKey() != 0;
            DrawableLine dl = new DrawableLine(2, index.get(), hasInput ? res.getSymbol() : "  ", Color.BRIGHT_WHITE, hasInput ? res.getB() : Background.DEFAULT);
            dw.add(dl);
            dw.add(new DrawableLine(4, index.get(), inOut.getKey() != 0 ? " x " + inOut.getKey() : "    "));
            dw.add(new DrawableLine(8, index.get(), index.get() == 0 ? "    -> " : "       "));
            boolean hasOutput = inOut.getValue() != 0;
            DrawableLine dlo = new DrawableLine(15, index.get(), hasOutput ? res.getSymbol() : "  ", Color.BRIGHT_WHITE, hasOutput ? res.getB() : Background.DEFAULT);
            dw.add(dlo);
            dw.add(new DrawableLine(17, index.get(), inOut.getValue() != 0 ? " x " + inOut.getValue() : "    "));
            dw.add(new DrawableLine(21, index.get(), "    ║"));
            toReturn.add(dw);
            index.set(index.get() + 1);
        });

        return toReturn;
    }


}
