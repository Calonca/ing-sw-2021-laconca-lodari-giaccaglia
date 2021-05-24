package it.polimi.ingsw.client.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimplePersonalBoard {

    private SimpleDepots depots;

    private Map<Integer, List<DevelopmentCardAsset>> visibleCardsOnCells;

    private SimpleFaithTrack simpleFaithTrack;

    public SimplePersonalBoard(SimpleDepots depots, Map<Integer, List<UUID>> cards, String trackConfig){
        updateSimplePersonalBoard(depots, cards, trackConfig);
    }

    public void updateSimplePersonalBoard(SimpleDepots depots, Map<Integer, List<UUID>> cards, String trackConfig){
        updateSimpleDepots(depots);
        updateVisibleCards(cards);
        updateSimpleFaithTrack(trackConfig);
    }

    public void updateSimpleDepots(SimpleDepots depots){
        this.depots = depots;
    }

    public void updateSimpleDepots(Map<Integer, List<ResourceAsset>> simpleWarehouseLeadersDepot, Map<ResourceAsset, Integer> simpleStrongBox){
        depots.updateSimpleDepots(simpleWarehouseLeadersDepot, simpleStrongBox);
    }

    public void updateSimpleFaithTrack(String config){
        simpleFaithTrack.updateTrack(config);
    }

    public void updateVisibleCards(Map<Integer, List<UUID>> cards){
        this.visibleCardsOnCells = cards.keySet().stream().collect(Collectors.toMap(
                integer -> integer,
                integer -> {
                    List<DevelopmentCardAsset> card = new ArrayList<>();

                    if(cards.get(integer).size()>0)
                        card = cards.get(integer).stream().map(Cards::getDevelopmentCardAsset).collect(Collectors.toList());

                    return card;
                }
        ));
    }

    public SimpleDepots getDepots() {
        return depots;
    }

    public Map<Integer, List<DevelopmentCardAsset>> getVisibleCardsOnCells() {
        return visibleCardsOnCells;
    }
}