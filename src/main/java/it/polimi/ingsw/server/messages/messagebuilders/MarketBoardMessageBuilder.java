package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.market.Marble;
import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class MarketBoardMessageBuilder {

    public static UUID[][] marketBoardAdapter(GameModel gameModel){

        Marble[][] marketMarbles  = gameModel.getMarketMarbles();
        int marketRows = gameModel.getMarketBoardRows();
        int marketColumns = gameModel.getMarketBoardColumns();

        UUID[][] uuidMarblesMatrix = new UUID[marketRows][marketColumns];

        for(int i=0; i<marketRows; i++){
            for(int j=0; j<marketColumns; j++){
                uuidMarblesMatrix[i][j] = UUID.nameUUIDFromBytes(marketMarbles[i][j].toString().getBytes(StandardCharsets.UTF_8));
            }
        }

        return uuidMarblesMatrix;
    }

    public static List<UUID> pickedMarblesAdapter(GameModel gameModel){

        Marble [] pickedMarbles = gameModel.getPickedMarbles();
        return Arrays.stream(pickedMarbles).map(marble -> UUID.nameUUIDFromBytes(marble.toString().getBytes(StandardCharsets.UTF_8))).collect(Collectors.toList());

    }


}
