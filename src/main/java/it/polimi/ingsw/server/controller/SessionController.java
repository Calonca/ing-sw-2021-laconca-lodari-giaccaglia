package it.polimi.ingsw.server.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.json.Deserializator;
import it.polimi.ingsw.network.assets.CardAssetsContainer;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.jsonUtils.JsonUtility.deserialize;

public class SessionController {
    
    private HashMap<UUID,Match> matches = new HashMap<>();
    private List<ClientHandler> playersInLobby = new ArrayList<>();
    private static SessionController single_instance = null;

    public static SessionController getInstance()
    {
        if (single_instance == null)
            single_instance = new SessionController();
        return single_instance;
    }

    public void addPlayerToLobby(ClientHandler clientHandler){
        playersInLobby.add(clientHandler);
    }

    public Match getLastMatch(){
           return matches.get(matches.size()-1);
    }

    public Match addNewMatch(int maxPlayers, ClientHandler clientHandler){
        Match match = new Match(maxPlayers);
        matches.put(match.getMatchId(),match);
        clientHandler.setMatch(match);
        return match;
    }

    /**
     * Adds player to match and start
     * @return -1 if match is full or other problems
     */
    public int addPlayerToMatch(UUID matchID, String nickname, ClientHandler clientHandler){
        Match match = matches.get(matchID);
        if (match.canAddPlayer()){
            match.addPlayer(nickname,clientHandler);
            clientHandler.setMatch(match);
            return match.getLastPos();
        }
        else return -1;
    }

    public void startMatchAndNotifyStateIfPossible(Match match, ClientHandler clientHandler) {
        if (!match.canAddPlayer()) {
            match.startGame();
            playersInLobby.removeAll(match.clientsStream().collect(Collectors.toList()));
            try {
                List<Element> elements = new ArrayList<>();
                CardAssetsContainer.setCardAssetsContainer(Deserializator.networkDevCardsAssetsDeserialization());
                elements.add(Element.SimplePlayerLeaders);
                State state = State.SETUP_PHASE;
                StateInNetwork stateInNetwork = state.toStateMessage(match.getGame(), elements);
                match.currentPlayerClientHandler().sendAnswerMessage(stateInNetwork);
            } catch (IOException e) {
                e.printStackTrace();
            } //TODO RESTORE COMMENTED OUT CODE
        }
    }


    public void notifyPlayersInLobby(ClientHandler cl){
        playersInLobby.forEach(
                clientHandler -> {
                    try {
                        clientHandler.sendAnswerMessage(new MatchesData(matchesData(clientHandler)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public Map<UUID, String[]> matchesData(ClientHandler clientHandler){
        return matches.entrySet().stream()
                .filter(m->
                        (clientHandler.getMatch().isPresent()&&clientHandler.getMatch().get().getMatchId().equals(m.getKey()))
                        ||
                        (m.getValue().canAddPlayer())
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry->entry.getValue().getOnlinePlayers()
                ));
    }

    public void reloadServer() {
        File folder = new File("src/main/resources/savedMatches/");
        Arrays.stream(folder.listFiles()).filter(File::isFile).forEach((file)->{
                Match match = deserialize(folder.toString() + file.getName() + ".json", Match.class);
                matches.put(match.getMatchId(),match);
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
        return Arrays.stream(folder.listFiles()).filter(File::isFile)
                .filter(file -> file.getName().contains(gameId.toString()))
                .findFirst()
                .map((file -> deserialize(folder.toString() + file.getName() + ".json", Match.class)));
    }

    public void deleteGame(UUID gameId){
        File folder = new File("src/main/resources/savedMatches/");
        Arrays.stream(folder.listFiles()).filter(File::isFile)
                .filter(file -> file.getName().contains(gameId.toString()))
                .findFirst().ifPresent(File::delete);
    }
}
