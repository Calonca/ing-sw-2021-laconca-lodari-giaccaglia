package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SimpleProductions extends SimpleModelElement{


//          position          production
    private Map<Integer, SimpleProduction>availableProductions;

    public SimpleProductions(){}

    public SimpleProductions(Map<Integer, SimpleProduction> availableProductions){
        this.availableProductions = availableProductions;
    }

    @Override
    public void update(SimpleModelElement element) {

       SimpleProductions serverElement = (SimpleProductions) element;
       this.availableProductions = serverElement.availableProductions;

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
        //         resNum   quantity
        public Map<Integer, Integer> getInputResources() {
            return inputResources;
        }

        public int getResourceInputQuantity(int resourceNumber){

            if(ResourceAsset.fromInt(resourceNumber).equals(ResourceAsset.EMPTY))
                return 0;

            return inputResources.get(resourceNumber);
        }

        public Map<Integer, Integer> getOutputResources() {
            return outputResources;
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
