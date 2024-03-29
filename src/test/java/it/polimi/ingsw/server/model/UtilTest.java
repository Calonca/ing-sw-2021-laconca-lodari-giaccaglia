package it.polimi.ingsw.server.model;

import it.polimi.ingsw.network.util.Util;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void sumArray() {
        int[] a = {0,1,2,3};
        int[] b = {9,3,-3,3,8};
        int[] c = {};

        assertArrayEquals(new int[]{9,4}, Util.sumArray(a,b,2));
        assertArrayEquals(new int[]{9,4,-1,6,8,0,0,0},Util.sumArray(b,a,8));
        assertArrayEquals(new int[]{},Util.sumArray(a,c,0));
        assertArrayEquals(new int[]{0,1,2,3,0,0},Util.sumArray(a,c,6));
    }

    @Test
    public void testResourcesToChooseOnSetup(){

        assertEquals(0,Util.resourcesToChooseOnSetup(0));
        assertEquals(1,Util.resourcesToChooseOnSetup(1));
        assertEquals(1,Util.resourcesToChooseOnSetup(2));
        assertEquals(2,Util.resourcesToChooseOnSetup(3));

    }

    @Test
    public void testInitialFaithPoints(){

        assertEquals(0,Util.initialFaithPoints(0));
        assertEquals(0,Util.initialFaithPoints(1));
        assertEquals(1,Util.initialFaithPoints(2));
        assertEquals(1,Util.initialFaithPoints(3));

    }

}