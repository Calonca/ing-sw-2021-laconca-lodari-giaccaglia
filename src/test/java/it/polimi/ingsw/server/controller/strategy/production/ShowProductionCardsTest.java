package it.polimi.ingsw.server.controller.strategy.production;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.production.Production;
import it.polimi.ingsw.server.model.player.State;
import it.polimi.ingsw.server.model.player.board.LeaderDepot;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShowProductionCardsTest {

    @Test
    public void execute() {

        List<String> nicknames = new ArrayList<>();
        nicknames.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gamemodel = new GameModel(nicknames, isSinglePlayer);
        assertEquals(new ShowProductionCards().execute(gamemodel), State.CHOOSING_CARD_FOR_PRODUCTION);
    }
}