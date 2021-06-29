package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import it.polimi.ingsw.server.model.player.leaders.DevelopmentDiscountLeader;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.player.leaders.ProductionLeader;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
import it.polimi.ingsw.network.util.Util;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


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
     * Keeps track of currently active {@link DevelopmentDiscountLeader} related resources discounts.
     */
    private final int[] discounts;

    /**
     * A list of {@link Production productions}, composed both of {@link Production productions}
     * from {@link ProductionLeader ProductionLeader} and
     * {@link ProductionCardCell}<br>
     * The first one is the basic {@link Production},
     * then there are {@link Production productions} from {@link DevelopmentCard developmentCards}
     * that are initialized to Optional.empty() before being set
     * and then the ones from {@link ProductionLeader cards.leaders}
     */
    private Map<Integer, Production> productions;
    /**
     * An array of the spots where the player can place cards
     */
    private final ProductionCardCell[] cardCells;
    /**
     * A list that stores if a {@link Production} is selected, it has the same order of {@link #productions}
     */
    private Map<Integer, Boolean> prodsSelected;

    private Map<Integer, List<Integer>> historyOfInputResourcesForProduction;  //key -> position of production; value -> position of chosen resources

    private int lastSelectedProductionPosition;

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
      /*  strongBox.addResources(new int[]{20,20,20,20});   //to test CardShop
        warehouseLeadersDepots.addResource(new Pair<>(0,Resource.GOLD));
        warehouseLeadersDepots.addResource(new Pair<>(1,Resource.SERVANT));
        warehouseLeadersDepots.addResource(new Pair<>(3,Resource.STONE)); */
        productions = Stream.of((Production.basicProduction())).collect(Collectors.toMap(production  -> 0, production -> production));
        cardCells = Stream.generate(ProductionCardCell::new).limit(3).toArray(ProductionCardCell[]::new);
        prodsSelected = new HashMap<>();
        prodsSelected.put(0, false); //basic production
        discounts=new int[8];
        historyOfInputResourcesForProduction = new HashMap<>();
        lastSelectedProductionPosition = -1;

    }

    /**
     * Returns a list containing all the {@link ProductionCardCell productionCardCells} in {@link #cardCells}
     * @return a list containing all the {@link ProductionCardCell productionCardCells} in {@link #cardCells}
     */
    public List<ProductionCardCell> getCardCells() {
        return Arrays.stream(cardCells).collect(Collectors.toList());
    }

    public Optional<Production> getProductionFromPosition(int position){

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(position >= 0 && position <= productionsLastPosition)
            return Optional.of(productions.get(position));

        else return Optional.empty();
    }

    public Map<Integer, List<DevelopmentCard>> getVisibleCardsOnCells(){

        return IntStream.rangeClosed(1, cardCells.length).boxed().collect(Collectors.toMap(
                integer -> integer,
                integer -> cardCells[integer-1].getStackedCards()
        ));

    }

    public Map<Integer, Pair <Pair<Map<Integer, Integer> , Map<Integer, Integer>>, Pair<Boolean, Boolean>>> getSimpleProductionsMap(){

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(productionsLastPosition<cardCells.length + 2)
            productionsLastPosition = cardCells.length + 2;

        return IntStream.rangeClosed(0, productionsLastPosition).boxed().collect(Collectors.toMap(
                productionIndex-> productionIndex,
                productionIndex -> {

                    Map<Integer, Integer> inputs =  productions.containsKey(productionIndex) ? productions.get(productionIndex).getInputsMap() : new HashMap<>();

                    Map<Integer, Integer> outputs = productions.containsKey(productionIndex) ?  productions.get(productionIndex).getOutputsMap() : new HashMap<>();



                    boolean isSelected = prodsSelected.containsKey(productionIndex) && prodsSelected.get(productionIndex);

                    boolean isAvailable = !isSelected && productions.containsKey(productionIndex) && hasResources( productions.get(productionIndex).getInputs());

                    Pair<Map<Integer, Integer>, Map<Integer, Integer>> inputsAndOutPuts = new Pair<>(inputs, outputs);

                    return new Pair<>(inputsAndOutPuts, new Pair<>(isAvailable, isSelected));

                }
        ));

    }

    /**
     * Takes the selected {@link Resource resources} from the selected {@link Production productions}
     * and puts the outputs {@link Resource resources}
     * in the {@link #strongBox}.
     * The output faith or bad faith gets saved
     */
    public void produce(){

        removeSelected();

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(productionsLastPosition<cardCells.length + 2)
            productionsLastPosition = cardCells.length + 2;

        int[] resources = IntStream.rangeClosed(0,productionsLastPosition).filter((pos)->prodsSelected.containsKey(pos))
                .filter((pos)->prodsSelected.get(pos))
                .mapToObj((pos)->productions.get(pos).getOutputs())
                .reduce(IntStream.generate(()->0).limit(6).toArray(),(a,b)-> Util.sumArray(a,b,6));

        faithPointsToAdd = resources[4];
        badFaithToAdd = resources[5];
        strongBox.addResources(Arrays.stream(resources).limit(Resource.nRes).toArray());

        resetChoices();
        resetSelectedProductions();
        resetHistoryOfInputResourcesForProduction();

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

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(productionsLastPosition<cardCells.length + 2)
            productionsLastPosition = cardCells.length + 2;


        return IntStream.rangeClosed(0,productionsLastPosition)
                .filter((i)-> prodsSelected.containsKey(i))
                .filter((i) -> prodsSelected.get(i))
                .filter((i)->productions.get(i).choiceCanBeMade())
                .mapToObj((i)->productions.get(i)).findFirst();
    }

    /**
     * Returns an array of the length of the {@link Production production} list,
     * containing true if the {@link Production production}
     * at that position in the array can be used with the {@link Resource resources} owned by the player
     * @return an array which values are true for available productions
     */
    public Boolean[] getAvailableProductions(){

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(productionsLastPosition<cardCells.length + 2)
            productionsLastPosition = cardCells.length + 2;

        return IntStream.rangeClosed(0, productionsLastPosition)
                .boxed()
                .map(index -> {

                    if(productions.containsKey(index))
                        return hasResources(productions.get(index).getInputs());
                    else return false;

                })
                .toArray(Boolean[]::new);

    }





    /**
     * Returns an array of the length of the {@link Production production} list,
     * containing true if the {@link Production production}
     * at that position will be used in this turn
     * @return an array which values are true for selected productions
     */
    public Boolean[] getSelectedProduction(){

        int productionsLastPosition = prodsSelected.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(productionsLastPosition<cardCells.length + 2)
            productionsLastPosition = cardCells.length + 2;

        return IntStream.rangeClosed(0, productionsLastPosition)
                .boxed()
                .map(index -> prodsSelected.getOrDefault(index, false))
                .toArray(Boolean[]::new);

    }

    /**
     * Resets the {@link Resource resources} chosen to {@link Resource#TO_CHOOSE} for all the {@link Production productions} that have choices.
     * Should be called at the end of the turn
     */
    public void resetChoices(){
        productions.values().stream().filter(Production::choiceCanBeMade).forEach(Production::resetChoice);
    }

    /**
     * Resets the {@link Resource resources} chosen to {@link Resource#TO_CHOOSE} for all the {@link Production productions} that have choices
     */
    public void resetSelectedProductions(){
        prodsSelected.keySet().forEach(pos -> prodsSelected.put(pos, false));
    }

    /**
     * Toggles the selection of the {@link Production production} at the given position,
     * deciding if it will be used in the current turn
     * @param pos position of a production
     */
    public void toggleSelectProductionAt(int pos) {

        int productionsLastPosition = prodsSelected.keySet().stream().mapToInt(position -> position).max().getAsInt();


        if(pos>productionsLastPosition || !productions.containsKey(pos))
            return;

        prodsSelected.put(pos, !prodsSelected.get(pos));

        if(prodsSelected.containsKey(pos))   //production enabled
            lastSelectedProductionPosition = pos;

        if(!prodsSelected.containsKey(pos) && historyOfInputResourcesForProduction.containsKey(pos)) {  //disable production, revert selected resources

            List<Integer> positionsOfResourcesToDeselect = historyOfInputResourcesForProduction.get(pos);
            Production toggledProduction = productions.get(pos);
            positionsOfResourcesToDeselect.forEach(this::deselectResourceAt);

            resetHistoryOfProductionResources(pos);

            toggledProduction.resetChoice();

        }
    }

    public void resetAllSelectedProductions(){

        resetChoices();
        resetSelectedProductions();
        historyOfInputResourcesForProduction.keySet().forEach(this::deselectResourceAt);
        resetHistoryOfInputResourcesForProduction();

    }


    /**
     * Flags the {@link Production production} at the given position as selected,
     * meaning that it will be used in the current turn
     * @param pos position of a production
     */
    public void selectProductionAt(int pos) {
        if(!productions.containsKey(pos))
            return;
        prodsSelected.put(pos,true);}
    /**
     * Flags the {@link Production production} at the given position as deselected,
     * meaning that it will not be used in the current turn
     * @param pos position of a production
     */
    public void deselectProductionAt(int pos) {

        if(pos>= productions.size())
            return;

        prodsSelected.put(pos,false);}

    /**
     * Adds a {@link Production production} to the list of productions
     * @param production is a {@link Production}
     */
    public void addProduction(Production production)
    {

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        productions.put(productionsLastPosition+1, production);
        prodsSelected.put(productionsLastPosition+1, false);
    }

    /**
     * Adds the given {@link DevelopmentCard card} to the top of one of the spots that can store {@link DevelopmentCard cards} in the PersonalBoard.
     * The number of spot where to add the {@link DevelopmentCard card} is specified by the given position
     * @param card {@link DevelopmentCard card} that will be added
     * @param pos position starting from 0 of the representing a spot on the PersonalBoard
     */
    public void addDevelopmentCardToCell(DevelopmentCard card, int pos) {

        productions.put(pos+1, card.getProduction());
        prodsSelected.put(pos+1, false);
        cardCells[pos].addToTop(card);
    }


    //todo send warning message when player has 6 cards!
    /**
     * Returns true if the player has bought six or more cards at the development market
     * @return true if the player has bought six or more cards at the development market
     */
    public boolean playerHasSixOrMoreCards() {
        return Arrays.stream(cardCells).map((p)->p.getStackedCards().size())
                .reduce(0, Integer::sum)>=6;
    }

    public boolean playerHasSevenCards(){
        return Arrays.stream(cardCells).map((p)->p.getStackedCards().size())
                .reduce(0, Integer::sum)==7;
    }


    /**
     * Returns how many of the given {@link Resource resource} the player has in all the {@link StorageUnit deposits}
     * @param resourceType a {@link Resource resource}, should be a "physical" {@link Resource resource} for a meaningful call
     * @return a int indicating how many of the given {@link Resource resource} the player has in all the {@link StorageUnit deposits}
     */
    public int getNumberOf(Resource resourceType) {
        return strongBox.getNumberOf(resourceType) + warehouseLeadersDepots.getNumberOf(resourceType);
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

        int productionsLastPosition = productions.keySet().stream().mapToInt(pos -> pos).max().getAsInt();

        if(productionsLastPosition<cardCells.length + 2)
            productionsLastPosition = cardCells.length + 2;

        return  Math.max(0,
                IntStream.rangeClosed(0,productionsLastPosition).filter((pos) -> prodsSelected.containsKey(pos))
                        .filter((pos)->prodsSelected.get(pos)).map((pos)->productions.get(pos).getNumOfResInInput())
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

    public Map<Integer, List<Pair<Resource, Boolean>>> getSimpleWarehouseLeadersDepots(){
        return warehouseLeadersDepots.getSimpleWarehouseLeadersDepots();
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
    private StorageUnit storageUnitFromPos(int pos){
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

            Production firstProductionSelectedWithChoice = firstProductionSelectedWithChoice().get();

            int productionPosition = productions.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(firstProductionSelectedWithChoice))
                    .findFirst()
                    .get()
                    .getKey();

            if(historyOfInputResourcesForProduction.containsKey(productionPosition)) {
                List<Integer> chosenResourcesPositions = historyOfInputResourcesForProduction.get(productionPosition);
                chosenResourcesPositions.add(resPos);

            }
            else {

                List<Integer> chosenResourcesPositions = new ArrayList<>();
                chosenResourcesPositions.add(resPos);
                historyOfInputResourcesForProduction.put(productionPosition, chosenResourcesPositions);
            }

            storageUnitFromPos(resPos).selectResourceAt(resPos);
            production.performChoiceOnInput(res);
        });
    }

    public void selectResourceAt(int position){

        storageUnitFromPos(position).selectResourceAt(position);
    }

    public void deselectResourceAt(int position){

        storageUnitFromPos(position).deselectResourceAt(position);

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

        //Goes up to toChoose
        int[] inputOfLengthResources = IntStream.concat(Arrays.stream(toCheck),IntStream.generate(()->0)).limit(Resource.nRes+3).toArray();

        //For physical resources
        int[] toFindInWarehouse =IntStream.range(0,Resource.nRes).sorted()
                .map(pos -> inputOfLengthResources[pos]- strongBox.getNumberOfNotSelected(Resource.fromInt(pos)))
                .map(res -> Math.max(res, 0))
                .toArray();

        if(!getWarehouseLeadersDepots().enoughResourcesForProductions(toFindInWarehouse))
            return false;
        return Arrays.stream(inputOfLengthResources).limit(Resource.nRes).reduce(0, Integer::sum) + inputOfLengthResources[6]<=numOfResources();

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
     * @param developmentCard if NULL -> return false
     * @return is true if both conditions are true
     */
    public boolean areDevCardRequirementsSatisfied(DevelopmentCard developmentCard)
    {
        if(Objects.isNull(developmentCard))
            return false;

        int [] requiredResources = developmentCard.getCostAsArray();

        for(int i=0; i<requiredResources.length; i++)
            requiredResources[i] -= discounts[i];

        return isDevCardLevelSatisfied(developmentCard) && hasResources(requiredResources);
    }

    public Map<Integer, Boolean> getAvailableSpotsForDevCard(DevelopmentCard developmentCard){

        return IntStream.range(0, cardCells.length).boxed().collect(Collectors.toMap(
                position -> position + 1,

                position -> Objects.nonNull(developmentCard) && cardCells[position].isSpotAvailable(developmentCard)
        ));

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

        return hasResources(toar) && isLeaderColorRequirementsSatisfied(leader);

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
     * Returns the resource at a given position in the {{@link PersonalBoard#warehouseLeadersDepots} or {@link PersonalBoard#strongBox}
     * @return the resource at the given position, if present, otherwise returns {@link Resource#EMPTY EMPTY}
     */
    public Resource getResourceAtPosition(int position){
        return  storageUnitFromPos(position).getResourceAt(position);
    }



    /**
     * Return a reduced representation of a {@link Box} as a Map where : <br>
     *
     * - key -> Resource <em>global position</em> in this {@link Box} <br>
     * - value -> Pair where : <br> <br>
     *                      - key -> {@link Resource} int value <br>
     *                      - value -> Map where : <br>
     *
     *                                      -   key ->  {@link Resource} current amount in this Box. <br>
     *                                      -   value -> currently selected resources for production <br> <br>
     *
     *
     *
     * @return reduced representation of a {@link Box}
     */
    public Map<Integer, Pair<Integer, Pair<Integer, Integer>>> getSimpleStrongBox() {

        return IntStream.range(0, strongBox.getNumOfResourcesTypes()).boxed().collect(Collectors.toMap(
                i -> strongBox.globalPositionOfRes(Resource.fromIntFixed(i)),
                i -> new Pair<>(i,  new Pair<>(strongBox.getNumberOf(Resource.fromIntFixed(i)), strongBox.getNSelected(Resource.fromIntFixed(i))))));

    }

    /**
     * Return a reduced representation of a {@link Box} as a Map where :
     *
     * - key -> Resource <em>global position</em> in this {@link Box}
     * - value -> Pair where :
     *                      - key -> {@link Resource} int value
     *                      - value -> {@link Resource} current amount in this Box.
     * @return reduced representation of a {@link Box}
     */
    public Map<Integer, Pair<Integer, Integer>> getSimpleDiscardBox() {

        return IntStream.range(0, discardBox.getNumOfResourcesTypes()).boxed().collect(Collectors.toMap(
                i -> discardBox.globalPositionOfRes(Resource.fromIntFixed(i)),
                i -> new Pair<>(i, discardBox.getNumberOf(Resource.fromIntFixed(i)))));

    }

    public int getPointsFromResources(){
        return Resource.getStream(4).map(this::getNumberOf).reduce(0, Integer::sum);
    }

    public int getPointsFromDevelopmentCards(){
        return Arrays.stream(cardCells).mapToInt(ProductionCardCell::getTotalCellPoints).sum();
    }


    /**
     * Method to get currentlu active {@link DevelopmentCard} required resources discounts, stored inside player's
     * {@link PersonalBoard#discounts} array as positional int values-
     * @return {@link PersonalBoard#discounts} array storing updated values.
     */
    public int[] getDiscounts()
    {
        return discounts;
    }


    /**
     * Method to store inside player's {@link PersonalBoard#discounts} array a new positional {@link Resource} value to discount
     * after the appropriate {@link Leader} <em>effect</em> has been activated.
     * @param discount {@link Pair} containing a Resource to discount when purchasing a {@link DevelopmentCard} from {@link CardShop} as a key
     * and an int as a value indicating the discount amount.
     * Used by leader interface
     */
    public void applyDiscount(Pair<Resource,Integer> discount)
    {
        discounts[discount.getKey().getResourceNumber()]+=discount.getValue();
    }


    public List<Integer> getPosOfChosenResourcesOnInputForProduction(int productionPosition){
        return historyOfInputResourcesForProduction.get(productionPosition);
    }

    public void resetHistoryOfProductionResources(int productionPosition){
        historyOfInputResourcesForProduction.remove(productionPosition);
    }

    public void resetHistoryOfInputResourcesForProduction(){
        historyOfInputResourcesForProduction.clear();
    }

    public int getLastSelectedProductionPosition(){
        return lastSelectedProductionPosition;
    }

    public void setBadFaithToAdd(int amount){
        this.badFaithToAdd = amount;
    }


}
