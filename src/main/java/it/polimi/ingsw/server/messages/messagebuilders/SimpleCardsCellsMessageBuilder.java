package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimpleCardsCellsMessageBuilder {

    public static Map<Integer, List<UUID>> cardCellsAdapter(Map<Integer, List<DevelopmentCard>> visibleCardCells){

        return visibleCardCells.keySet().stream().collect(Collectors.toMap(
                integer -> integer,
                integer ->{

                    List<UUID> cardIds = new ArrayList<>();
                    if(visibleCardCells.get(integer).size()>0)
                        cardIds = visibleCardCells.get(integer)
                                .stream()
                                .map(DevelopmentCard::getCardId)
                                .collect(Collectors.toList());

                    return cardIds;
                }
        ));
    }
}
