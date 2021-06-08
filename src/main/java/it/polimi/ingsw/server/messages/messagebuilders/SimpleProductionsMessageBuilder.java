package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import it.polimi.ingsw.server.model.GameModel;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleProductionsMessageBuilder {

    public static Map<Integer, Pair<SimpleProductions.SimpleProduction, Boolean>> simpleProductionsMap(GameModel gameModel) {

        Map<Integer, Pair<Pair<Map<Integer, Integer>, Map<Integer, Integer>>, Boolean>> productionsMap = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleProductionsMap();


        return productionsMap.keySet().stream().collect(Collectors.toMap(
                index -> index, //production index

                index -> {

                    Pair<Map<Integer, Integer>, Map<Integer, Integer>> inputsAndOutPutsResourcesInts = productionsMap.get(index).getKey();
                    boolean isAvailable = productionsMap.get(index).getValue();

                    Map<Integer, Integer> inputResources = inputsAndOutPutsResourcesInts.getKey();
                    Map<Integer, Integer> outputsResources = inputsAndOutPutsResourcesInts.getValue();


                    SimpleProductions.SimpleProduction simpleProduction = new SimpleProductions.SimpleProduction(inputResources, outputsResources);

                    return new Pair<>(simpleProduction, isAvailable);
                }

        ));

    }
}
