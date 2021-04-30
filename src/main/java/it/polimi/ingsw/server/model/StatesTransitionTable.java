package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.clienttoserver.events.Validable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StatesTransitionTable {
    private Map<State, Map<Validable, GameStrategy>> table;

    public static StatesTransitionTable singlePlayer() throws IOException {
        Gson gson = new Gson();
        String SinglePlayerStatesTransitionTable = Files.readString(Path.of("src/main/resources/config/SinglePlayerStatesTransitionTable/.json"), StandardCharsets.US_ASCII);
        return gson.fromJson(SinglePlayerStatesTransitionTable, StatesTransitionTable.class);
    }

    public static StatesTransitionTable multiPlayer() throws IOException {
        Gson gson = new Gson();
        String MultiPlayerStatesTransitionTable = Files.readString(Path.of("src/main/resources/config/MultiPlayerStatesTransitionTable/.json"), StandardCharsets.US_ASCII);
        return gson.fromJson(MultiPlayerStatesTransitionTable, StatesTransitionTable.class);
    }

    public static StatesTransitionTable fromIsSinglePlayer(boolean isSinglePlayer) throws IOException {
        return isSinglePlayer?StatesTransitionTable.singlePlayer():StatesTransitionTable.multiPlayer();
    }

    private void saveSinglePlayerStatesTransitionTable () throws IOException {

        //table.put(State.INITIAL_PHASE,)    ;
        //
        //Writer writer = new FileWriter("src/main/resources/config/SinglePlayerTable.json");
        //Gson gson = new GsonBuilder().create();
        //gson.toJson(this, writer);
        //writer.flush(); //flush data to file   <---
        //writer.close(); //close write          <---
    }

    private void saveMultiPlayerStatesTransitionTable() throws IOException{

    }

    public GameStrategy getStrategy(State state, Validable event){
        return table.get(state).get(event);
    }

}
