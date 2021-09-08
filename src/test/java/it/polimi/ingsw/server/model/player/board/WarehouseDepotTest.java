package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class WarehouseDepotTest {
    private WarehouseDepot warehouseDepot;

    @Before
    public void setUp() {
        warehouseDepot = new WarehouseDepot(3,0);
    }

    @Test
    public void correctlyConstructed() {
        assertArrayEquals(new Boolean[]{false,false,false},getSelectedS(warehouseDepot));
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY},getResources(warehouseDepot));
        assertArrayEquals(new int[]{0,1,2},warehouseDepot.availableSpotsFor(Resource.GOLD).toArray());
        assertEquals(0,warehouseDepot.getOccupiedSpotsInDepotNum());
    }
    
    @Test
    public void testAddAndRemove() {

        //Add first resource
        warehouseDepot.addResource(new Pair<>(0,Resource.SERVANT));
        warehouseDepot.toggleSelected(0);
        assertArrayEquals(new int[]{1,2},warehouseDepot.availableSpotsFor(Resource.SERVANT).toArray());
        assertEquals(1,warehouseDepot.getOccupiedSpotsInDepotNum());
        assertArrayEquals(new Resource[]{Resource.SERVANT,Resource.EMPTY,Resource.EMPTY},getResources(warehouseDepot));
        assertArrayEquals(new Boolean[]{true,false,false},getSelectedS(warehouseDepot));
        assertArrayEquals(new int[]{},warehouseDepot.availableSpotsFor(Resource.GOLD).toArray());

        //Remove 1 resource
        warehouseDepot.removeResource(0);
        assertArrayEquals(new Boolean[]{false,false,false},getSelectedS(warehouseDepot));
        assertArrayEquals(new Resource[]{Resource.EMPTY,Resource.EMPTY,Resource.EMPTY},getResources(warehouseDepot));
        assertArrayEquals(new int[]{0,1,2},warehouseDepot.availableSpotsFor(Resource.GOLD).toArray());
        assertArrayEquals(new int[]{0,1,2},warehouseDepot.availableSpotsFor(Resource.SERVANT).toArray());
        assertEquals(0,warehouseDepot.getOccupiedSpotsInDepotNum());

        //Add all the resources
        warehouseDepot.addResource(new Pair<>(2,Resource.STONE));
        warehouseDepot.addResource(new Pair<>(0,Resource.STONE));
        warehouseDepot.addResource(new Pair<>(1,Resource.STONE));
        assertArrayEquals(new int[]{},warehouseDepot.availableSpotsFor(Resource.STONE).toArray());
        assertEquals(3,warehouseDepot.getOccupiedSpotsInDepotNum());
        assertArrayEquals(new Resource[]{Resource.STONE,Resource.STONE,Resource.STONE},getResources(warehouseDepot));
        assertArrayEquals(new Boolean[]{false,false,false},getSelectedS(warehouseDepot));
        assertArrayEquals(new int[]{},warehouseDepot.availableSpotsFor(Resource.GOLD).toArray());
    }

    private Resource[] getResources(Depot dep) {
        return IntStream.range(dep.getLastGlobalPosition()-dep.getSize()+1,dep.getLastGlobalPosition()+1)
                .mapToObj(dep::getAtGPos)
                .map(Pair::getKey)
                .toArray(Resource[]::new);
    }

    private Boolean[] getSelectedS(Depot dep) {
        return IntStream
                .range(dep.getLastGlobalPosition()-dep.getSize()+1,dep.getLastGlobalPosition()+1)
                .mapToObj(dep::getAtGPos)
                .map(Pair::getValue)
                .toArray(Boolean[]::new);
    }
}