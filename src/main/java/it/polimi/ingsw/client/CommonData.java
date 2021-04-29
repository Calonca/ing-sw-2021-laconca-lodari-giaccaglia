package it.polimi.ingsw.client;

import javafx.util.Pair;

import java.util.Optional;
import java.util.UUID;


/**
 * Useful data before the start of the game.
 *
 * Todo make it so that when reciving a {@link it.polimi.ingsw.client.messages.servertoclient.MatchesData MatchesData} the data visible in {@link it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatchView CreateJoinLoadMatchView} gets updated
 */
public class CommonData {
    public Optional<Pair<UUID,String[]>[]> matchesData = Optional.empty();
}
