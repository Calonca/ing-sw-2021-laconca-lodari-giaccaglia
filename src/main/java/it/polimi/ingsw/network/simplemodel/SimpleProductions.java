package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SimpleProductions extends SimpleModelElement{


//          position          production
    private Map<Integer, SimpleProduction> availableProductions;

    private int lastSelectedProductionPosition;

    public SimpleProductions(){}

    public SimpleProductions(Map<Integer, SimpleProduction> availableProductions, int lastSelectedProductionPosition){
        this.availableProductions = availableProductions;
        this.lastSelectedProductionPosition = lastSelectedProductionPosition;
    }

    @Override
    public void update(SimpleModelElement element) {

       SimpleProductions serverElement = (SimpleProductions) element;
       this.availableProductions = serverElement.availableProductions;
       this.lastSelectedProductionPosition = serverElement.lastSelectedProductionPosition;

    }

    public Optional<Boolean> isProductionAtPositionAvailable(int productionPosition){

        SimpleProduction productionPairAtPos = availableProductions.get(productionPosition);

        if(productionPairAtPos==null)
            return Optional.empty();
        else return Optional.of(productionPairAtPos.isAvailable());
    }

    public Optional<Boolean> isProductionAtPositionSelected(int productionPosition){

        SimpleProduction productionPairAtPos = availableProductions.get(productionPosition);

        if(productionPairAtPos==null)
            return Optional.empty();

        else return Optional.of(productionPairAtPos.isAvailable());
    }

    public Optional<SimpleProduction> getProductionAtPos(int productionPosition){

        SimpleProduction productionAtPos = availableProductions.get(productionPosition);

        if(productionAtPos == null)
            return Optional.empty();

        else return Optional.of(productionAtPos);
    }

    public int getLastSelectedProductionPosition(){
        return lastSelectedProductionPosition;
    }

    public static class SimpleProduction{

        private final Map<Integer, Integer> inputResources;
        private final Map<Integer, Integer> outputResources;
        private final boolean isAvailable;
        private final boolean isSelected;

        public SimpleProduction(Map<Integer, Integer> inputResources, Map<Integer, Integer> outputResources, boolean isAvailable, boolean isSelected){

            this.inputResources = inputResources;
            this.outputResources = outputResources;
            this.isAvailable = isAvailable;
            this.isSelected = isSelected;

        }

        //         resAsset   quantity
        public Map<ResourceAsset, Integer> getInputResources() {

            return inputResources.entrySet().stream().collect(Collectors.toMap(
                    entry -> ResourceAsset.fromInt(entry.getKey()),
                    Map.Entry::getValue
            ));

        }

        public int getResourceInputQuantity(int resourceNumber){

            if(ResourceAsset.fromInt(resourceNumber).equals(ResourceAsset.EMPTY))
                return 0;

            return inputResources.get(resourceNumber);
        }

        public Map<ResourceAsset, Integer> getOutputResources() {

            return outputResources.entrySet().stream().collect(Collectors.toMap(
                    entry -> ResourceAsset.fromInt(entry.getKey()),
                    Map.Entry::getValue
            ));
        }

        public int getResourceOutputQuantity(int resourceNumber){

            if(ResourceAsset.fromInt(resourceNumber).equals(ResourceAsset.EMPTY))
                return 0;

            return outputResources.get(resourceNumber);
        }

        public boolean isAvailable(){
            return isAvailable;
        }

        public boolean isSelected(){
            return isSelected;
        }

        public boolean choicesCanBeMadeOnInput(){

            return inputResources.keySet()
                    .stream()
                    .anyMatch(resourceInt -> resourceInt.equals(ResourceAsset.TOCHOOSE.getResourceNumber()));
        }

        public boolean choicesCanBeMadeOnOutput(){

            return outputResources.keySet()
                    .stream()
                    .anyMatch(resourceInt -> resourceInt.equals(ResourceAsset.TOCHOOSE.getResourceNumber()));

        }

    }

}
