package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardColor;
import it.polimi.ingsw.server.model.ProductionCardCell;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductionCardCellTest
{

    @Test
    public void getFrontCard()
    {
        ProductionCardCell testCardCell_gFC = new ProductionCardCell();
        DevelopmentCard testCardOne_gFC;

        for(int i=0; i<testCardCell_gFC.getMaxsize()-1; i++)
        {
            testCardOne_gFC= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
            testCardCell_gFC.addToTop(testCardOne_gFC);
            assertEquals(testCardOne_gFC, testCardCell_gFC.getFrontCard());
        }


    }

    @Test
    public void isSpotAvailable()
    {
        ProductionCardCell testCardCell_iSA = new ProductionCardCell();
        DevelopmentCard testCardOne_iSA;

        for(int i=0; i<testCardCell_iSA.getMaxsize()-1; i++)
        {
            testCardOne_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
            testCardCell_iSA.addToTop(testCardOne_iSA);
            assertTrue(testCardCell_iSA.isSpotAvailable());
        }
        testCardOne_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardOne_iSA);
        assertFalse(testCardCell_iSA.isSpotAvailable());


    }


    @Test
    public void addToTop()
    {
        ProductionCardCell testCardCell_aTT = new ProductionCardCell();
        assertEquals(0,testCardCell_aTT.stackedCards.size());
        DevelopmentCard testCardOne_aTT;

        for(int i=0; i<testCardCell_aTT.getMaxsize()-1; i++)
        {
            testCardOne_aTT= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
            testCardCell_aTT.addToTop(testCardOne_aTT);
            assertTrue(testCardCell_aTT.isSpotAvailable());
            assertEquals(i+1,testCardCell_aTT.stackedCards.size());
        }




    }
}