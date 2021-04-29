package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SessionController {
    
    private Map<UUID,Match> matches = new HashMap<>();
    private List<String> playersInLobby = new ArrayList<>();
    private static SessionController single_instance = null;

    public static SessionController getInstance()
    {
        if (single_instance == null)
            single_instance = new SessionController();

        return single_instance;
    }

    public void addPlayerToLobby(String nickname){
        playersInLobby.add(nickname);
    }

    public Match getLastMatch(){
           return matches.get(matches.size()-1);
    }

    public Match addNewMatch(int maxPlayers){
        Match match = new Match(maxPlayers);
        matches.put(match.getMatchId(),match);
        return match;
    }

    /**
     * Adds player to match and start
     * @param matchID
     * @param nickname
     * @return false if match is full
     */
    public boolean addPlayerToMatchAndStartWhenReady(UUID matchID, String nickname, ClientHandler clientHandler){
        Match match = matches.get(matchID);
        if (match.canAddPlayer()){
            match.addPlayer(nickname,clientHandler);
            if (!match.canAddPlayer()){
                Runnable r = match::startGame;
                r.run();
            }
            notifyMatchChanges();
            return true;
        }
        else return false;
    }

    public void notifyMatchChanges(){
        matches.values().stream().filter(Match::hasNotStarted).flatMap(Match::clientsStream).forEach(
                clientHandler -> {
                    try {
                        clientHandler.sendAnswerMessage(new MatchesData(matchesData()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public Pair<UUID,String[]>[] matchesData(){
        return matches.entrySet().stream()
                .map(entry->new Pair<>(entry.getKey(), entry.getValue().getOnlinePlayers()))
                .toArray(Pair[]::new);
    }

    public void reloadServer() {
        File folder = new File("src/main/resources/savedMatches/");
        Gson gson = new Gson();
        Arrays.stream(folder.listFiles()).filter(File::isFile).forEach((file)->{
            try {
                String matchString = Files.readString(Path.of(folder.toString() + file.getName() + ".json"), StandardCharsets.US_ASCII);
                Match match = gson.fromJson(matchString, Match.class);
                matches.put(match.getMatchId(),match);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public Optional<UUID> checkForSavedMatches(String nickname){
        File folder = new File("src/main/resources/savedMatches/");
        return Arrays.stream(folder.listFiles()).filter(File::isFile).map(File::getName)
                .filter(s -> s.contains(nickname))
                .map(s->UUID.fromString(StringUtils.substringAfterLast(s,"|")))
                .findFirst();
    }

    public void saveMatch(Match match) throws IOException {
        String gameName = match.getSaveName();
        Writer writer = new FileWriter("src/main/resources/savedMatches/"+gameName+".json");
        Gson gson = new GsonBuilder().create();
        gson.toJson(match, writer);
        writer.flush(); //flush data to file
        writer.close(); //close write
    }

    public Optional<Match> loadMatch(UUID gameId) {
        File folder = new File("src/main/resources/savedMatches/");
        Gson gson = new Gson();
        return Arrays.stream(folder.listFiles()).filter(File::isFile)
                .filter(file -> file.getName().contains(gameId.toString()))
                .findFirst()
                .map((file -> {
                    String match = null;
                    try {
                        match = Files.readString(Path.of(folder.toString() + file.getName() + ".json"), StandardCharsets.US_ASCII);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return gson.fromJson(match, Match.class);
        }));
    }

    public void deleteGame(UUID gameId){
        File folder = new File("src/main/resources/savedMatches/");
        Arrays.stream(folder.listFiles()).filter(File::isFile)
                .filter(file -> file.getName().contains(gameId.toString()))
                .findFirst().ifPresent(File::delete);
    }
}
