package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class SimpleProductions extends SimpleModelElement{


//          position          production       can activate
    private Map<Integer, Pair<SimpleProduction, Boolean>> availableProductions;

    public SimpleProductions(){}

    public SimpleProductions(Map<Integer, Pair<SimpleProduction, Boolean>> availableProductions){
        this.availableProductions = availableProductions;
    }

    @Override
    public void update(SimpleModelElement element) {

       SimpleProductions serverElement = (SimpleProductions) element;
       this.availableProductions = serverElement.availableProductions;

    }


    public static class SimpleProduction{

        private final Map<Integer, Integer> inputResources;
        private final Map<Integer, Integer> outputResources;

        public SimpleProduction(Map<Integer, Integer> inputResources, Map<Integer, Integer> outputResources){

            this.inputResources = inputResources;
            this.outputResources = outputResources;

        }

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
    }

}
