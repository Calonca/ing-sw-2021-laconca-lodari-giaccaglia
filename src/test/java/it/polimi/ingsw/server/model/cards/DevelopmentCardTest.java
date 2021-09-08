package it.polimi.ingsw.server.model.cards;

import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DevelopmentCardTest {


    @Test
    public void isAvailable()
    {
        //testo solo la condizione dei livelli, le risorse sono testate in production
        PersonalBoard personalboardtest = new PersonalBoard();
        Pair<Resource, Integer> costTest = new Pair<>(Resource.GOLD, 3);

        personalboardtest.getStrongBox().addResources(new int[]{3,0,0,0});
        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<>();
        requirementsTest.add(costTest);
        DevelopmentCard dev= new DevelopmentCard(1,DevelopmentCardColor.GREEN, Production.basicProduction(),3,requirementsTest);
        assertTrue(personalboardtest.areDevCardRequirementsSatisfied(dev));
        personalboardtest.getCardCells().get(0).addToTop(dev);
        personalboardtest.getCardCells().get(1).addToTop(dev);
        personalboardtest.getCardCells().get(2).addToTop(dev);
        assertFalse(personalboardtest.areDevCardRequirementsSatisfied(dev));

        dev= new DevelopmentCard(2,DevelopmentCardColor.GREEN, Production.basicProduction(),3,requirementsTest);

        assertTrue(personalboardtest.areDevCardRequirementsSatisfied(dev));
        personalboardtest.getCardCells().get(0).addToTop(dev);
        personalboardtest.getCardCells().get(1).addToTop(dev);
        personalboardtest.getCardCells().get(2).addToTop(dev);
        assertFalse(personalboardtest.areDevCardRequirementsSatisfied(dev));

        dev= new DevelopmentCard(3,DevelopmentCardColor.GREEN, Production.basicProduction(),3,requirementsTest);


        assertTrue(personalboardtest.areDevCardRequirementsSatisfied(dev));
        personalboardtest.getCardCells().get(0).addToTop(dev);
        personalboardtest.getCardCells().get(1).addToTop(dev);
        personalboardtest.getCardCells().get(2).addToTop(dev);
        assertFalse(personalboardtest.areDevCardRequirementsSatisfied(dev));

    }
}