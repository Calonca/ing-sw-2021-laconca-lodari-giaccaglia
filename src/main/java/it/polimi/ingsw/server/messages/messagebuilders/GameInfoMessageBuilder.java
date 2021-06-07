package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.ActiveLeaderBonusInfo;
import it.polimi.ingsw.network.simplemodel.EndGameInfo;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.leaders.DepositLeader;
import it.polimi.ingsw.server.model.player.leaders.DevelopmentDiscountLeader;
import it.polimi.ingsw.server.model.player.leaders.MarketLeader;
import it.polimi.ingsw.server.model.player.leaders.ProductionLeader;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameInfoMessageBuilder {

    public static Map<Integer, EndGameInfo.PlayerInfo> endGameInfoMap(GameModel gameModel){

        Map<Integer, Player> players = gameModel.getMatchPlayers();

        return players.keySet().stream().collect(Collectors.toMap(

                playerIndex -> playerIndex, playerIndex -> {

                    Player player = players.get(playerIndex);

                    return new EndGameInfo.PlayerInfo(

                            player.getCurrentGamePoints(),
                            player.getMatchOutCome(),
                            player.getPlayerPosition(),
                            player.getLorenzoPosition(),
                            player.getNickName()
                    );
                }
        ));

    }

    public static List<Integer> getPlayersEndingTheGame(GameModel gameModel) {
        return new ArrayList<>(gameModel.getPlayersEndingTheGame().keySet());
    }

    public static List<ResourceAsset> depotBonusResources(GameModel gameModel){

        return gameModel.getCurrentPlayer().getActiveLeaders().stream().filter(leader ->
                leader instanceof DepositLeader).map(depositLeader -> {

                    ResourceAsset asset = ResourceAsset.fromInt(((DepositLeader) depositLeader).getLeaderDepot().geResourceType());

                    return asset;

        }).collect(Collectors.toList());

    }

    public static List<Pair<ResourceAsset, Integer>> discountBonusResources(GameModel gameModel){

        return gameModel.getCurrentPlayer().getActiveLeaders().stream().filter(leader -> leader instanceof DevelopmentDiscountLeader).map(discountLeader -> {

                    DevelopmentDiscountLeader developmentDiscountLeader = (DevelopmentDiscountLeader) discountLeader;

                    Pair<Resource, Integer> resourcePair = ((DevelopmentDiscountLeader) discountLeader).getDiscount();
                    ResourceAsset resourceAsset = ResourceAsset.fromInt(resourcePair.getKey().getResourceNumber());
                    int discountAmount = resourcePair.getValue();

                    return new Pair<>(resourceAsset, discountAmount);

        }).collect(Collectors.toList());
    }

    public static List<ResourceAsset> marketBonusResources(GameModel gameModel){

        return gameModel.getCurrentPlayer().getActiveLeaders().stream().filter(leader -> leader instanceof MarketLeader).map(marketBonusLeader -> {

                    ResourceAsset asset = ResourceAsset.fromInt(((MarketLeader) marketBonusLeader).getResourceBonusType());

                    return asset;

        }).collect(Collectors.toList());

    }

    public static List<ActiveLeaderBonusInfo.SimpleProductionBonus> productionBonusResources(GameModel gameModel){

        return gameModel.getCurrentPlayer().getActiveLeaders().stream().filter(leader -> leader instanceof ProductionLeader).map(productionLeader -> {

            ProductionLeader leader = ((ProductionLeader) productionLeader);

                return new ActiveLeaderBonusInfo.SimpleProductionBonus(
                        leader.getProductionInputs().stream().map(ResourceAsset::fromInt).collect(Collectors.toList()),
                leader.getProductionOutputs().stream().map(ResourceAsset::fromInt).collect(Collectors.toList()));

                }

        ).collect(Collectors.toList());

    }

}
