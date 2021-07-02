package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleProductions extends SimpleModelElement{

//              position      production
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

    public int getIndexOfLastProduction(){
        return availableProductions.keySet().stream().mapToInt(i -> i).max().getAsInt();
    }

    public boolean isAnyProductionAvailable(){
        return availableProductions.values().stream().anyMatch(value -> value.isAvailable);
    }

    public Optional<SimpleProduction> lastSelectedProduction(){
        return Optional.ofNullable(availableProductions.get(getLastSelectedProductionPosition()));
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

        if(availableProductions == null)
            return Optional.empty();
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

        public List<ResourceAsset> getUnrolledInputs(){
            return getInputResources()
                    .entrySet().stream()
                    .flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                    .collect(Collectors.toList());
        }

        public List<ResourceAsset> getUnrolledOutputs(){
            return getOutputResources()
                    .entrySet().stream()
                    .flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                    .collect(Collectors.toList());}

        public Map<ResourceAsset, Integer> getOutputResources() {

            return outputResources.entrySet().stream().collect(Collectors.toMap(
                    entry -> ResourceAsset.fromInt(entry.getKey()),
                    Map.Entry::getValue
            ));
        }

        public boolean isAvailable(){
            return isAvailable;
        }

        public boolean isSelected(){
            return isSelected;
        }
    }

}
