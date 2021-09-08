package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class SimpleCardsCellsMessageBuilder {

    private SimpleCardsCellsMessageBuilder(){}

    public static Map<Integer, List<Pair<UUID, Boolean>>> cardCellsAdapter(GameModel gameModel, int indexOfPlayerRequestingUpdate) {


        if (gameModel.getPlayer(indexOfPlayerRequestingUpdate).isPresent()) {

            Map<Integer, List<DevelopmentCard>> visibleCardCells = gameModel.getPlayer(indexOfPlayerRequestingUpdate).get().getPersonalBoard().getVisibleCardsOnCells();

            return visibleCardCells.keySet().stream().collect(Collectors.toMap(
                    integer -> integer ,
                    integer -> {

                        List<Pair<UUID, Boolean>> cardIds = new ArrayList<>(0);

                        if (!visibleCardCells.get(integer).isEmpty())
                            cardIds = visibleCardCells.get(integer)
                                    .stream()
                                    .map(card -> new Pair<>(card.getCardId() , checkProductionRequirements(gameModel , integer , indexOfPlayerRequestingUpdate)))
                                    .collect(Collectors.toList());
                        return cardIds;

                    }
            ));

        }

        else return new HashMap<>();

    }

    private static boolean checkProductionRequirements(GameModel gameModel, int cardPosition, int indexOfPlayerRequestingUpdate){
        if( gameModel.getPlayer(indexOfPlayerRequestingUpdate).isPresent()){
        Boolean[] prods = gameModel.getPlayer(indexOfPlayerRequestingUpdate).get().getPersonalBoard().getAvailableProductions();
        return prods[cardPosition];
    }
        else return false;
    }

}
