package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.Production;
import it.polimi.ingsw.server.model.ProductionCardCell;
import it.polimi.ingsw.server.model.Resource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PersonalBoard {

    WarehouseLeadersDepots warehouseLeadersDepots;
    Box strongBox,discardBox,fromMarketBox;
    List<Production> productions;
    ProductionCardCell[] cardCells;
    List<Boolean> prodsSelected;
    int faithPointsToAdd, badFaithToAdd =0;


    PersonalBoard(){
        warehouseLeadersDepots = new WarehouseLeadersDepots();
        strongBox = new Box();
        //productions.add(new Production());//Adds basic production
        cardCells = Stream.generate(ProductionCardCell::new).limit(3).toArray(ProductionCardCell[]::new);
        prodsSelected.add(false);
    }

    public List<ProductionCardCell> getCardCells() {
        return Arrays.stream(cardCells).toList();
    }

    public void produce(){
        //warehouseLeadersDepots.removeSelected();
        //strongBox.removeSelected();
        //Map<Resource,Resource[]> a = IntStream.range(0,prodsSelected.size()).filter((pos)->prodsSelected.get(pos))
        //        .flatMap((pos)->productions.get(pos).getOutputs()).collect(Collectors.groupingBy((type)->type));
        //int[] resourcesRoAdd = a.values().stream().mapToInt(res -> res.length).toArray();
        //faithPointsToAdd = resourcesRoAdd[4];
        //badFaithToAdd = resourcesRoAdd[5];
        //strongBox.addResources(Arrays.stream(resourcesRoAdd).limit(4).toArray());
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
        //return IntStream.range(0,productions.size()).filter((pos)->productions[pos].choiceCanBeMade).findFirst();
        return OptionalInt.empty();
    }
    public Boolean[] calculateAvailableProductions(){
        return null;
    }
    public void resetChoices(){
        //productions.stream().filter((pro)->pro.choiceCanBeMade()).forEach((pro)->pro.resetChoice());
    }

    public void addProduction(Production production)
    {
        productions.add(production);
    }

    void resetSelectedProductions(){
        IntStream.range(0,prodsSelected.size()).forEach((pos)->prodsSelected.set(pos,false));
    }
    public int[] getSelectedProduction(){return null;}
    public void toggleSelectProductionAt(int pos) {}
    public void selectProductionAt(int pos) {}
    public void deselectProductionAt(int pos) {}
    public void setTemporaryCard(DevelopmentCard card) {}
    public boolean playerHasSixCards() {return false;}
    public int numOfResourcesOfType(Resource resources) {return  0;
    }
    public int numOfResources(){return 0;}

    public Box getStrongBox(){
        return strongBox;
    }
    public WarehouseLeadersDepots getWarehouseLeadersDepots(){
        return warehouseLeadersDepots;
    }
    void discardResources(){}
    void setMarketBox(){}
    void move(int startPos, int endPos){}
}
