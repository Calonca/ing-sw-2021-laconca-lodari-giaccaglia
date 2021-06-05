package it.polimi.ingsw.network.simplemodel;

import java.util.List;
import java.util.Map;

public class SimpleAvailableMovingPositions extends SimpleModelElement{

    private Map<Integer, List<Integer>> availableMovingPositions;

    public SimpleAvailableMovingPositions(){}

    public SimpleAvailableMovingPositions(Map<Integer, List<Integer>> availableMovingPositions){
        this.availableMovingPositions = availableMovingPositions;
    }

    @Override
    public void update(SimpleModelElement element) {

        availableMovingPositions = ((SimpleAvailableMovingPositions) element).availableMovingPositions;
    }

    public List<Integer> getAvailableMovingPositionsForResourceAt(int position){

        return availableMovingPositions.get(position);

    }
}
