package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.simplemodel.SimpleProductions;
import it.polimi.ingsw.server.model.GameModel;
import javafx.util.Pair;

import java.util.Map;
import java.util.stream.Collectors;

public class SimpleProductionsMessageBuilder {

    public static Map<Integer, SimpleProductions.SimpleProduction> simpleProductionsMap(GameModel gameModel){

   //      position                     inputs                 outputs        isAvailable   isSelected
        Map<Integer, Pair<Pair<Map<Integer, Integer>, Map<Integer, Integer>>, Pair<Boolean, Boolean>>> productionsMap = gameModel.getCurrentPlayer().getPersonalBoard().getSimpleProductionsMap();

        return productionsMap.keySet().stream().collect(Collectors.toMap(
                index -> index, //production index

                index -> {

                    Pair<Map<Integer, Integer>, Map<Integer, Integer>> inputsAndOutPutsResourcesInts = productionsMap.get(index).getKey();
                    Pair<Boolean, Boolean> booleanPair = productionsMap.get(index).getValue();

                    Boolean isAvailable = booleanPair.getKey();
                    Boolean isSelected = booleanPair.getValue();

                    SimpleProductions.SimpleProduction simpleProduction = new SimpleProductions.SimpleProduction(inputsAndOutPutsResourcesInts.getKey(), inputsAndOutPutsResourcesInts.getValue(), isAvailable, isSelected);

                   return simpleProduction;
                }

        ));

    }

}
