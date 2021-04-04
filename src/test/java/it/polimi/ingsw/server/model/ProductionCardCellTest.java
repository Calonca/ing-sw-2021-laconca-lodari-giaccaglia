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
        ProductionCardCell testCardCell_iSA = new ProductionCardCell();

        DevelopmentCard testCardOne_gFC= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardOne_gFC);
        assertEquals(testCardOne_gFC, testCardCell_iSA.getFrontCard());

        DevelopmentCard testCardTwo_gFC= new DevelopmentCard(1, DevelopmentCardColor.YELLOW);
        testCardCell_iSA.addToTop(testCardTwo_gFC);
        assertEquals(testCardTwo_gFC, testCardCell_iSA.getFrontCard());

        DevelopmentCard testCardThree_gFC= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardThree_gFC);
        assertEquals(testCardThree_gFC, testCardCell_iSA.getFrontCard());


    }

    @Test
    public void isSpotAvailable()
    {
        ProductionCardCell testCardCell_iSA = new ProductionCardCell();

        DevelopmentCard testCardOne_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardOne_iSA);
        assertTrue(testCardCell_iSA.isSpotAvailable());
        assertEquals(testCardOne_iSA, testCardCell_iSA.getFrontCard());

        DevelopmentCard testCardTwo_iSA= new DevelopmentCard(1, DevelopmentCardColor.YELLOW);
        testCardCell_iSA.addToTop(testCardTwo_iSA);
        assertTrue(testCardCell_iSA.isSpotAvailable());

        DevelopmentCard testCardThree_iSA= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_iSA.addToTop(testCardThree_iSA);
        assertTrue(testCardCell_iSA.isSpotAvailable());


    }


    @Test
    public void addToTop()
    {
        ProductionCardCell testCardCell_aTT = new ProductionCardCell();
        assertEquals(0,testCardCell_aTT.stackedCards.size());

        DevelopmentCard testCardOne_aTT= new DevelopmentCard(1, DevelopmentCardColor.GREEN);
        testCardCell_aTT.addToTop(testCardOne_aTT);
        assertEquals(1,testCardCell_aTT.stackedCards.size());

        DevelopmentCard testCardTwo_aTT= new DevelopmentCard(1, DevelopmentCardColor.BLUE);
        testCardCell_aTT.addToTop(testCardTwo_aTT);
        assertEquals(2,testCardCell_aTT.stackedCards.size());

        DevelopmentCard testCardThree_aTT= new DevelopmentCard(1, DevelopmentCardColor.PURPLE);
        testCardCell_aTT.addToTop(testCardThree_aTT);
        assertEquals(3,testCardCell_aTT.stackedCards.size());



    }
}