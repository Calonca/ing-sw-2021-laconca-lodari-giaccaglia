package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.client.simplemodel.State;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCard;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getSimpleModel;
import static it.polimi.ingsw.client.view.abstractview.ViewBuilder.getThisPlayerCache;

public class SelectablePositions extends SimpleModelElement{

    private Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> selPositions;

    private transient List<ResourceAsset> resourcesToChoose;

    //                position  numOfSelected
    private transient Map<Integer, Integer> selectedResourcesMap;

    //                position  numOfSelected
    private transient Map<Integer, Integer> resourcesOriginalAvailability;

    private transient int indexOfCurrentResourceToChoose;

    public SelectablePositions(){

        selPositions = new HashMap<>();
        selectedResourcesMap = new HashMap<>();
        resourcesOriginalAvailability = new HashMap<>();
        resourcesToChoose = new ArrayList<>();

    }

    public SelectablePositions(Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> selectableWarehousePositions, Map<Pair<Integer, ResourceAsset>, MutablePair<Integer, Boolean>> selectableStrongBoxPositions){

        selPositions = new HashMap<>();
        selPositions.putAll(selectableWarehousePositions);
        selPositions.putAll(selectableStrongBoxPositions);
        resourcesToChoose = new ArrayList<>();
        resourcesOriginalAvailability = new HashMap<>();
        selectedResourcesMap = new HashMap<>();

    }

    @Override
    public void update(SimpleModelElement element) {

        resourcesToChoose = new ArrayList<>();
        resourcesOriginalAvailability = new HashMap<>();

        SelectablePositions serverElement = (SelectablePositions) element;
        selPositions = serverElement.selPositions;
        buildResourcesOriginalAvailability();
        resourcesToChoose = null;

    }

    //       position  numOfSelectable
    public Map<Integer,Integer> getUpdatedSelectablePositions(List<Integer> chosenInputPos){
        if(Objects.isNull(resourcesToChoose))
            buildResourcesToChoose();
        indexOfCurrentResourceToChoose = chosenInputPos.size();
        if(Objects.isNull(resourcesToChoose) || indexOfCurrentResourceToChoose == resourcesToChoose.size())
            return new HashMap<>();
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
                        updatedAvailability = resourcesOriginalAvailability.get(position) - selectedResourcesMap.get(position);

                    else
                        updatedAvailability = resourcesOriginalAvailability.get(position);

                    Pair<Integer, ResourceAsset> mapKey = getKeyFromPos(position);
                    MutablePair<Integer, Boolean> mapValue = selPositions.get(mapKey);
                    mapValue.setLeft(updatedAvailability);
                    if(mapValue.getLeft() == 0)
                        mapValue.setRight(false);

                }
        );

    }

    private Pair<Integer, ResourceAsset> getKeyFromPos(int pos){

        return selPositions
                .keySet()
                .stream()
                .filter(pair -> (pair.getKey().equals(pos)))
                .findFirst()
                .get();
    }

    private Map<Integer, Integer> getSelectablePositions(){

        ResourceAsset nextResourceToChoose = resourcesToChoose.get(indexOfCurrentResourceToChoose);

        return selPositions.keySet()
                .stream()
                .filter(pair -> pair.getValue().equals(nextResourceToChoose) || nextResourceToChoose.equals(ResourceAsset.TO_CHOOSE))
                .filter(pair -> selPositions.get(pair).getValue())
                .collect(Collectors.toMap
                        (
                                Pair::getKey,
                                pair -> selPositions.get(pair).getLeft()
                        ));

    }

    private void buildResourcesToChooseForCard(){

        SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
        NetworkDevelopmentCard purchasedCard = simpleCardShop.getPurchasedCard().get().getDevelopmentCard();
        resourcesToChoose = purchasedCard.getCosts();

    }

    private void  buildResourcesToChooseForProduction(){

        SimpleCardCells simpleCardCells = Objects.requireNonNull(getThisPlayerCache()).getElem(SimpleCardCells.class).orElseThrow();
        int lastSelectedProduction = simpleCardCells.getSimpleProductions().getLastSelectedProductionPosition();

        Map<ResourceAsset, Integer> inputRes =  simpleCardCells.getProductionAtPos(lastSelectedProduction).map(SimpleProductions.SimpleProduction::getInputResources).orElse(new HashMap<>());

        resourcesToChoose = inputRes.entrySet()
                .stream()
                .flatMap(p -> Stream.generate(p::getKey).limit(p.getValue()))
                .collect(Collectors.toList());
    }

    private void buildResourcesOriginalAvailability(){

        resourcesOriginalAvailability = selPositions
                .keySet()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toMap(
                position -> position,
                position -> selPositions.get(getKeyFromPos(position)).getKey()
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

    private void buildResourcesToChoose(){
        String currentState = Objects.requireNonNull(getThisPlayerCache()).getCurrentState();
        if(currentState != null) {
            if(currentState.equals(State.CHOOSING_RESOURCES_FOR_DEVCARD.toString())) {
                SimpleCardShop simpleCardShop = getSimpleModel().getElem(SimpleCardShop.class).orElseThrow();
                if (simpleCardShop.getPurchasedCard().isPresent()) {
                    buildResourcesToChooseForCard();
                }
            }
            else if(currentState.equals(State.CHOOSING_RESOURCE_FOR_PRODUCTION.toString()))
                buildResourcesToChooseForProduction();

        }
    }

}
