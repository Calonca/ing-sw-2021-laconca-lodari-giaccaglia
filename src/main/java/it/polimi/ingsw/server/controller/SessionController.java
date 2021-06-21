package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.utils.Deserializator;
import it.polimi.ingsw.server.utils.Serializator;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class SessionController {
    
    private HashMap<UUID,Match> matches = new HashMap<>();

    private HashMap<UUID, Long> matchesDisconnectionTimes = new HashMap<>();
    private transient List<ClientHandler> playersInLobby = new ArrayList<>();
    private static SessionController single_instance = null;
    private final String matchesFolderName = "src/savedMatches";
    private static final String sessionFolderName = "src/savedSession";
    private final int maxSecondsOffline = 1800; //30 minutes
    private final AtomicBoolean isDebugMode = new AtomicBoolean(false);

    public static SessionController getInstance()
    {
        if (Objects.isNull(single_instance))
            CreateSessionController();
        return single_instance;
    }

    private static void CreateSessionController(){

        reloadSessionIfPresent().ifPresentOrElse(
                loadedSession -> single_instance = loadedSession,
                () -> single_instance = new SessionController());
    }

    private SessionController(){}

    public void toggleDebugMode(){
        boolean oldDebugMode = isDebugMode.get();
        this.isDebugMode.set(!oldDebugMode);
    }

    public boolean getDebugMode(){
        return isDebugMode.get();
    }
    public void addPlayerToLobby(ClientHandler clientHandler){
        playersInLobby.add(clientHandler);
    }

    public Match getLastMatch(){
           return matches.get(matches.size()-1);
    }

    public Optional<Match> getMatch(UUID matchId){
        return Optional.of(matches.get(matchId));
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

        if (match.canAddPlayer() || match.isPlayerOffline(nickname)){
            match.addPlayer(nickname,clientHandler);
            clientHandler.setMatch(match);
            return match.getLastPos();
        }

        else return -1;
    }

    public void startMatchAndNotifyStateIfPossible(Match match) {

        if (!match.canAddPlayer()) {
            playersInLobby.removeAll(match.clientsStream().collect(Collectors.toList()));
            match.startGame();
        }
    }

    public void joinMatchAndNotifyStateIfPossible(Match match, String playerNickname){

            List<Element> elements = Element.getAsList();
            match.notifyStateToAllPlayers(elements, playerNickname);
            match.updateOtherPlayersCachesMessage(playerNickname);


    }

    public void notifyPlayerDisconnection(Match match, String playerNickname){
        List<Element> elements = new ArrayList<>();
        elements.add(Element.PlayersInfo);
        match.notifyStateToAllPlayers(elements, playerNickname);
        match.transitionToNextStateAfterDisconnection(playerNickname);

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
    //                  isSaved    onlineUsers, offlineUsers
    public Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData(ClientHandler clientHandler){

        return matches.entrySet().stream()
                .filter(match->
                        (clientHandler.getMatch().isPresent() && clientHandler.getMatch().get().getMatchId().equals(match.getKey()))
                        ||
                        (match.getValue().canAddPlayer())
                        || (match.getValue().wasClientInMatch(clientHandler.getNickname()))
                ).sorted((m1, m2) -> m2.getValue().getCreatedTime().compareTo(m1.getValue().getCreatedTime()))
                .limit(20)
                .collect(Collectors.toMap(
                        entry -> {

                            UUID matchId = entry.getKey();
                            Boolean isSavedMatch = matches.get(matchId).wasClientInMatch(clientHandler.getNickname());
                            return new Pair<>(matchId, isSavedMatch);
                        },

                        entry-> new Pair<>(entry.getValue().getOnlinePlayers(), entry.getValue().getOfflinePlayers())
                ));
    }

    private void reloadSavedMatches() {

        File folder = new File(Paths.get(matchesFolderName).toAbsolutePath().toString());

        Arrays.stream(folder.listFiles()).filter(File::isFile).forEach((file)->{

                Match match = Deserializator.deserializeMatch(folder.toString() +'/' + file.getName());
                match.getGame().setMatch(match);
                matches.put(match.getMatchId(),match);
        });

    }

    private static Optional<SessionController> reloadSessionIfPresent() {

        File folder = new File(Paths.get(sessionFolderName).toAbsolutePath().toString());

        return Arrays
                .stream(folder.listFiles())
                .filter(File::isFile)
                .findFirst()
                .map(file -> {
                    SessionController session = Deserializator.deserializeSession(folder.toString() + '/' + file.getName());
                    session.matches.values().forEach(Match::restoreMatch);
                    return session;
                });
    }

    //saved match for a player
    public Optional<UUID> checkForSavedMatches(String nickname){

        File folder = new File(matchesFolderName);
        folder = new File(folder.getAbsolutePath());

        return Arrays.stream(folder.listFiles()).filter(File::isFile).map(File::getName)
                .filter(s -> s.contains(nickname))
                .map(s->UUID.fromString(StringUtils.substringAfterLast(s,"|")))
                .findFirst();
    }

    public void saveMatch(Match match){

        if(!isDebugMode.get()) {

            String gameName = match.getSaveName();
            String folderAbsolutePath = Paths.get(matchesFolderName).toAbsolutePath().toString();
            String path = folderAbsolutePath + '/' + gameName + ".json";

            try {
                Serializator.serializeMatch(match, path);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void saveSessionController() {


        if(!isDebugMode.get()) {

            String sessionName = "session";
            String folderAbsolutePath = Paths.get(sessionFolderName).toAbsolutePath().toString();
            String path = folderAbsolutePath + '/' + sessionName + ".json";

            try {
                Serializator.serializeSession(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Optional<Match> loadMatch(UUID gameId) {

        File folder = new File(matchesFolderName);
        return Arrays.stream(folder.listFiles()).filter(File::isFile)
                .filter(file -> file.getName().contains(gameId.toString()))
                .findFirst()
                .map((file -> Deserializator.deserializeMatch(file.getName() + ".json")));

    }

    public void deleteGame(UUID gameId){
        File folder = new File(Paths.get(matchesFolderName).toAbsolutePath().toString());
        Arrays.stream(folder.listFiles()).filter(File::isFile)
                .filter(file -> file.getName().contains(gameId.toString()))
                .findFirst().ifPresent(File::delete);
    }

    public boolean checkGameTimeout(UUID gameId){

        long currentTime =  System.currentTimeMillis();
        long disconnectionTime = matchesDisconnectionTimes.get(gameId);
        int elapsedTimeInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(currentTime - disconnectionTime);
        return elapsedTimeInSeconds > maxSecondsOffline;

    }

    public void registerDisconnection(Match match){
        matchesDisconnectionTimes.put(match.getMatchId(),System.currentTimeMillis());
        match.stopGame();

    }

    public void setPlayerOffline(ClientHandler clientHandler){

        if(clientHandler.getMatch().isPresent()){
            Match match = clientHandler.getMatch().get();
            match.setOfflineUser(clientHandler);

            if(match.areAllPlayersOffline())
                registerDisconnection(match);
        }

    }




}
