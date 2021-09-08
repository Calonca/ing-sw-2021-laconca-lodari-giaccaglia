package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class DepotTest {
    private Depot depotGold,depotEmpty;

    @Before
    public void setUp() {
        depotGold = new Depot(4,10, Resource.GOLD) {};
        depotEmpty = new Depot(3,0, Resource.EMPTY) {};
    }

    @Test
    public void correctlyConstructed() {
        assertArrayEquals(new Boolean[]{false,false,false,false},getSelectedS(depotGold));
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY,Resource.EMPTY},getResources(depotGold));
        assertArrayEquals(new int[]{},depotGold.availableSpotsFor(Resource.SHIELD).toArray());
        assertArrayEquals(new int[]{10,11,12,13},depotGold.availableSpotsFor(Resource.GOLD).toArray());
        assertEquals(0,depotGold.getOccupiedSpotsInDepotNum());

        assertArrayEquals(new Boolean[]{false,false,false},getSelectedS(depotEmpty));
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY},getResources(depotEmpty));
        assertArrayEquals(new int[]{0,1,2},depotEmpty.availableSpotsFor(Resource.GOLD).toArray());
        assertEquals(0,depotEmpty.getOccupiedSpotsInDepotNum());
    }

    @Test
    public void getSize() {
        assertEquals(4,depotGold.getSize());
        assertEquals(3,depotEmpty.getSize());
    }

    @Test
    public void getLastGlobalPosition() {
        assertEquals(13,depotGold.getLastGlobalPosition());
        assertEquals(2,depotEmpty.getLastGlobalPosition());
    }

    @Test
    public void testAddAndRemove() {
        //depotGoldTest
        //Add first resource
        depotGold.addResource(new Pair<>(13,Resource.GOLD));
        depotGold.toggleSelected(13);
        assertArrayEquals(new int[]{10,11,12},depotGold.availableSpotsFor(Resource.GOLD).toArray());
        assertEquals(1,depotGold.getOccupiedSpotsInDepotNum());
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY,Resource.GOLD},getResources(depotGold));
        assertArrayEquals(new Boolean[]{false,false,false,true},getSelectedS(depotGold));

        //Remove 1 resource
        depotGold.removeResource(13);
        assertArrayEquals(new Boolean[]{false,false,false,false},getSelectedS(depotGold));
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY,Resource.EMPTY},getResources(depotGold));
        assertArrayEquals(new int[]{},depotGold.availableSpotsFor(Resource.SHIELD).toArray());
        assertArrayEquals(new int[]{10,11,12,13},depotGold.availableSpotsFor(Resource.GOLD).toArray());
        assertEquals(0,depotGold.getOccupiedSpotsInDepotNum());
        //Add all the resources
        depotGold.addResource(new Pair<>(13,Resource.GOLD));
        depotGold.addResource(new Pair<>(11,Resource.GOLD));
        depotGold.addResource(new Pair<>(12,Resource.GOLD));
        depotGold.addResource(new Pair<>(10,Resource.GOLD));
        assertArrayEquals(new int[]{},depotGold.availableSpotsFor(Resource.GOLD).toArray());
        assertEquals(4,depotGold.getOccupiedSpotsInDepotNum());
        assertArrayEquals(new Resource[]{Resource.GOLD,Resource.GOLD,Resource.GOLD,Resource.GOLD},getResources(depotGold));
        assertArrayEquals(new Boolean[]{false,false,false,false},getSelectedS(depotGold));

        //depotEmptyTest
        //Add first resource
        depotEmpty.addResource(new Pair<>(0,Resource.SERVANT));
        depotEmpty.toggleSelected(0);
        assertArrayEquals(new int[]{1,2},depotEmpty.availableSpotsFor(Resource.SERVANT).toArray());
        assertEquals(1,depotEmpty.getOccupiedSpotsInDepotNum());
        assertArrayEquals(new Resource[]{Resource.SERVANT,Resource.EMPTY,Resource.EMPTY},getResources(depotEmpty));
        assertArrayEquals(new Boolean[]{true,false,false},getSelectedS(depotEmpty));
        assertArrayEquals(new int[]{},depotEmpty.availableSpotsFor(Resource.GOLD).toArray());
        //Remove 1 resource
        depotEmpty.removeResource(0);
        assertArrayEquals(new Boolean[]{false,false,false},getSelectedS(depotEmpty));
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY},getResources(depotEmpty));
        assertArrayEquals(new int[]{},depotEmpty.availableSpotsFor(Resource.GOLD).toArray());
        assertArrayEquals(new int[]{0,1,2},depotEmpty.availableSpotsFor(Resource.SERVANT).toArray());
        assertEquals(0,depotEmpty.getOccupiedSpotsInDepotNum());
        //Add all the resources
        depotEmpty.addResource(new Pair<>(2,Resource.SERVANT));
        depotEmpty.addResource(new Pair<>(0,Resource.SERVANT));
        depotEmpty.addResource(new Pair<>(1,Resource.SERVANT));
        assertArrayEquals(new int[]{},depotEmpty.availableSpotsFor(Resource.SERVANT).toArray());
        assertEquals(3,depotEmpty.getOccupiedSpotsInDepotNum());
        assertArrayEquals(new Resource[]{Resource.SERVANT,Resource.SERVANT,Resource.SERVANT},getResources(depotEmpty));
        assertArrayEquals(new Boolean[]{false,false,false},getSelectedS(depotEmpty));
        assertArrayEquals(new int[]{},depotEmpty.availableSpotsFor(Resource.GOLD).toArray());
    }

    @Test
    public void testSelection() {
        depotEmpty.addResource(new Pair<>(1,Resource.GOLD));
        assertEquals(false,depotEmpty.getAtGPos(1).getValue());
        depotEmpty.setSelected(true,1);
        assertEquals(true,depotEmpty.getAtGPos(1).getValue());
        depotEmpty.setSelected(false,1);
        assertEquals(false,depotEmpty.getAtGPos(1).getValue());
        depotEmpty.toggleSelected(1);
        assertEquals(true,depotEmpty.getAtGPos(1).getValue());
        depotEmpty.toggleSelected(1);
        assertEquals(false,depotEmpty.getAtGPos(1).getValue());
    }

    @Test
    public void getNumberOf(){
        assertArrayEquals(new int[]{0,0,0,0,0},Resource.getStream(5).mapToInt((res)->depotGold.getNumberOf(res)).toArray());
        depotGold.addResource(new Pair<>(13,Resource.GOLD));
        assertArrayEquals(new int[]{1,0,0,0,0},Resource.getStream(5).mapToInt((res)->depotGold.getNumberOf(res)).toArray());
        depotGold.addResource(new Pair<>(12,Resource.GOLD));
        assertArrayEquals(new int[]{2,0,0,0,0},Resource.getStream(5).mapToInt((res)->depotGold.getNumberOf(res)).toArray());

        assertArrayEquals(new int[]{0,0,0,0},Resource.getStream(4).mapToInt((res)->depotEmpty.getNumberOf(res)).toArray());
        depotEmpty.addResource(new Pair<>(1,Resource.STONE));
        assertArrayEquals(new int[]{0,0,0,1},Resource.getStream(4).mapToInt((res)->depotEmpty.getNumberOf(res)).toArray());
        depotEmpty.addResource(new Pair<>(2,Resource.STONE));
        assertArrayEquals(new int[]{0,0,0,2},Resource.getStream(4).mapToInt((res)->depotEmpty.getNumberOf(res)).toArray());

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void whenExceptionInGlobalPositionFromLocalThrown_thenExpectationSatisfied(){
        depotGold.getSelected(110);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void whenExceptionInGlobalToLocalPosThrown_thenExpectationSatisfied(){
        depotGold.getSelected(110);
    }

    private Resource[] getResources(Depot dep) {
        return IntStream.range(dep.getLastGlobalPosition()-dep.getSize()+1,dep.getLastGlobalPosition()+1).mapToObj(dep::getAtGPos).map(Pair::getKey).toArray(Resource[]::new);
    }

    private Boolean[] getSelectedS(Depot dep) {
        return IntStream.range(dep.getLastGlobalPosition()-dep.getSize()+1,dep.getLastGlobalPosition()+1).mapToObj(dep::getAtGPos).map(Pair::getValue).toArray(Boolean[]::new);
    }

}