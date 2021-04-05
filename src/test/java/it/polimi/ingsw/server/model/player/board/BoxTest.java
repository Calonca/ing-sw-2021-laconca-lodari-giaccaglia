package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BoxTest {

    @Test
    public void strongBoxSetCorrectly() {
        Box b = new Box();
        Resource.getStream(Resource.nRes).forEach((type)->assertEquals(b.getNumberOf(type),0));
    }

    @Test
    public void boxSetCorrectly() {
        final int[] testList = {0,1,4,20};
        for (int a:testList){
            Box b = new Box(a);
            Resource.getStream(a).forEach((type)->assertEquals(b.getNumberOf(type),0));
        }
    }

    @Test
    public void removeResources() {
        Box box = new Box(3);
        int[] a = {12,3,0};
        int[] b = {22,2};
        box.addResources(a);
        box.removeResources(b);
        assertArrayEquals(Resource.getStream(3).mapToInt(box::getNumberOf).toArray(), new int[]{0,1,0});
    }

    @Test
    public void addResources() {
        Box box = new Box(5);
        int[] a = {1,0,23,4,0};
        int[] b = {24,11,9};
        box.addResources(a);
        assertArrayEquals(Resource.getStream(5).mapToInt(box::getNumberOf).toArray(), a);
        box.addResources(b);
        assertArrayEquals(Resource.getStream(5).mapToInt(box::getNumberOf).toArray(), new int[]{25,11,32,4,0});
    }

    @Test
    public void testSelection() {
        Box box = new Box(3);
        int[] a = {11,2,0};
        box.addResources(a);
        box.selectN(21, Resource.GOLD);
        box.deselectN(100, Resource.SERVANT);
        box.selectN(1, Resource.SERVANT);
        box.selectN(10,Resource.SHIELD);

        assertEquals(11,box.getNSelected(Resource.GOLD));
        assertEquals(1,box.getNSelected(Resource.SERVANT));
        assertEquals(0,box.getNSelected(Resource.SHIELD));
    }

    @Test
    public void removeSelected() {
        Box box = new Box(5);
        int[] a = {5,10,23,40,0};
        box.addResources(a);
        box.selectN(5,Resource.GOLD);
        box.selectN(11, Resource.SHIELD);
        box.removeSelected();
        assertArrayEquals(Resource.getStream(5).mapToInt(box::getNumberOf).toArray(), new int[]{0,10,12,40,0});
    }

}