package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.DevelopmentCardAsset;
import it.polimi.ingsw.network.assets.LeaderCardAsset;
import it.polimi.ingsw.network.assets.leaders.NetworkProductionLeaderCard;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleCardCells extends SimpleModelElement{

    private SimplePlayerLeaders simplePlayerLeaders;

    private Map<Integer, LeaderCardAsset> activeProductionLeaders;

    private Map<Integer, List<DevelopmentCardAsset>> devCardsCells;

    private Map<Integer, Boolean> availableSpotsForDevCard;

    private SimpleProductions simpleProductions;

    public SimpleCardCells(){}

    public void setSimplePlayerLeaders(SimplePlayerLeaders simplePlayerLeaders){
        this.simplePlayerLeaders = simplePlayerLeaders;
    }

    public void setSimpleProductions(SimpleProductions simpleProductions){
        this.simpleProductions = simpleProductions;
    }

    public SimpleCardCells(Map<Integer, List<Pair<UUID, Boolean>>> cards,  Map<Integer, Boolean> availableSpotsForDevCard){

        this.availableSpotsForDevCard = availableSpotsForDevCard;

        this.devCardsCells = cards
                .keySet()
                .stream()
                .collect(Collectors.toMap(
                position -> position,
                position -> cards.get(position).stream().map(cardPair -> {

                DevelopmentCardAsset card = Cards.getDevelopmentCardAsset(cardPair.getKey()).orElseThrow();
                card.getDevelopmentCard().setSelectable(cardPair.getValue());
                return card;

                }).collect(Collectors.toList())
        ));
    }

    public void update(SimpleModelElement element){
        SimpleCardCells serverCardCells = (SimpleCardCells) element;
        this.devCardsCells = serverCardCells.devCardsCells;
        this.availableSpotsForDevCard = serverCardCells.availableSpotsForDevCard;
        updateProductionLeaders();

    }

    private void updateProductionLeaders(){

        List<LeaderCardAsset> playerLeaders = simplePlayerLeaders.getPlayerLeaders();

        List<LeaderCardAsset> activeProductionLeadersList = playerLeaders
                .stream()
                .filter( leaderCardAsset -> (leaderCardAsset.getNetworkLeaderCard() instanceof NetworkProductionLeaderCard))
                .filter( leaderCardAsset -> leaderCardAsset.getNetworkLeaderCard().isLeaderActive())
                .collect(Collectors.toList());


        int lastDevCardsCellsIndex = devCardsCells.keySet().stream().mapToInt(index -> index).max().orElse(3);

        activeProductionLeaders = IntStream.range(lastDevCardsCellsIndex + 1, lastDevCardsCellsIndex + 1 + activeProductionLeadersList.size())
                .boxed()
                .collect(Collectors.toMap(

                index -> index,
                index -> activeProductionLeadersList.get(index - lastDevCardsCellsIndex - 1)

                ));

    }

    public Map<Integer, Optional<DevelopmentCardAsset>> getDevCardsCells(){

        return devCardsCells.entrySet().stream().collect(Collectors.toMap(

                Map.Entry::getKey,
                e -> e.getValue().isEmpty()?Optional.empty():Optional.ofNullable(e.getValue().get(0))
        ));

    }

    public Map<Integer, LeaderCardAsset> getActiveProductionLeaders(){
        return activeProductionLeaders;
    }

    public boolean isSpotAvailable(int position){

        if(availableSpotsForDevCard.get(position)!=null)
            return availableSpotsForDevCard.get(position);
        else
            return false;
    }

    //basic production is at position 0

    public Optional<SimpleProductions.SimpleProduction> getProductionAtPos(int position){
        return simpleProductions.getProductionAtPos(position);
    }

    public Optional<Boolean> isProductionAtPositionSelected(int productionPosition){
        return simpleProductions.isProductionAtPositionSelected(productionPosition);
    }

    public Optional<Boolean> isProductionAtPositionAvailable(int productionPosition){
        return simpleProductions.isProductionAtPositionAvailable(productionPosition);
    }


}
