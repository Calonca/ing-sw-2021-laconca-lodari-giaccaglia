package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.market.Marble;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MarketBoardMessageBuilder {

    private MarketBoardMessageBuilder(){}

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
