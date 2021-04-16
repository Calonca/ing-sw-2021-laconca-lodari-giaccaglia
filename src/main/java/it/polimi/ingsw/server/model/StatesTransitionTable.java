package it.polimi.ingsw.server.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.controller.strategy.GameStrategy;
import it.polimi.ingsw.server.messages.servertoclient.events.Event;
import it.polimi.ingsw.server.model.player.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class StatesTransitionTable {
    private Map<State,Map<Event, GameStrategy>> table;

    public static StatesTransitionTable singlePlayer() throws IOException {
        Gson gson = new Gson();
        String SinglePlayerStatesTransitionTable = Files.readString(Path.of("src/main/resources/SinglePlayerStatesTransitionTable/.json"), StandardCharsets.US_ASCII);
        return gson.fromJson(SinglePlayerStatesTransitionTable, StatesTransitionTable.class);
    }

    public static StatesTransitionTable multiPlayer() throws IOException {
        Gson gson = new Gson();
        String MultiPlayerStatesTransitionTable = Files.readString(Path.of("src/main/resources/MultiPlayerStatesTransitionTable/.json"), StandardCharsets.US_ASCII);
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

    public GameStrategy getStrategy(State state, Event event){
        return table.get(state).get(event);
    }

}
