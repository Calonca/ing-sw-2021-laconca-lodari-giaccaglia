package it.polimi.ingsw.server.model;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.*;
import it.polimi.ingsw.server.model.player.board.Box;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameModel {

    private Player currentPlayer;
    private List<Player> players;
    private List<Player> onlinePlayers;
    private MarketBoard resourcesMarket;
    private State gamePhase;
    private Player winnerPlayer;
    private boolean isStarted;
    private final boolean isSinglePlayer;
    private Player singlePlayer;
    private SinglePlayerDeck soloDeck;
    private CardShop cardShop;

    public GameModel(List<String> nicknames, boolean isSinglePlayer){

        this.isSinglePlayer = isSinglePlayer;
        commonInit(nicknames);

        if(isSinglePlayer) soloModeInit(nicknames.get(0));

        isStarted = false;
    }

    private void commonInit(List<String> nicknames){
        resourcesMarket = MarketBoard.initializeMarketBoard("src/main/resources/config/MarketBoardConfig.json");
        players = new ArrayList<>(nicknames.size());
        players = nicknames.stream().map(Player::new).collect(Collectors.toList());
        onlinePlayers = nicknames.stream().map(Player::new).collect(Collectors.toList());
        cardShop = new CardShop();
    }

    private void soloModeInit(String nickName){
        singlePlayer = new Player(nickName);
        soloDeck = new SinglePlayerDeck();
        currentPlayer = singlePlayer;
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {this.currentPlayer = currentPlayer;}

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
                .filter(player -> !(player == currentPlayer))
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

    public void chooseLineFromMarketBoard(MarketLine line){
        resourcesMarket.chooseMarketLine(line);
    }

    public void updateMatrixAfterTakingResources(){
        resourcesMarket.updateMatrix();
    }

    public boolean areThereWhiteMarblesInPickedLine(){
        return resourcesMarket.areThereWhiteMarbles();
    }

    public int getNumberOfWhiteMarblesInPickedLine(){
        return resourcesMarket.getNumberOfWhiteMarbles();
    }

    public void convertWhiteMarbleInPickedLine(){
        resourcesMarket.convertWhiteMarble();
    }

    public Box getBoxResourcesFromMarketBoard(){
        return resourcesMarket.getMappedResourcesBox();
    }

    public void setPlayerStatus(int playerNumber, boolean currentlyOnline){
        players.get(playerNumber).setCurrentStatus(currentlyOnline);

        if(currentlyOnline) {
            if (onlinePlayers.stream()
                    .noneMatch(player -> player == players.get(playerNumber)))
                onlinePlayers.add(players.get(playerNumber));
        }
        else{
            onlinePlayers = onlinePlayers.stream()
                    .filter(player -> player!= players.get(playerNumber))
                    .collect(Collectors.toList());
        }
    }

    public boolean isPlayerCurrentlyOnline(int playerNumber){
        return onlinePlayers.stream()
                .anyMatch(player -> player == players.get(playerNumber));
    }


}
