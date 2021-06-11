package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleCardsCellsMessageBuilder {

    public static Map<Integer, List<Pair<UUID, Boolean>>> cardCellsAdapter(GameModel gameModel){

        Map<Integer, List<DevelopmentCard>> visibleCardCells = gameModel.getCurrentPlayer().getPersonalBoard().getVisibleCardsOnCells();

        return visibleCardCells.keySet().stream().collect(Collectors.toMap(
                integer -> integer,
                integer ->{

                    List<Pair<UUID, Boolean>> cardIds = new ArrayList<>(0);

                    if(visibleCardCells.get(integer).size()>0)
                        cardIds = visibleCardCells.get(integer)
                                .stream()
                                .map(card -> new Pair<>(card.getCardId(), checkProductionRequirements(gameModel, integer)))
                                .collect(Collectors.toList());
                    return cardIds;

                }
        ));

    }


    private static boolean checkProductionRequirements(GameModel gameModel, int cardPosition){
        Boolean[] prods = gameModel.getCurrentPlayer().getPersonalBoard().getAvailableProductions();
        return prods[cardPosition];
    }

}
