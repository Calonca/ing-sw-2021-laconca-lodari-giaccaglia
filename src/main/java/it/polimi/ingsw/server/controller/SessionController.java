package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.model.player.track.FaithTrack;
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
    
    private List<Match> matches = new ArrayList<>();
    private List<String> playersInLobby = new ArrayList<>();

    public void addPlayerToLobby(String nickname){
        playersInLobby.add(nickname);
    }

    public Match getLastMatch(){
           return matches.get(matches.size()-1);
    }

    public void addNewMatch(int maxPlayers){
        matches.add(new Match(maxPlayers));
    }

    public void reloadServer() {
        File folder = new File("src/main/resources/savedMatches/");
        Gson gson = new Gson();
        Arrays.stream(folder.listFiles()).filter(File::isFile).forEach((file)->{
            try {
                String match = Files.readString(Path.of(folder.toString() + file.getName() + ".json"), StandardCharsets.US_ASCII);
                matches.add(gson.fromJson(match, Match.class));
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
