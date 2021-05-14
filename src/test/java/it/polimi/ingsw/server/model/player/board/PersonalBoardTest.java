package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.cards.production.ProductionCardCell;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class PersonalBoardTest {
    PersonalBoard board;

    @Before
    public void setUp() throws Exception {
        board = new PersonalBoard();
        board.addDevelopmentCardToCell(new DevelopmentCard(
                    0, DevelopmentCardColor.GREEN,
                    new Production(new int[]{2,1},new int[]{0,1})),
                1);
        board.addDevelopmentCardToCell(new DevelopmentCard(
                    0, DevelopmentCardColor.BLUE,
                    new Production(new int[]{20,1,1},new int[]{0,1})),
                2);
        board.addDevelopmentCardToCell(new DevelopmentCard(
                    1, DevelopmentCardColor.PURPLE,
                    new Production(new int[]{1},new int[]{2,0,0,0,1,1,1})),
                1);
        board.addProduction(new Production(new int[]{0,1,1},new int[]{1,0,0,1,1}));
        Pair[] pa = Stream.of(
                new Pair<>(0,Resource.SERVANT),
                new Pair<>(2,Resource.GOLD),
                new Pair<>(5,Resource.SHIELD)
        ).toArray(Pair[]::new);
        board.getWarehouseLeadersDepots().addResources(pa);
        board.getStrongBox().addResources(new int[]{5,15,2});
        board.getDiscardBox().addResources(new int[]{3,2,1,3});
    }

    @Test
    public void setUpCorrectly() {
        //Test Empty
        PersonalBoard emptyBoard = new PersonalBoard();
        assertFalse(emptyBoard.playerHasSixOrMoreCards());
        assertArrayEquals(new Boolean[]{false}, emptyBoard.getSelectedProduction());
        assertArrayEquals(new Boolean[]{false}, emptyBoard.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,true,true},
                emptyBoard.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(0,emptyBoard.getFaithToAdd());
        assertEquals(0,emptyBoard.getBadFaithToAdd());
        assertEquals(Optional.empty(),emptyBoard.firstProductionSelectedWithChoice());
        //Test storage units setup
        assertArrayEquals(new int[]{0,0,0,0},Resource.getStream(4).mapToInt((a)->emptyBoard.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{0,0,0,0},Resource.getStream(4).mapToInt((a)->emptyBoard.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{0,0,0,0},Resource.getStream(4).mapToInt(emptyBoard::getNumberOf).toArray());
        assertEquals(0,emptyBoard.numOfResources());
        assertEquals(
                JsonUtility.toPrettyFormat("{0:[{key:EMPTY,value:false}]," +
                "1:[{key:EMPTY,value:false},{key:EMPTY,value:false}]," +
                "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:EMPTY,value:false}]}"),
                emptyBoard.getWarehouseLeadersDepots().structuredTableJson());


        //Test board
        assertFalse(board.playerHasSixOrMoreCards());
        assertArrayEquals(new Boolean[]{false,false,false,false}, board.getSelectedProduction());
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        //Test depot setup
        assertArrayEquals(new int[]{5,15,2,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{3,2,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{6,16,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertEquals(25,board.numOfResources());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson() );
    }

    @Test
    public void testProduction() {
        board.addProduction(new Production(new int[]{4,8,0,0,0,0,1},new int[]{0,0,1,0,2,3,1}));
        assertArrayEquals(new Boolean[]{true,true,false,true,true}, board.getAvailableProductions());
        //Select first production
        board.selectProductionAt(5);
        board.performChoiceOnInput(-8);
        board.performChoiceOnOutput(Resource.STONE);

        board.getStrongBox().selectN(4,Resource.GOLD);
        board.getStrongBox().selectN(7,Resource.SERVANT);
        board.getWarehouseLeadersDepots().selectResourceAt(0);
        assertArrayEquals(new int[]{5,7,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(
                JsonUtility.toPrettyFormat("{0:[{key:SERVANT,value:true}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());
        assertEquals(0, board.remainingToSelectForProduction());
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        //Select second production
        board.selectProductionAt(2);
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertEquals(1, board.remainingToSelectForProduction());
        board.getWarehouseLeadersDepots().selectResourceAt(2);
        assertEquals(0, board.remainingToSelectForProduction());
        board.performChoiceOnOutput(Resource.STONE);
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertArrayEquals(new int[]{5,7,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:true}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:true}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson() );
        //Production
        board.produce();
        assertArrayEquals(new Boolean[]{false,false,false,false,false}, board.getSelectedProduction());
        assertArrayEquals(new Boolean[]{true,true,false,true,false}, board.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(3,board.getFaithToAdd());
        assertEquals(4,board.getBadFaithToAdd());
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertArrayEquals(new int[]{2,8,3,2},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{2,8,4,2},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertEquals(16,board.numOfResources());
        assertArrayEquals(new int[]{0,0,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:EMPTY,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:EMPTY,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());
    }

    @Test
    public void setFaithToZero() {
        board.addProduction(new Production(new int[]{4,8,0,0,0,0,1},new int[]{1,2,1,3,3,4,1}));
        board.selectProductionAt(5);
        board.performChoiceOnInput(-8);
        board.performChoiceOnOutput(Resource.STONE);
        board.getStrongBox().selectN(4,Resource.GOLD);
        board.getStrongBox().selectN(7,Resource.SERVANT);
        board.getWarehouseLeadersDepots().selectResourceAt(0);

        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        board.produce();
        assertEquals(3,board.getFaithToAdd());
        assertEquals(4,board.getBadFaithToAdd());

        board.setFaithToZero();
        assertEquals(0,board.getFaithToAdd());
        assertEquals(4,board.getBadFaithToAdd());
    }

    @Test
    public void setBadFaithToZero() {
        board.addProduction(new Production(new int[]{4,8,0,0,0,0,1},new int[]{1,2,1,3,5,5}));
        board.selectProductionAt(5);
        board.performChoiceOnOutput(Resource.STONE);
        board.getStrongBox().selectN(4,Resource.GOLD);
        board.getStrongBox().selectN(7,Resource.SERVANT);
        board.getWarehouseLeadersDepots().selectResourceAt(0);

        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        board.produce();
        assertEquals(5,board.getFaithToAdd());
        assertEquals(5,board.getBadFaithToAdd());

        board.setBadFaithToZero();
        assertEquals(5,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
    }

    @Test
    public void testChoices() {
        board.selectProductionAt(0);
        //Perform choice on input
        board.performChoiceOnInput(-7);
        assertArrayEquals(new Boolean[]{true,false,false,false}, board.getSelectedProduction());
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertArrayEquals(new int[]{5,15,2,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{3,2,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{6,16,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertEquals(25,board.numOfResources());
        assertArrayEquals(new int[]{0,1,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());
        //Perform choice on input
        board.performChoiceOnInput(2);
        assertArrayEquals(new Boolean[]{true,false,false,false}, board.getSelectedProduction());
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertArrayEquals(new int[]{5,15,2,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{3,2,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{6,16,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertEquals(25,board.numOfResources());
        assertArrayEquals(new int[]{0,1,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:true}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());
        //Perform choice on output
        board.performChoiceOnOutput(Resource.STONE);
        assertArrayEquals(new Boolean[]{true,false,false,false}, board.getSelectedProduction());
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        board.selectProductionAt(2);
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertArrayEquals(new int[]{5,15,2,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{3,2,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{6,16,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertEquals(25,board.numOfResources());
        assertArrayEquals(new int[]{0,1,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:true}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());
        //Perform choice on output
        board.performChoiceOnOutput(Resource.GOLD);
        assertArrayEquals(new Boolean[]{true,true,false,false}, board.getSelectedProduction());
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        assertArrayEquals(
                new Boolean[]{true,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        assertArrayEquals(new int[]{5,15,2,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{3,2,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{6,16,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertEquals(25,board.numOfResources());
        assertArrayEquals(new int[]{0,1,0,0},Resource.getStream(4).mapToInt((a)->board.getStrongBox().getNSelected(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:GOLD,value:true}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());
    }

    @Test
    public void resetChoices(){
        board.selectProductionAt(0);
        board.performChoiceOnInput(-8);
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        board.performChoiceOnOutput(Resource.STONE);
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        board.resetChoices();
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        board.performChoiceOnInput(-8);
        board.performChoiceOnInput(-6);
        board.performChoiceOnOutput(Resource.STONE);
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        board.selectProductionAt(2);
        assertNotEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
        board.performChoiceOnOutput(Resource.GOLD);
        assertEquals(Optional.empty(),board.firstProductionSelectedWithChoice());
    }

    @Test
    public void getAvailableProductions() {
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        board.getStrongBox().addResources(new int[]{100,0,0,0});
        assertArrayEquals(new Boolean[]{true,true,true,true}, board.getAvailableProductions());
        board.getStrongBox().removeResources(new int[]{105,0,0,0});
        assertArrayEquals(new Boolean[]{true,true,false,true}, board.getAvailableProductions());
        board.getWarehouseLeadersDepots().removeResource(2);
        assertArrayEquals(new Boolean[]{true,false,false,true}, board.getAvailableProductions());
        board.getStrongBox().removeResources(new int[]{0,15,1,0});
        assertArrayEquals(new Boolean[]{true,false,false,true}, board.getAvailableProductions());
        board.getWarehouseLeadersDepots().removeResource(0);
        assertEquals(2,board.numOfResources());
        assertArrayEquals(new Boolean[]{true,false,false,false}, board.getAvailableProductions());
        board.getStrongBox().removeResources(new int[]{0,0,1,0});
        assertEquals(1,board.numOfResources());
        assertArrayEquals(new Boolean[]{false,false,false,false}, board.getAvailableProductions());
    }



    @Test
    public void testProductionSelection() {
        board.addProduction(new Production(new int[]{1,2},new int[]{0,1}));
        board.addProduction(new Production(new int[]{0,1,1},new int[]{1}));
        assertArrayEquals(new Boolean[]{false,false,false,false,false,false}, board.getSelectedProduction());
        board.toggleSelectProductionAt(0);
        assertArrayEquals(new Boolean[]{true,false,false,false,false,false}, board.getSelectedProduction());
        board.selectProductionAt(2);
        assertArrayEquals(new Boolean[]{true,true,false,false,false,false}, board.getSelectedProduction());
        board.toggleSelectProductionAt(2);
        board.toggleSelectProductionAt(6);
        assertArrayEquals(new Boolean[]{true,false,false,false,false,true}, board.getSelectedProduction());
        board.deselectProductionAt(3);
        board.selectProductionAt(2);
        board.selectProductionAt(5);
        board.selectProductionAt(6);
        assertArrayEquals(new Boolean[]{true,true,false,false,true,true}, board.getSelectedProduction());
        board.resetSelectedProductions();
        assertArrayEquals(new Boolean[]{false,false,false,false,false,false}, board.getSelectedProduction());
    }


    @Test
    public void addProduction() {
        Production p = new Production(new int []{},new int[]{0,0,0,0,0,0,1});
        board.addProduction(p);
        assertEquals(5, board.getSelectedProduction().length);
        board.performChoiceOnInput(-9);
        board.performChoiceOnInput(-7);
        board.performChoiceOnOutput(Resource.STONE);
        board.performChoiceOnOutput(Resource.GOLD);
        assertNotEquals(Optional.of(p),board.firstProductionSelectedWithChoice());
        board.selectProductionAt(5);
        assertEquals(Optional.of(p),board.firstProductionSelectedWithChoice());
        assertArrayEquals(p.getOutputs(),board.firstProductionSelectedWithChoice().get().getOutputs());
    }

    @Test
    public void addDevelopmentCardToCell() {
        Production p = new Production(new int []{},new int[]{0,0,0,0,0,0,1});
        DevelopmentCard card4 = new DevelopmentCard(1,DevelopmentCardColor.GREEN,p);
        board.addDevelopmentCardToCell(card4,0);
        assertArrayEquals(
                new Boolean[]{false,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertFalse(board.playerHasSixOrMoreCards());
        DevelopmentCard addedCard = board.getCardCells().get(0).getFrontCard();
        assertEquals(card4,addedCard);

        DevelopmentCard card5 = new DevelopmentCard(1,DevelopmentCardColor.BLUE,p);
        DevelopmentCard card6 = new DevelopmentCard(2,DevelopmentCardColor.PURPLE,p);
        DevelopmentCard card7 = new DevelopmentCard(2,DevelopmentCardColor.GREEN,p);
        board.addDevelopmentCardToCell(card5,2);
        assertFalse(board.playerHasSixOrMoreCards());
        board.addDevelopmentCardToCell(card6,1);
        assertTrue(board.playerHasSixOrMoreCards());
        board.addDevelopmentCardToCell(card7,0);
        assertTrue(board.playerHasSixOrMoreCards());
        assertArrayEquals(
                new Boolean[]{false,false,false},
                board.getCardCells().stream().map(ProductionCardCell::getFrontCard).map(Objects::isNull).toArray());
        assertTrue(board.playerHasSixOrMoreCards());
        DevelopmentCard addedCard0 = board.getCardCells().get(0).getFrontCard();
        assertEquals(card7,addedCard0);
        assertTrue(board.playerHasSixOrMoreCards());
        DevelopmentCard addedCard1 = board.getCardCells().get(1).getFrontCard();
        assertEquals(card6,addedCard1);
        assertTrue(board.playerHasSixOrMoreCards());
        DevelopmentCard addedCard2 = board.getCardCells().get(2).getFrontCard();
        assertEquals(card5,addedCard2);

    }

    @Test
    public void discardResources() {
        Box discardBox = Box.discardBox();
        discardBox.addResources(new int[]{12,3,4,5,7});
        //Set marketBox
        board.setMarketBox(discardBox);
        assertEquals(discardBox,board.getDiscardBox());
        assertArrayEquals(new int[]{12,3,4,5,7},Resource.getStream(5).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertArrayEquals(new int[]{0,0,0,0,0},Resource.getStream(5).mapToInt((a)->board.getDiscardBox().getNSelected(a)).toArray());
        assertEquals(0,board.getFaithToAdd());
        assertEquals(0,board.getBadFaithToAdd());

        discardBox.removeResources(new int[]{2,0,1,0});
        assertArrayEquals(new int[]{10,3,3,5,7},Resource.getStream(5).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        //Discard market box
        board.discardResources();
        assertEquals(7,board.getFaithToAdd());
        assertEquals(21,board.getBadFaithToAdd());
    }


    @Test
    public void testStorageUnitFromPos() {
        for (int i=-8;i<6;i++){
            if (i<-4)
                assertEquals(board.getStrongBox(),board.storageUnitFromPos(i));
            else if (i<0)
                assertEquals(board.getDiscardBox(),board.storageUnitFromPos(i));
            else assertEquals(board.getWarehouseLeadersDepots(),board.storageUnitFromPos(i));
        }
    }

    @Test
    public void testMove() {
        //From box to warehouse
        board.move(-4,1);
        assertArrayEquals(new int[]{7,16,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertArrayEquals(new int[]{2,2,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SERVANT,value:false}]," +
                        "1:[{key:GOLD,value:false},{key:GOLD,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());

        //From warehouse to box
        board.move(0,-3);
        assertArrayEquals(new int[]{7,15,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertArrayEquals(new int[]{2,3,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:EMPTY,value:false}]," +
                        "1:[{key:GOLD,value:false},{key:GOLD,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:SHIELD,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());

        //Between Warehouse
        board.move(5,0);
        assertArrayEquals(new int[]{7,15,3,0},Resource.getStream(4).mapToInt(board::getNumberOf).toArray());
        assertArrayEquals(new int[]{2,3,1,3},Resource.getStream(4).mapToInt((a)->board.getDiscardBox().getNumberOf(a)).toArray());
        assertEquals(JsonUtility.toPrettyFormat(
                "{0:[{key:SHIELD,value:false}]," +
                        "1:[{key:GOLD,value:false},{key:GOLD,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:EMPTY,value:false}]}"),
                board.getWarehouseLeadersDepots().structuredTableJson());

    }

}