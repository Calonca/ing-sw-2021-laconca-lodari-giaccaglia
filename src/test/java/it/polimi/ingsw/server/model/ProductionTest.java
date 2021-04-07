package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductionTest {

    @Test
    public void isAvailable()
    {
        PersonalBoard personalboardtest=new PersonalBoard();
        Production productiontest;

        //SOLO RISORSA NORMALE, SUCCESSO
        productiontest= new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,0,0,1});
        personalboardtest.addProduction(productiontest);
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{1,0,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,0,0,0,0,0,0});

        //SOLO RISORSA NORMALE, SUCCESSO ABBONDANTE
        productiontest= new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,0,0,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{10,0,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{10,0,0,0,0,0,0});

        //SOLO RISORSA NORMALE, FALLIMENTO
        productiontest= new Production(new int[]{1,0,0,0,0,0,0},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{0,1,0,0,0,0,0});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{0,1,0,0,0,0,0});


        //SOLO RISORSA NORMALE, FALLIMENTO VICINO
        productiontest= new Production(new int[]{10,0,0,0,0,0,0},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{9,1,0,0,0,0,0});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{9,1,0,0,0,0,0});

        //RISORSE A SCELTA SPARSE, SUCCESSO
        productiontest= new Production(new int[]{0,0,0,0,0,0,4},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{1,1,1,1,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,1,1,1,0,0,0});

        //RISORSE A SCELTA SPARSE, SUCCESSO ABBONDANTE
        productiontest= new Production(new int[]{0,0,0,0,0,0,40},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{10,10,10,100,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{10,10,10,100,0,0,0});

        //RISORSE A SCELTA SPARSE, FALLIMENTO VICINO
        productiontest= new Production(new int[]{0,0,0,0,0,0,40},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{10,10,9,10,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{10,10,9,10,0,0,0});

        //RISORSE A SCELTA SPARSE, FALLIMENTO
        productiontest= new Production(new int[]{0,0,0,0,0,0,40},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{10,10,0,10,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{10,10,9,10,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO PER ENTRAMBI
        productiontest= new Production(new int[]{1,0,0,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{1,1,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,1,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO PER A SCELTA
        productiontest= new Production(new int[]{1,0,0,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{0,1,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{0,1,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO ABBONDANTE PER SCELTA
        productiontest= new Production(new int[]{1,0,0,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{1,10,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{0,1,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO ABBONDANTE PER ENTRAMBI
        productiontest= new Production(new int[]{1,0,0,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{100,100,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,100,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO ABBONDANTE PER SCELTA
        productiontest= new Production(new int[]{1,0,0,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{0,100,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{0,100,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO ABBONDANTE PER SCELTA E FALLIMENTO VICINO PER NORMALE
        productiontest= new Production(new int[]{10,0,0,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{9,100,0,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{0,100,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO PER QUELLA NORMALE
        productiontest= new Production(new int[]{1,1,1,0,0,0,1},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{1,1,1,0,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,0,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO PER QUELLA NORMALE FALLIMENTO VICINO PER SCELTA
        productiontest= new Production(new int[]{1,1,1,0,0,0,10},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{1,1,1,9,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,0,0,0,0,0,0});


        //RISORSA NORMALE E A SCELTA, SUCCESSO ABBONDANTE PER NORMALE E FALLIMENTO PER SCELTA
        productiontest= new Production(new int[]{1,1,1,0,0,0,100},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{10,10,10,9,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,0,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, SUCCESSO ABBONDANTE PER NORMALE E FALLIMENTO VICINO PER SCELTA
        productiontest= new Production(new int[]{1,1,1,0,0,0,31},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{10,10,10,3,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,0,0,0,0,0,0});

        //RISORSA NORMALE E A SCELTA, FALLIMENTO VICINO PER ENTRAMBI
        productiontest= new Production(new int[]{10,10,10,0,0,0,29},new int[]{0,0,0,0,0,1,1});
        assertFalse(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().addResources(new int[]{9,9,9,3,0,0,0});
        assertTrue(productiontest.isAvailable(personalboardtest));
        personalboardtest.getStrongBox().removeResources(new int[]{1,0,0,0,0,0,0});








    }

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