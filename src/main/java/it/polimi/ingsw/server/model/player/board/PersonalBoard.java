package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.ProductionLeader;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
import it.polimi.ingsw.server.utils.Util;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Collections;


/**
 * A virtual copy of the personalBoard of the game Maestri del Rinascimento
 */
public class PersonalBoard {

    /**
     * A {@link StorageUnit} containing both {@link LeaderDepot} and {@link WarehouseDepot},
     */
    private final WarehouseLeadersDepots warehouseLeadersDepots;
    /**
     * A {@link StorageUnit} representing the Strongbox of the game Maestri del Rinascimento
     */
    private final Box strongBox;
    /**
     * A {@link Box} received from the MarketBoard
     * used as a temporary deposit for resources that will be discarded if not taken by the player
     */
    private Box discardBox;
    /**
     * A list of {@link Production productions}, composed both of {@link Production productions}
     * from {@link ProductionLeader ProductionLeader} and
     * {@link ProductionCardCell}<br>
     * The first one is the basic {@link Production},
     * then there are {@link Production productions} from {@link DevelopmentCard developmentCards}
     * that are initialized to Optional.empty() before being set
     * and then the ones from {@link ProductionLeader cards.leaders}
     */
    private  List<Optional<Production>> productions;
    /**
     * An array of the spots where the player can place cards
     */
    private final ProductionCardCell[] cardCells;
    /**
     * A list that stores if a {@link Production} is selected, it has the same order of {@link #productions}
     */
    private List<Optional<Boolean>> prodsSelected;
    /**
     * The faith points that need to added by the strategy to the {@link FaithTrack FaithTrack}
     */
    private int faithPointsToAdd = 0;
    /**
     * The faith points that need to added by the strategy to the
     * {@link FaithTrack FaithTrack}
     * of the other players
     */
    private int badFaithToAdd = 0;

    /**
     * Initializes the personalBoard like in a standard game of Maestri del Rinascimento
     */
    public PersonalBoard(){
        warehouseLeadersDepots = new WarehouseLeadersDepots();
        strongBox = Box.strongBox();
        discardBox = Box.discardBox();
        productions = Stream.of(Optional.of(Production.basicProduction())).collect(Collectors.toList());
        productions.add(Optional.empty());
        productions.add(Optional.empty());
        productions.add(Optional.empty());
        cardCells = Stream.generate(ProductionCardCell::new).limit(3).toArray(ProductionCardCell[]::new);
        prodsSelected = Stream.of(Optional.of(false)).collect(Collectors.toList());
        prodsSelected.add(Optional.empty());
        prodsSelected.add(Optional.empty());
        prodsSelected.add(Optional.empty());
    }

    /**
     * Returns a list containing all the {@link ProductionCardCell productionCardCells} in {@link #cardCells}
     * @return a list containing all the {@link ProductionCardCell productionCardCells} in {@link #cardCells}
     */
    public List<ProductionCardCell> getCardCells() {
        return Arrays.stream(cardCells).collect(Collectors.toList());
    }

    public Map<Integer, List<DevelopmentCard>> getVisibleCardsOnCells(){

        return IntStream.range(0, cardCells.length).boxed().collect(Collectors.toMap(integer -> integer,
                integer -> {

                List<DevelopmentCard> card = new ArrayList<>();

                if(cardCells[integer].getStackedCardsSize() > 0)
                    card.add(cardCells[integer].getFrontCard());

                 return card;
                }));

    }

    /**
     * Takes the selected {@link Resource resources} from the selected {@link Production productions}
     * and puts the outputs {@link Resource resources}
     * in the {@link #strongBox}.
     * The output faith or bad faith gets saved
     */
    public void produce(){
        removeSelected();
        int[] resources = IntStream.range(0,prodsSelected.size()).filter((pos)->prodsSelected.get(pos).isPresent())
                .filter((pos)->prodsSelected.get(pos).get())
                .mapToObj((pos)->productions.get(pos).get().getOutputs())
                .reduce(IntStream.generate(()->0).limit(6).toArray(),(a,b)-> Util.sumArray(a,b,6));
        faithPointsToAdd = resources[4];
        badFaithToAdd = resources[5];
        strongBox.addResources(Arrays.stream(resources).limit(Resource.nRes).toArray());
        resetChoices();
        resetSelectedProductions();
    }

    /**
     * Removes the selected {@link Resource resources} from the {@link PersonalBoard}
     */
    public void removeSelected(){
        warehouseLeadersDepots.removeSelected();
        strongBox.removeSelected();
    }

    /**
     * Getter for {@link #faithPointsToAdd}
     * @return the number of faith points to add to the player
     */
    public int getFaithToAdd(){
        return faithPointsToAdd;
    }

    /**
     * Getter for {@link #badFaithToAdd}
     * @return the number of faith points to add to other players
     */
    public int getBadFaithToAdd() {
        return badFaithToAdd;
    }

    /**
     * Resets {@link #faithPointsToAdd}
     */
    public void setFaithToZero() {
        this.faithPointsToAdd = 0;
    }

    /**
     * Resets {@link #badFaithToAdd}
     */
    public void setBadFaithToZero() {
        this.badFaithToAdd = 0;
    }

    /**
     * Returns the position of the first {@link Production production} where the player can choose a {@link Resource},
     * returns OptionalInt.empty() if there are no such {@link Production productions}
     * @return an {@link OptionalInt} of the first {@link Production production} where the player can choose a {@link Resource}
     */
    public Optional<Production> firstProductionSelectedWithChoice(){
        return IntStream.range(0,productions.size())
                .filter((i)->productions.get(i).isPresent())
                .filter((i)-> prodsSelected.get(i).get())
                .filter((i)->productions.get(i).get().choiceCanBeMade())
                .mapToObj((i)->productions.get(i).get()).findFirst();
    }

    /**
     * Returns an array of the length of the {@link Production production} list,
     * containing true if the {@link Production production}
     * at that position in the array can be used with the {@link Resource resources} owned by the player
     * @return an array which values are true for available productions
     */
    public Boolean[] getAvailableProductions(){
        return productions.stream().flatMap(Optional::stream).map((p)-> hasResources(p.getInputs())).toArray(Boolean[]::new);
    }

    /**
     * Returns an array of the length of the {@link Production production} list,
     * containing true if the {@link Production production}
     * at that position will be used in this turn
     * @return an array which values are true for selected productions
     */
    public Boolean[] getSelectedProduction(){return prodsSelected.stream().filter(Optional::isPresent).flatMap(Optional::stream).toArray(Boolean[]::new);}

    /**
     * Resets the {@link Resource resources} chosen to {@link Resource#TOCHOOSE} for all the {@link Production productions} that have choices.
     * Should be called at the end of the turn
     */
    public void resetChoices(){
        productions.stream().flatMap(Optional::stream).filter(Production::choiceCanBeMade).forEach(Production::resetchoice);
    }

    /**
     * Resets the {@link Resource resources} chosen to {@link Resource#TOCHOOSE} for all the {@link Production productions} that have choices
     */
    public void resetSelectedProductions(){
        IntStream.range(0,prodsSelected.size())
                .filter((pos)->prodsSelected.get(pos).isPresent())
                .forEach((pos)->prodsSelected.set(pos,Optional.of(false)));
    }

    /**
     * Toggles the selection of the {@link Production production} at the given position,
     * deciding if it will be used in the current turn
     * @param pos position of a production
     */
    public void toggleSelectProductionAt(int pos) {
        prodsSelected.set(pos,
                prodsSelected.get(pos).map((op)->!op)
        );
    }

    /**
     * Flags the {@link Production production} at the given position as selected,
     * meaning that it will be used in the current turn
     * @param pos position of a production
     */
    public void selectProductionAt(int pos) {prodsSelected.set(pos,Optional.of(true));}
    /**
     * Flags the {@link Production production} at the given position as deselected,
     * meaning that it will not be used in the current turn
     * @param pos position of a production
     */
    public void deselectProductionAt(int pos) {prodsSelected.set(pos,Optional.of(false));}

    /**
     * Adds a {@link Production production} to the list of productions
     * @param production is a {@link Production}
     */
    public void addProduction(Production production)
    {
        productions.add(Optional.of(production));
        prodsSelected.add(Optional.of(false));
    }

    /**
     * Adds the given {@link DevelopmentCard card} to the top of one of the spots that can store {@link DevelopmentCard cards} in the PersonalBoard.
     * The number of spot where to add the {@link DevelopmentCard card} is specified by the given position
     * @param card {@link DevelopmentCard card} that will be added
     * @param pos position starting from 0 of the representing a spot on the PersonalBoard
     */
    public void addDevelopmentCardToCell(DevelopmentCard card, int pos) {
        productions.set(pos+1,Optional.of(card.getProduction()));
        prodsSelected.set(pos+1,Optional.of(false));
        cardCells[pos].addToTop(card);
    }

    /**
     * Returns true if the player has bought six or more cards at the development market
     * @return true if the player has bought six or more cards at the development market
     */
    public boolean playerHasSixOrMoreCards() {
        return Arrays.stream(cardCells).map((p)->p.getStackedCards().size())
                .reduce(0, Integer::sum)>=6;
    }

    /**
     * Returns how many of the given {@link Resource resource} the player has in all the {@link StorageUnit deposits}
     * @param type a {@link Resource resource}, should be a "physical" {@link Resource resource} for a meaningful call
     * @return a int indicating how many of the given {@link Resource resource} the player has in all the {@link StorageUnit deposits}
     */
    public int getNumberOf(Resource type) {
        return strongBox.getNumberOf(type)+warehouseLeadersDepots.getNumberOf(type);
    }

    /**
     * Returns the total number of {@link Resource resource} the player has in all the {@link StorageUnit deposits}
     * @return total number of {@link Resource resource} contained in all the {@link StorageUnit deposits}
     */
    public int numOfResources(){return Resource.getStream(Resource.nRes).map(this::getNumberOf).reduce(0,Integer::sum);}


    /**
     * Returns how many more {@link Resource resources} the player needs to select to activate {@link Production production}
     * @return number of {@link Resource resources} to select remaining to activate {@link Production production}
     */
    public int remainingToSelectForProduction(){
        return  Math.max(0,
                IntStream.range(0,productions.size()).filter((pos)->productions.get(pos).isPresent())
                .filter((pos)->prodsSelected.get(pos).get()).map((pos)->productions.get(pos).get().getNumOfResInInput())
                .reduce(0,Integer::sum)
                -
                (warehouseLeadersDepots.getTotalSelected()+strongBox.getTotalSelected()));
    }


    /**
     * Getter for the {@link #strongBox}
     * @return the {@link #strongBox}
     */
    public Box getStrongBox(){
        return strongBox;
    }

    /**
     * Getter for the {@link #discardBox}
     * @return the {@link #discardBox}
     */
    public Box getDiscardBox(){
        return discardBox;
    }

    /**
     * Getter for the {@link #warehouseLeadersDepots}
     * @return the {@link #warehouseLeadersDepots}
     */
    public WarehouseLeadersDepots getWarehouseLeadersDepots(){
        return warehouseLeadersDepots;
    }

    public Map<Integer, List<Integer>> getSimpleWarehouseLeadersDepots(){
        return warehouseLeadersDepots.getSimpleWarehouseLeadersDepots();
    }

    public Map<Integer, Integer> getSimpleStrongBox(){
        return strongBox.getSimpleBox();
    }

    /**
     * Removes resources from the {@link #discardBox} and records how many faith points to add to other players
     * since each resource discarded corresponds to one faith point that the other players will get
     */
    public void discardResources(){
        badFaithToAdd += Resource.getStream(Resource.nRes).map(discardBox::getNumberOf).reduce(0,Integer::sum);
        faithPointsToAdd += discardBox.getNumberOf(Resource.FAITH);
        discardBox = Box.discardBox();
    }

    /**
     * Sets the {@link #discardBox} to the given {@link Box}
     * @param marketBox a {@link Box}
     */
    public void setMarketBox(Box marketBox){
        discardBox = marketBox;
    }

    /**
     * Returns the {@link StorageUnit} that contains the given position<br>
     * Deposits position convention:<br>
     * -8 to -5 for {@link #strongBox}<br>
     * -4 to -1 for {@link #discardBox}<br>
     * >=0 for {@link #warehouseLeadersDepots}<br>
     * The lower number always indicates the {@link Resource resource} int the lower position of the {@link Resource} ordering
     * @param pos position of the {@link StorageUnit} to get
     * @return {@link StorageUnit} that contains the given position
     */
    public StorageUnit storageUnitFromPos(int pos){
        if (pos>=-8 && pos<=-5) return strongBox;
        else if (pos>=-4 && pos<=-1) return discardBox;
        else return warehouseLeadersDepots;
    }

    /**
     * Chooses the {@link Resource resource} at the given global position as the first choice in the first {@link Production production} with choices
     * @param resPos a position with the deposits position convention, >=-8
     */
    public void performChoiceOnInput(int resPos){
        firstProductionSelectedWithChoice().ifPresent((production)-> {
            Resource res = storageUnitFromPos(resPos).getResourceAt(resPos);
            storageUnitFromPos(resPos).selectResourceAt(resPos);
            production.performChoiceOnInput(res);
        });
    }

    /**
     * Chooses the given {@link Resource resource} as the output the first choice in the first {@link Production production} with choices
     * @param res a "Physical" {@link Resource}
     */
    public void performChoiceOnOutput(Resource res){
        firstProductionSelectedWithChoice().filter(Production::choiceCanBeMadeOnOutput).ifPresent((production)-> production.performChoiceOnOutput(res));
    }

    /**
     * Moves {@link Resource resources} across deposits from the given start to given end position.<br>
     * Deposits position convention:<br>
     * -8 to -5 for {@link #strongBox}<br>
     * -4 to -1 for {@link #discardBox}<br>
     * >=0 for {@link #warehouseLeadersDepots}<br>
     * The lower number always indicates the {@link Resource resource} int the lower position of the {@link Resource} ordering
     * @param startPos the position from where to take the {@link Resource resource} following the deposits position convention
     * @param endPos the position where to put the {@link Resource resource} following the deposits position convention
     */
    public void move(int startPos, int endPos){
        storageUnitFromPos(endPos).addResource(
                new Pair<>(endPos, storageUnitFromPos(startPos).getResourceAt(startPos))
        );
        storageUnitFromPos(startPos).removeResource(startPos);
    }

    /**
     * This method will check if the given resources are present in the {@link PersonalBoard}.
     * @param toCheck in the cell with index i there is the number of resource with order i in the resource ordering to check for availability
     * @return True if there are enough resources for selected productions
     */
    public boolean hasResources(int[] toCheck)
    {
        Box box= getStrongBox();
        //Goes up to toChoose
        int[] inputOfLengthResources = IntStream.concat(Arrays.stream(toCheck),IntStream.generate(()->0)).limit(Resource.nRes+3).toArray();
        //For physical resources
        int[] toFindInWarehouse =IntStream.range(0,Resource.nRes).map(pos -> inputOfLengthResources[pos]-box.getNumberOf(Resource.fromInt(pos))).map(res -> Math.max(res, 0)).toArray();
        if(!getWarehouseLeadersDepots().enoughResourcesForProductions(toFindInWarehouse))
            return false;
        return Arrays.stream(inputOfLengthResources).limit(Resource.nRes).reduce(0, Integer::sum)+inputOfLengthResources[6]<=numOfResources();
    }

    /**
     * This method will check if there are enough spaces in the {@link ProductionCardCell}
     * @param developmentCard level as an int value
     * @return is true if there is at least a space available
     */
    public boolean isDevCardLevelSatisfied(DevelopmentCard developmentCard)
    {
        for(int i=0; i<getCardCells().size(); i++)
            if(getCardCells().get(i).getStackedCardsSize()+1==developmentCard.getLevel())
                return true;
        return false;
    }

    /**
     * This method combines two methods to complete the requirements for developmentcards
     * @param developmentCard is not NULL
     * @return is true if both conditions are true
     */
     public boolean isDevelopmentCardAvailable(DevelopmentCard developmentCard)
     {
        return isDevCardLevelSatisfied(developmentCard) && hasResources(developmentCard.getCostAsArray());
     }

    /**
     * This method will check if there are enough resources and cards to play a leader.
     * Leaders cost is converted to an array to re-use the method for the productions.
     * @param leader is not NULL
     * @return is true if requirements are met
     */
    public boolean isLeaderRequirementsSatisfied(Leader leader){
        int[] toar = new int[4];
        for (Pair<Resource, Integer> resourceIntegerPair : leader.getRequirementsResources())
            toar[resourceIntegerPair.getKey().getResourceNumber()] += resourceIntegerPair.getValue();

        return hasResources(toar)&&isLeaderColorRequirementsSatisfied(leader);
    }

    /**
     * This method will check if there are enough cards to play a leader
     * @param leader is not NULL
     * @return is true if requirements are met
     */
    public boolean isLeaderColorRequirementsSatisfied(Leader leader)
    {
        int temp=0;
        for (Pair<DevelopmentCardColor, Integer> requirementsCard : leader.getRequirementsCards()) {
            for (int j = 0; j < getCardCells().size(); j++)
                temp +=getCardCells().get(j).howManyOfColor(requirementsCard.getKey(),leader.getRequirementsCardsLevel());
            if (temp < requirementsCard.getValue())
                return false;
            temp=0;
        }
        return true;
    }

    /**
     * Returns all the available global positions in the {@link PersonalBoard} where the given resource can be moved
     * @return an IntStream of global positions
     */
    public IntStream availableMovingPositionsForResource(Resource res){
        return IntStream.concat(
                IntStream.of(discardBox.globalPositionOfRes(res),strongBox.globalPositionOfRes(res)),
                warehouseLeadersDepots.availableMovingPositionsForResource(res));
    }

    /**
     * Returns all the available positions in all the {@link PersonalBoard} where the resource at the given position can be moved
     * @param position the global position of the resource of which available moving positions will be returned
     * @return an IntStream of global positions
     */
    public IntStream availableMovingPositionsForResourceAt(int position){
        return availableMovingPositionsForResource(storageUnitFromPos(position).getResourceAt(position)).filter((i)->i!=position);
    }

    /**
     * Returns the resource at a given position in the {{@link PersonalBoard#warehouseLeadersDepots} or {@link PersonalBoard#strongBox}
     * @return the resource at the given position, if present, otherwise returns {@link Resource#EMPTY EMPTY}
     */
    public Resource getResourceAtPosition(int position){
        return warehouseLeadersDepots.getResourceAt(position).equals(Resource.EMPTY) ?
                strongBox.getResourceAt(position) :
                warehouseLeadersDepots.getResourceAt(position);
    }

}
