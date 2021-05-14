package it.polimi.ingsw.server.model.player.leaders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.network.assets.devcards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import javafx.util.Pair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class LeaderTest
{

    @Test
    public void areRequirementsSatisfied()
    {
        Pair<Resource, Integer> discountTest = new Pair<>(Resource.GOLD, 3);
        LeaderDepot depotTest= new LeaderDepot(2,Resource.GOLD);
        Resource bonus=Resource.GOLD;
        Production productiontest = Production.basicProduction();


        Pair<Resource, Integer> costTest = new Pair<>(Resource.GOLD, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 1);
        DevelopmentCard testcard= new DevelopmentCard(1,DevelopmentCardColor.BLUE,new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1}));

        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);

        Leader leadertestDD;
        Leader leadertestD;
        Leader leadertestM;
        Leader leadertestP;

        //FALLISCE NON HA NIENTE
        leadertestM = new MarketLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, bonus);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestM));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{0,0,3,0});
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestM));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{0,0,3,0});

        // FALLISCE HA RISORSA MA NON CARTA
        leadertestDD = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, discountTest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestDD));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{3,0,0,0});
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestDD));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{3,0,0,0});

        // FALLISCE HA MOLTA RISORSA MA NON CARTA
        leadertestDD = new DevelopmentDiscountLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, discountTest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestDD));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestDD));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});

        gamemodel.getCurrentPlayer().getPersonalBoard().addDevelopmentCardToCell(testcard,1);
        //HA CARTA MA NON RISORSA
        leadertestD = new DepositLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, depotTest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestD));


        //HA CARTA E RISORSA
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{3,0,0,0});
        assertTrue(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{3,0,0,0});

        //HA CARTA E TANTA RISORSA
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertTrue(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});


        gamemodel.getCurrentPlayer().getPersonalBoard().addDevelopmentCardToCell(testcard,1);
        //HA TANTE CARTE E NON RISORSA
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));

        //HA TANTE CARTE E RISORSE GIUSTE
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{3,0,0,0});
        assertTrue(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{3,0,0,0});

        //HA TANTE CARTE E TANTE RISORSE
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertTrue(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});


        cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.GREEN, 1);
        requirementsCardsTest.add(cardcostTest);
        //NON HA CARTE DI COLORI DIVERSI E TANTE RISORSE
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});


        testcard= new DevelopmentCard(1,DevelopmentCardColor.GREEN,new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1}));
        gamemodel.getCurrentPlayer().getPersonalBoard().addDevelopmentCardToCell(testcard,1);
        //HA CARTE DI COLORI DIVERSI E TANTE RISORSE
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertTrue(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});

        testcard= new DevelopmentCard(2,DevelopmentCardColor.GREEN,new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1}));
        gamemodel.getCurrentPlayer().getPersonalBoard().addDevelopmentCardToCell(testcard,1);
        //HA CARTE DI COLORI DIVERSI MA NON DEL LIVELLO GIUSTO E TANTE RISORSE
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest,2);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});

        testcard= new DevelopmentCard(2,DevelopmentCardColor.BLUE,new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1}));
        gamemodel.getCurrentPlayer().getPersonalBoard().addDevelopmentCardToCell(testcard,2);
        //HA CARTE DI COLORI DIVERSI DEL LIVELLO GIUSTO E TANTE RISORSE
        leadertestP = new ProductionLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, productiontest,2);
        assertFalse(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().addResources(new int[]{30,0,0,0});
        assertTrue(gamemodel.getCurrentPlayer().getPersonalBoard().isLeaderRequirementsSatisfied(leadertestP));
        gamemodel.getCurrentPlayer().getPersonalBoard().getStrongBox().removeResources(new int[]{30,0,0,0});
    }


    @Test
    public void setPast() {
        Pair<Resource, Integer> discountTest = new Pair<>(Resource.GOLD, 3);
        LeaderDepot depotTest= new LeaderDepot(2,Resource.GOLD);
        Resource bonus=Resource.GOLD;
        Production productiontest = Production.basicProduction();


        Pair<Resource, Integer> costTest = new Pair<>(Resource.GOLD, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 1);
        DevelopmentCard testcard= new DevelopmentCard(1,DevelopmentCardColor.BLUE,new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1}));

        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);


        Leader test=new MarketLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, bonus);

    }

    @Test
    public void anyLeaderPlayable() {
        Pair<Resource, Integer> discountTest = new Pair<>(Resource.GOLD, 3);
        LeaderDepot depotTest= new LeaderDepot(2,Resource.GOLD);
        Resource bonus=Resource.GOLD;
        Production productiontest = Production.basicProduction();


        Pair<Resource, Integer> costTest = new Pair<>(Resource.GOLD, 3);
        Pair<DevelopmentCardColor, Integer> cardcostTest = new Pair<DevelopmentCardColor,Integer>(DevelopmentCardColor.BLUE, 1);
        DevelopmentCard testcard= new DevelopmentCard(1,DevelopmentCardColor.BLUE,new Production(new int[]{0,0,0,0,0,0,2},new int[]{0,0,0,0,0,1,1}));

        List<Pair<Resource, Integer>> requirementsTest = new ArrayList<Pair<Resource, Integer>>();
        List<Pair<DevelopmentCardColor, Integer>> requirementsCardsTest = new ArrayList<Pair<DevelopmentCardColor, Integer>>();

        requirementsTest.add(costTest);
        requirementsCardsTest.add(cardcostTest);

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer,null);

        Leader test=new MarketLeader(LeaderState.INACTIVE, 3, requirementsTest, requirementsCardsTest, bonus);;
        assertTrue(gamemodel.anyLeaderPlayableForCurrentPlayer());
    }
}