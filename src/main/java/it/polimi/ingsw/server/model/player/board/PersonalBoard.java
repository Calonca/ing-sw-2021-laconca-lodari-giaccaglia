package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.Production;
import it.polimi.ingsw.server.model.ProductionCardCell;
import it.polimi.ingsw.server.model.Resource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PersonalBoard {

    WarehouseLeadersDepots warehouseLeadersDepots;
    Box strongBox,discardBox;
    List<Production> productions;
    ProductionCardCell[] cardCells;
    List<Boolean> prodsSelected;
    int faithPointsToAdd, badFaithToAdd =0;


    public PersonalBoard(){
        warehouseLeadersDepots = new WarehouseLeadersDepots();
        strongBox = new Box();
        discardBox = new Box();
        productions.add(Production.basicProduction());
        cardCells = Stream.generate(ProductionCardCell::new).limit(3).toArray(ProductionCardCell[]::new);
        prodsSelected.add(false);
    }

    public List<ProductionCardCell> getCardCells() {
        return Arrays.stream(cardCells).collect(Collectors.toList());
    }

    public void produce(){
        warehouseLeadersDepots.removeSelected();
        strongBox.removeSelected();
        IntStream.range(0,prodsSelected.size()).filter((pos)->prodsSelected.get(pos))
                .mapToObj((pos)->productions.get(pos).getOutputs())
                .reduce((a,b)->IntStream.range(0,6).map((pos)->a[pos]+b[pos]).toArray()).ifPresent((resources)->{
                faithPointsToAdd = resources[4];
                badFaithToAdd = resources[5];
                strongBox.addResources(Arrays.stream(resources).limit(4).toArray());
                }
        );
    }

    public boolean isProductionEmpty()
    {
        return productions.size()!=0;
    }
    
    public int getFaithToAdd(){
        return faithPointsToAdd;
    }

    public int getBadFaithToAdd() {
        return badFaithToAdd;
    }

    public void setFaithToZero() {
        this.faithPointsToAdd = 0;
    }

    public void setBadFaithToZero() {
        this.badFaithToAdd = 0;
    }

    public OptionalInt firstProductionWithChoice(){
        return IntStream.range(0,productions.size()).filter(
                (pos)->productions.get(pos).choiceCanBeMade()).findFirst();
    }

    public Boolean[] getAvailableProductions(){
        return productions.stream().map((p)->p.isAvailable(this)).toArray(Boolean[]::new);
    }
    public void resetChoices(){
        productions.stream().filter(Production::choiceCanBeMade).forEach(Production::resetchoice);
    }

    public void addProduction(Production production)
    {
        productions.add(production);
    }

    void resetSelectedProductions(){
        IntStream.range(0,prodsSelected.size()).forEach((pos)->prodsSelected.set(pos,false));
    }

    public Boolean[] getSelectedProduction(){return prodsSelected.toArray(Boolean[]::new);}

    public void toggleSelectProductionAt(int pos) {prodsSelected.set(pos,!prodsSelected.get(pos));}
    public void selectProductionAt(int pos) {prodsSelected.set(pos,true);}
    public void deselectProductionAt(int pos) {prodsSelected.set(pos,false);}
    public void addDevelopmentCardToCell(DevelopmentCard card,int pos) {
        cardCells[pos].addToTop(card);
    }
    public boolean playerHasSixCards() {
        return Arrays.stream(cardCells).map((p)->p.stackedCards.size())
                .reduce(0, Integer::sum)==6;
    }

    public int getNumberOf(Resource type) {
        return strongBox.getNumberOf(type)+warehouseLeadersDepots.getNumberOf(type);
    }

    public int numOfResources(){return Resource.getStream(Resource.nRes).map(this::getNumberOf).reduce(0,Integer::sum);}

    public Box getStrongBox(){
        return strongBox;
    }
    public WarehouseLeadersDepots getWarehouseLeadersDepots(){
        return warehouseLeadersDepots;
    }
    void discardResources(){
        badFaithToAdd += Resource.getStream(Resource.nRes).map(discardBox::getNumberOf).reduce(0,Integer::sum);
        discardBox = new Box();
    }
    void setMarketBox(){
        //Todo setMarketBox and check usage
    }
    void move(int startPos, int endPos){
        //Todo move
    }
}
