package it.polimi.ingsw.network.simplemodel;

import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SelectablePositions extends SimpleModelElement{

    Map<Integer, MutablePair<Integer, Boolean>> selectablePositions;

    public SelectablePositions(){
        selectablePositions = new HashMap<>();
    }

    public SelectablePositions(Map<Integer, MutablePair<Integer, Boolean>> selectableWarehousePositions, Map<Integer, MutablePair<Integer, Boolean>> selectableStrongBoxPositions){

        selectablePositions = new HashMap<>();
        selectablePositions.putAll(selectableWarehousePositions);
        selectablePositions.putAll(selectableStrongBoxPositions);
    }

    @Override
    public void update(SimpleModelElement element) {
        SelectablePositions serverElement = (SelectablePositions) element;
        selectablePositions = serverElement.selectablePositions;
    }

    public List<Integer> getUpdatedSelectablePositions(List<Integer> chosenInputPos){
        updateAvailability(chosenInputPos);
        return getSelectablePositions();
    }

    private void updateAvailability(List<Integer> chosenInputPos){

        chosenInputPos.forEach(position -> {

            MutablePair<Integer, Boolean> pair = selectablePositions.get(position);

            pair.setLeft(pair.getLeft() - 1);

            if(pair.getLeft() == 0)
                pair.setRight(false);

        });
    }

    public List<Integer> getSelectablePositions(){

        List<Integer> positions = selectablePositions.keySet()
                .stream()
                .filter(position -> selectablePositions.get(position).getValue())
                .collect(Collectors.toList());

        return positions;
    }






}
