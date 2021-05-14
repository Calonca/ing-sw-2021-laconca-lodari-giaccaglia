package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.utils.Util;
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
}