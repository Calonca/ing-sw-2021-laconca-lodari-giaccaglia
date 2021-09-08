package it.polimi.ingsw.server.controller;


import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.utils.Deserializator;
import it.polimi.ingsw.server.utils.Serializator;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class SessionController {

    private static SessionController singleInstance = null;
    private static final String SESSION_PATH = "session.json";
    private static final int MAX_SECONDS_OFFLINE = 1800000; //30 minutes
    private final ConcurrentHashMap<UUID,Match> matches = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Long> matchesDisconnectionTimes = new ConcurrentHashMap<>();
    private transient Thread deadMatchesChecker;
    private final transient List<ClientHandler> clientsNotInLobby = new ArrayList<>();

    private final AtomicBoolean isDebugMode = new AtomicBoolean(false);


    public static SessionController getInstance()
    {
        if (Objects.isNull(singleInstance)) {
            createSessionController();
            startDeadMatchesChecker();
        }
        return singleInstance;
    }
    private static void createSessionController(){

        reloadSessionIfPresent().ifPresentOrElse(
                loadedSession -> singleInstance = loadedSession,
                () -> singleInstance = new SessionController());

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
        clientsNotInLobby.add(clientHandler);
    }

    public Optional<Match> getMatch(UUID matchId){
        return Optional.of(matches.get(matchId));
    }

    public synchronized Match addNewMatch(int maxPlayers, ClientHandler clientHandler){
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
            int playerIndex = match.addPlayer(nickname,clientHandler);
            clientHandler.setMatch(match);
            return playerIndex;
        }

        else return -1;
    }

    public void startMatchAndNotifyStateIfPossible(Match match) {

        if (!match.canAddPlayer()) {
            clientsNotInLobby.removeAll(match.clientsStream().collect(Collectors.toList()));
            match.startGame();
        }
    }

    public void sendUpdatedAvailableMatches(){

        clientsNotInLobby.forEach(client -> client.sendAnswerMessage(new MatchesData(SessionController.getInstance().matchesData(client))));
    }

    public void notifyPlayersInLobby(){
        clientsNotInLobby.forEach(
                clientHandler -> clientHandler.sendAnswerMessage(new MatchesData(matchesData(clientHandler)))
        );
    }
    //                  isSaved    onlineUsers, offlineUsers
    public Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData(ClientHandler clientHandler){

        return matches.entrySet().stream()
                .filter(match->


                        (clientHandler.getMatch().isPresent() && clientHandler.getMatch().get().getMatchId().equals(match.getKey())) // match was saved and was not restored
                        ||
                        (match.getValue().isNicknameAvailable(clientHandler.getNickname()) && match.getValue().canAddPlayer()) // player can join match if there is enough space and nickname constraints are met

                        || (match.getValue().wasClientInMatch(clientHandler.getNickname())) // client was in match



                ).sorted((m1, m2) -> m2.getValue().getCreatedTime().compareTo(m1.getValue().getCreatedTime()))
                .limit(20)
                .collect(Collectors.toMap(
                        entry -> {
                            UUID matchId = entry.getKey();
                            Boolean isSavedMatch = matches.get(matchId).wasClientInMatch(clientHandler.getNickname()) && matches.get(matchId).getOnlinePlayers().length == 0;
                            return new Pair<>(matchId, isSavedMatch);
                        },

                        entry-> new Pair<>(entry.getValue().getOnlinePlayers(), entry.getValue().getOfflinePlayers())
                ));
    }

    private static Optional<SessionController> reloadSessionIfPresent() {

        File f = new File(SESSION_PATH);
        if(f.exists() && !f.isDirectory()) {
                SessionController session = Deserializator.deserializeSession(SESSION_PATH);

            if(Objects.nonNull(session)) {
                session.matches.values().forEach(Match::restoreMatch);
                return Optional.of(session);
            }
            else return Optional.empty();
            }

            else
                return Optional.empty();
    }

    public void saveSessionController() {

        if(!isDebugMode.get()) {

            try {
                Serializator.serializeSession(SESSION_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    public synchronized void deleteGame(UUID gameId){
        matches.remove(gameId);
        matchesDisconnectionTimes.remove(gameId);
        saveSessionController();
    }

    public boolean checkGameTimeout(UUID gameId){

        long currentTime =  System.currentTimeMillis();
        long disconnectionTime;
        if(Objects.isNull(matchesDisconnectionTimes.get(gameId)))
            return false;
        else
           disconnectionTime = matchesDisconnectionTimes.get(gameId);

        long elapsedTimeInSeconds = currentTime - disconnectionTime;
        return elapsedTimeInSeconds > MAX_SECONDS_OFFLINE;

    }

    public synchronized void registerDisconnection(Match match){
        matchesDisconnectionTimes.put(match.getMatchId(), System.currentTimeMillis());
        match.stopGameIfPresent();

    }

    public void setPlayerOffline(ClientHandler clientHandler){

        if(clientHandler.getMatch().isPresent()){
            Match match = clientHandler.getMatch().get();
            match.setOfflineUser(clientHandler);

            if(match.areAllPlayersOffline())
                registerDisconnection(match);
        }
        else
            removeClientIfPresent(clientHandler);
    }
    
    public void removeClientIfPresent(ClientHandler clientHandler){
        clientsNotInLobby.remove(clientHandler);
    }

    private static void startDeadMatchesChecker(){

        SessionController sessionController = getInstance();

        if(Objects.isNull(sessionController.deadMatchesChecker)){

            sessionController.deadMatchesChecker = new Thread(() ->{


                try {

                    while(true){
                        Thread.sleep(300000);  // check every 5 minutes

                        for (UUID uuid : sessionController.matches.keySet()) {

                          if(!Objects.isNull(sessionController.matches.get(uuid)) && sessionController.checkGameTimeout(uuid)){
                                sessionController.deleteGame(uuid);

                        }
                      }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });


           sessionController.deadMatchesChecker.start();
        }

    }



}
