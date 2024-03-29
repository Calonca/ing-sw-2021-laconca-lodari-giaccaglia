package it.polimi.ingsw.server.model.player.board;

import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.*;

public class BoxTest {

    private Box strongBox,box3;

    @Before
    public void setUp() {
        strongBox = Box.strongBox();
        box3 = new Box(3,-12);
        box3.addResources(new int[]{5,15,2});
    }

    @Test
    public void strongBoxSetCorrectly() {
        Resource.getStream(Resource.N_RES).forEach((type)->assertEquals(0, strongBox.getNumberOf(type)));
    }

    @Test
    public void discardBoxSetCorrectly() {
        Box discardBox = Box.discardBox();
        Resource.getStream(Resource.N_RES +1).forEach((type)->assertEquals(0, discardBox.getNumberOf(type)));
    }

    @Test
    public void boxSetCorrectly() {
        final int[] testList = {0,1,4,20};
        for (int a:testList){
            Box b = new Box(a,a/2);
            Resource.getStream(a).forEach((type)->assertEquals(b.getNumberOf(type),0));
        }
        assertArrayEquals(new int[]{5,15,2},Resource.getStream(3).mapToInt(box3::getNumberOf).toArray());
    }

    @Test
    public void removeResource() {
        box3.removeResource(-11);
        box3.removeResource(-10);
        assertArrayEquals(new int[]{5,14,1},Resource.getStream(3).mapToInt(box3::getNumberOf).toArray());
    }

    @Test
    public void removeResources() {
        int[] a = {12,3,0};
        int[] b = {22,2};
        box3.removeResources(a);
        box3.removeResources(b);
        assertArrayEquals(new int[]{0,10,2},Resource.getStream(3).mapToInt(box3::getNumberOf).toArray());
    }

    @Test
    public void addResource() {
        box3.addResource(new Pair<>(-12,Resource.GOLD));
        assertArrayEquals(new int[]{6,15,2},Resource.getStream(3).mapToInt(box3::getNumberOf).toArray());
        box3.addResource(new Pair<>(-10,Resource.SHIELD));
        assertArrayEquals(new int[]{6,15,3},Resource.getStream(3).mapToInt(box3::getNumberOf).toArray());
    }

    @Test
    public void addResources() {
        Box box = new Box(5,0);
        int[] a = {1,0,23,4,0};
        int[] b = {24,11,9};
        box.addResources(a);
        assertArrayEquals(a,Resource.getStream(5).mapToInt(box::getNumberOf).toArray());
        box.addResources(b);
        assertArrayEquals(new int[]{25,11,32,4,0},Resource.getStream(5).mapToInt(box::getNumberOf).toArray());
    }

    @Test
    public void getResourceAtTest() {
        Box box = new Box(6,-10);
        box.addResources(new int[]{0,2,4,0,2,3});
        assertArrayEquals(
                new int[]{7,1,2,7,4,5},
                Resource.getStream(6)
                        .mapToInt(Resource::getResourceNumber)
                        .map((a)->a-10).mapToObj(box::getResourceAt)
                        .mapToInt(Resource::getResourceNumber).toArray());
    }

    @Test
    public void getResourceAt() {
        box3.addResources(new int[]{1,1,1});
        assertArrayEquals(
                Stream.concat(Resource.getStream(3),Stream.generate(()->Resource.EMPTY).limit(108)).toArray(),
                Resource.getStream(111)
                        .map((res)->box3.getResourceAt(res.getResourceNumber()-12)).toArray()
        );
    }

    @Test
    public void testSelection() {
        Box box = new Box(3,6);
        int[] a = {11,2,0};
        box.addResources(a);
        box.selectN(20, Resource.GOLD);
        box.selectResourceAt(6);
        assertEquals(11,box.getTotalSelected());
        box.deselectN(99, Resource.SERVANT);
        box.deselectResourceAt(7);
        assertEquals(0,box.getNSelected(Resource.SERVANT));
        box.selectN(1, Resource.SERVANT);
        assertEquals(12,box.getTotalSelected());
        box.selectN(10,Resource.SHIELD);

        assertEquals(11,box.getNSelected(Resource.GOLD));
        assertEquals(1,box.getNSelected(Resource.SERVANT));
        assertEquals(0,box.getNSelected(Resource.SHIELD));
    }

    @Test
    public void removeSelected() {
        Box box = new Box(5,9);
        int[] a = {5,10,23,40,0};
        box.addResources(a);
        box.selectN(5,Resource.GOLD);
        box.selectN(11, Resource.SHIELD);
        box.removeSelected();
        assertArrayEquals( new int[]{0,10,12,40,0},Resource.getStream(5).mapToInt(box::getNumberOf).toArray());
    }

}