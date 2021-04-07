package it.polimi.ingsw.server.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductionTest {

   // @Test
   // public void isAvailable() {
   // }

    @Test
    public void choiceCanBeMade()
    {
        Production productiontest;
        productiontest= new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1});
        assertTrue(productiontest.choiceCanBeMade());
        assertTrue(productiontest.choiceCanBeMadeOnInput());
        assertTrue(productiontest.choiceCanBeMadeOnOutput());
        productiontest= new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,0,0});
        assertTrue(productiontest.choiceCanBeMade());
        assertTrue(productiontest.choiceCanBeMadeOnInput());
        assertFalse(productiontest.choiceCanBeMadeOnOutput());
        productiontest= new Production(new int[]{0,0,0,0,0,0,0},new int[]{0,0,0,0,0,1,1});
        assertTrue(productiontest.choiceCanBeMade());
        assertFalse(productiontest.choiceCanBeMadeOnInput());
        assertTrue(productiontest.choiceCanBeMadeOnOutput());
        productiontest= new Production(new int[]{0,0,0,0,0,0,0},new int[]{0,0,0,0,0,0,0});
        assertFalse(productiontest.choiceCanBeMade());
        assertFalse(productiontest.choiceCanBeMadeOnInput());
        assertFalse(productiontest.choiceCanBeMadeOnOutput());


    }


    @Test
    public void performChoiceOnOutput()
    {
        Production productiontest;
        Resource resource= Resource.GOLD;
        productiontest= new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1});
        productiontest.performChoiceOnOutput(resource);
        assertArrayEquals(new int[]{1,0,0,0,0,1,0}, productiontest.getOutputs());
    }


    @Test
    public void resetchoice()
    {
        Production productiontest= Production.basicProduction();
        productiontest.performChoiceOnOutput(Resource.FAITH);
        productiontest.resetchoice();
        assertArrayEquals(new int[]{0,0,0,0,0,1,1}, productiontest.getOutputs());
    }
}