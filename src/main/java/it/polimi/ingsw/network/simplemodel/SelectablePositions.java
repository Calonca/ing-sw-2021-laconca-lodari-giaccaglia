package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class SelectablePositions extends SimpleModelElement{

    private Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> selectablePositions;

    private transient List<ResourceAsset> resourcesToChoose;

    //                position  numOfSelected
    private transient Map<Integer, Integer> selectedResourcesMap;

    //                position  numOfSelected
    private transient Map<Integer, Integer> resourcesOriginalAvailability;

    private transient int indexOfCurrentResourceToChoose = 0;

    public SelectablePositions(){

        selectablePositions = new HashMap<>();
        selectedResourcesMap = new HashMap<>();
        resourcesOriginalAvailability = new HashMap<>();
        resourcesToChoose = new ArrayList<>();

    }

    public SelectablePositions(Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> selectableWarehousePositions, Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> selectableStrongBoxPositions){

        selectablePositions = new HashMap<>();
        selectablePositions.putAll(selectableWarehousePositions);
        selectablePositions.putAll(selectableStrongBoxPositions);
        resourcesToChoose = new ArrayList<>();
        resourcesOriginalAvailability = new HashMap<>();
        selectedResourcesMap = new HashMap<>();

    }

    @Override
    public void update(SimpleModelElement element) {

        SelectablePositions serverElement = (SelectablePositions) element;
        selectablePositions = serverElement.selectablePositions;
        buildResourcesOriginalAvailability();
        String currentState = getThisPlayerCache().getCurrentState();
        if(currentState != null) {
            SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
            if(simpleCardShop.getPurchasedCard().isPresent()) {
                buildResourcesToChooseForCard();
            }
            else
                buildResourcesToChooseForProduction();
        }

    }

    //       position  numOfSelectable
    public Map<Integer,Integer> getUpdatedSelectablePositions(List<Integer> chosenInputPos){

        if(!chosenInputPos.isEmpty())
            updateAvailability(chosenInputPos);

        return getSelectablePositions();

    }

    private void updateAvailability(List<Integer> chosenInputPos){

        buildSelectedResourcesMap(chosenInputPos);

        resourcesOriginalAvailability.keySet().forEach(
                position -> {

                    int updatedAvailability;

                    if(selectedResourcesMap.containsKey(position))
                        updatedAvailability = selectedResourcesMap.get(position);

                    else
                        updatedAvailability = resourcesOriginalAvailability.get(position);

                    Pair<Integer, ResourceAsset> mapKey = getKeyFromPos(position);
                    MutablePair<Integer, Boolean> mapValue = selectablePositions.get(mapKey);
                    mapValue.setLeft(updatedAvailability);
                    if(mapValue.getLeft() == 0)
                        mapValue.setRight(false);

                }
        );

    }

    private Pair<Integer, ResourceAsset> getKeyFromPos(int pos){

        Pair<Integer, ResourceAsset> mapKey =  selectablePositions
                .keySet()
                .stream()
                .filter(pair -> (pair.getKey().equals(pos)))
                .findFirst()
                .get();

        return mapKey;
    }

    private Map<Integer, Integer> getSelectablePositions(){

        ResourceAsset nextResourceToChoose = resourcesToChoose.get(indexOfCurrentResourceToChoose);
        Map<Integer, Integer> selectablePositionsMap = selectablePositions.keySet()
                .stream()
                .filter(pair -> pair.getValue().equals(nextResourceToChoose) || nextResourceToChoose.equals(ResourceAsset.TO_CHOOSE))
                .filter(pair -> selectablePositions.get(pair).getValue())
                .collect(Collectors.toMap
                        (
                                Pair::getKey,
                                pair -> selectablePositions.get(pair).getLeft()
                        ));

        indexOfCurrentResourceToChoose = resourcesToChoose.size()> indexOfCurrentResourceToChoose ? indexOfCurrentResourceToChoose++ : indexOfCurrentResourceToChoose;
        return selectablePositionsMap;

    }

    private void buildResourcesToChooseForCard(){

        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        NetworkDevelopmentCard purchasedCard = simpleCardShop.getPurchasedCard().get().getDevelopmentCard();
        resourcesToChoose = purchasedCard.getCosts();

    }

    private void  buildResourcesToChooseForProduction(){

        SimpleCardCells simpleCardCells = getThisPlayerCache().getElem(SimpleCardCells.class).orElseThrow();
        int lastSelectedProduction = simpleCardCells.getSimpleProductions().getLastSelectedProductionPosition();

        Map<ResourceAsset, Integer> inputRes =  simpleCardCells.getProductionAtPos(lastSelectedProduction).map(SimpleProductions.SimpleProduction::getInputResources).orElse(new HashMap<>());

        resourcesToChoose = inputRes.entrySet()
                .stream()
                .flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                .collect(Collectors.toList());
    }



    private void buildResourcesOriginalAvailability(){

        resourcesOriginalAvailability = selectablePositions
                .keySet()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toMap(
                position -> position,
                position -> selectablePositions.get(getKeyFromPos(position)).getKey()
        ));

    }

    private void buildSelectedResourcesMap(List<Integer> chosenInputPositions){

        selectedResourcesMap = chosenInputPositions

                .stream()
                .collect(Collectors.toMap(

                        position -> position,

                        position -> chosenInputPositions
                                .stream()
                                .filter(positionToFind -> positionToFind.equals(position))
                                .mapToInt(positionToFind -> 1).sum(),

                        (position1, position2) -> position1

        ));

    }


}
