package it.polimi.ingsw.client.model;

import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardColor;
import it.polimi.ingsw.server.model.ProductionCardCell;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductionCardCellTest
{


    @Test
    public void isSpotAvailable()
    {
        ProductionCardCell testCardCell_iSA = new ProductionCardCell();

        DevelopmentCard testCardOne_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardOne_iSA);
        assertTrue(testCardCell_iSA.isSpotAvailable());

        DevelopmentCard testCardTwo_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardTwo_iSA);
        assertTrue(testCardCell_iSA.isSpotAvailable());

        DevelopmentCard testCardThree_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardThree_iSA);
        assertTrue(testCardCell_iSA.isSpotAvailable());

        DevelopmentCard testCardFour_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardFour_iSA);
        assertFalse(testCardCell_iSA.isSpotAvailable());
    }


    @Test
    public void addToTop()
    {
        ProductionCardCell testCardCell_aTT = new ProductionCardCell();
        assertEquals(0,testCardCell_aTT.stackHeight);

        DevelopmentCard testCardOne_aTT= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_aTT.addToTop(testCardOne_aTT);
        assertEquals(1,testCardCell_aTT.stackHeight);

        DevelopmentCard testCardTwo_aTT= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_aTT.addToTop(testCardTwo_aTT);
        assertEquals(2,testCardCell_aTT.stackHeight);

        DevelopmentCard testCardThree_aTT= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_aTT.addToTop(testCardThree_aTT);
        assertEquals(3,testCardCell_aTT.stackHeight);



    }
}