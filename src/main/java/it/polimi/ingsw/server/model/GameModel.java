package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.SinglePlayerDeck;
import it.polimi.ingsw.server.model.player.SoloActionToken;
import it.polimi.ingsw.server.model.player.State;
import it.polimi.ingsw.server.model.player.board.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameModel {

    public static Player currentPlayer;
    private final List<Player> players;
    private List<Player> onlinePlayers;
    private final MarketBoard resourcesMarket;
    private State gamePhase;
    private Player winnerPlayer;
    private boolean isStarted;
    private boolean isSinglePlayer;
    private final Player singlePlayer;
    private final SinglePlayerDeck soloDeck;
    private final CardShop cardShop;

    public GameModel() throws IOException {

        resourcesMarket = MarketBoard.initializeMarketBoard("MarketBoardConfig.json");
        //temporary assignments to avoid warnings
        soloDeck = new SinglePlayerDeck();
        singlePlayer = new Player();
        players = new ArrayList<>(4);
        cardShop = new CardShop();

    }


    public Player getCurrentPlayer()
    {
        return currentPlayer;
    }



    public Player getPlayer(String nickname) {
          return players.stream()
                .filter(player1 -> player1.getNickName().equals(nickname))
                .findAny().orElse(null);
    }

    private void updateOnlinePlayers() {
        onlinePlayers = onlinePlayers
                .stream()
                .filter(Player::isOnline)
                .collect(Collectors.toList());
    }

    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers);
    }

    public State getGamePhase(){
        return gamePhase;
    }

    public Player getNextPlayer() {

        int currentPlayerIndex = onlinePlayers.indexOf(currentPlayer);

        if(currentPlayerIndex == (onlinePlayers.size() -1 ) )
            return players.get(0);
        else
            return players.get(onlinePlayers.indexOf(currentPlayer) + 1);
    }

    public void addFaithPointToOtherPlayers() {
        onlinePlayers.stream()
                .filter(player1 -> !(player1 == currentPlayer))
                .forEach(Player::moveOnePosition);
    }

    public void addFaithPointToLorenzo(){
        singlePlayer.moveLorenzoOnePosition();
    }

    public void shuffleSoloDeck(){
        soloDeck.shuffleActionTokens();
    }

    public SoloActionToken showSoloActionToken(){
        return soloDeck.showToken();
    }

    public void activateSoloActionToken(){
        soloDeck.activateToken(this);
    }

    public void discardCardFromShop(DevelopmentCardColor card, int amount){
        cardShop.discardCard(card, amount);
    }

    public Player getSinglePlayer(){
        return singlePlayer;
    }

    public Marble[] pickResourcesFromMarket(MarketLine line){
        return resourcesMarket.pickResources(line);
    }

    public void updateMatrixAfterTakingResources(MarketLine line ){
        resourcesMarket.updateMatrix(line);
    }

}
