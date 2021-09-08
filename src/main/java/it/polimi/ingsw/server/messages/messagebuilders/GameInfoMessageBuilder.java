package it.polimi.ingsw.server.messages.messagebuilders;

import it.polimi.ingsw.network.assets.resources.ResourceAsset;
import it.polimi.ingsw.network.simplemodel.ActiveLeaderBonusInfo;
import it.polimi.ingsw.network.simplemodel.SimplePlayerInfo;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.leaders.DepositLeader;
import it.polimi.ingsw.server.model.player.leaders.DevelopmentDiscountLeader;
import it.polimi.ingsw.server.model.player.leaders.MarketLeader;
import it.polimi.ingsw.server.model.player.leaders.ProductionLeader;
import it.polimi.ingsw.server.model.player.track.TileState;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameInfoMessageBuilder {

    private GameInfoMessageBuilder(){}

    public static List<Integer> getPlayersEndingTheGame(GameModel gameModel) {
        return new ArrayList<>(gameModel.getPlayersEndingTheGame().keySet());
    }

    public static Map<Integer, Boolean> getMatchOutcomeMap(GameModel gameModel){

        return gameModel.getMatchPlayers().keySet().stream().collect(Collectors.toMap(
                index -> index,
                index -> gameModel.getPlayer(index).get().getMatchOutCome()
        ));
    }

    public static List<ResourceAsset> depotBonusResources(GameModel gameModel, int playerIndex){

        if(gameModel.getPlayer(playerIndex).isPresent())
            return gameModel.getPlayer(playerIndex).get().getActiveLeaders().stream().filter(DepositLeader.class::isInstance).map(depositLeader -> ResourceAsset.fromInt(((DepositLeader) depositLeader).getLeaderDepot().geResourceType())).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    public static List<Pair<ResourceAsset, Integer>> discountBonusResources(GameModel gameModel, int playerIndex){

        if(gameModel.getPlayer(playerIndex).isPresent())
            return gameModel.getPlayer(playerIndex).get().getActiveLeaders().stream().filter(DevelopmentDiscountLeader.class::isInstance).map(discountLeader -> {

                        Pair<Resource, Integer> resourcePair = ((DevelopmentDiscountLeader) discountLeader).getDiscount();
                        ResourceAsset resourceAsset = ResourceAsset.fromInt(resourcePair.getKey().getResourceNumber());
                        int discountAmount = resourcePair.getValue();

                        return new Pair<>(resourceAsset, discountAmount);

            }).collect(Collectors.toList());
        else return new ArrayList<>();
    }

    public static List<ResourceAsset> marketBonusResources(GameModel gameModel, int playerIndex){

        if(gameModel.getPlayer(playerIndex).isPresent())
            return gameModel.getPlayer(playerIndex).get().getActiveLeaders().stream().filter(MarketLeader.class::isInstance).map(marketBonusLeader -> ResourceAsset.fromInt(((MarketLeader) marketBonusLeader).getResourceBonusType())).collect(Collectors.toList());
        else return new ArrayList<>();

    }

    public static List<ActiveLeaderBonusInfo.SimpleProductionBonus> productionBonusResources(GameModel gameModel, int playerIndex){

        if(gameModel.getPlayer(playerIndex).isPresent())
            return gameModel.getPlayer(playerIndex).get().getActiveLeaders().stream().filter(ProductionLeader.class::isInstance).map(productionLeader -> {

                ProductionLeader leader = ((ProductionLeader) productionLeader);

                    return new ActiveLeaderBonusInfo.SimpleProductionBonus(
                            leader.getProductionInputsMap().keySet().stream().map(ResourceAsset::fromInt).collect(Collectors.toList()),
                            leader.getProductionOutputsMap().keySet().stream().map(ResourceAsset::fromInt).collect(Collectors.toList()));

                    }

            ).collect(Collectors.toList());
        else return new ArrayList<>();

    }

    public static Map<Integer, SimplePlayerInfo> getSimplePlayerInfoMap(GameModel gameModel){

        int numOfPlayers = gameModel.getMatchPlayers().size();

        return IntStream.range(0, numOfPlayers).boxed().collect(Collectors.toMap(

                playerIndex -> playerIndex,

                playerIndex -> {

                    Player player = gameModel.getPlayer(playerIndex).get();
                    int currentVictoryPoints = player.getCurrentGamePoints();
                    int currentPosition = player.getPlayerPosition();
                    int currentLorenzoPosition = player.getLorenzoPosition();
                    String nickname = player.getNickname();
                    boolean isOnline = player.isOnline();

                    return new SimplePlayerInfo(
                            currentVictoryPoints,
                            currentPosition,
                            currentLorenzoPosition,
                            isOnline,
                            playerIndex,
                            nickname);
                }
        ));
    }

    public static Set<Integer> getPlayersTriggeringVaticanReport(GameModel gameModel){
       List<Integer> playersTriggeringList  = new ArrayList<>(gameModel.getVaticanReportTriggers());
       return new HashSet<>(playersTriggeringList);
    }

    public static Map<Integer, Pair<Integer, it.polimi.ingsw.network.simplemodel.TileState>> getPopeTileStateMap(GameModel gameModel){

        return IntStream.range(0, gameModel.getMatchPlayers().size()).boxed().collect(Collectors.toMap(

                index -> index,
                index -> gameModel.getPlayer(index)
                        .get()
                        .getStateOfLastTurnedTileInTrack().map(
                                state -> new Pair<>(
                                        state.getKey(),
                                        it.polimi.ingsw.network.simplemodel.TileState.valueOf(state.getValue().name())
                        ))


                        .orElse(new Pair<>(0, it.polimi.ingsw.network.simplemodel.TileState.valueOf(TileState.DISCARDED.name()))

        )));
    }




}
