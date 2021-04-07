package it.polimi.ingsw.server.model.player;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class PopeFavourTileTest {

    List<PopeFavourTile> favourTiles;
    PopeFavourTile testTile;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        String json = Files.readString(Path.of("src/main/resources/config/FaithTrackConfig.json"), StandardCharsets.US_ASCII);
        JsonObject jsonFaithTrackClass = new Gson().fromJson(json, JsonObject.class);
        JsonElement jsonFaithTrackList = jsonFaithTrackClass.get("tiles");
        Type myListType = new TypeToken<List<PopeFavourTile>>(){}.getType();
        favourTiles = new Gson().fromJson(jsonFaithTrackList, myListType);
    }

    @Test
    public void getVictoryPoints() {

        testTile = favourTiles.get(0);
        assertEquals(testTile.getVictoryPoints(), 2);

        testTile = favourTiles.get(1);
        assertEquals(testTile.getVictoryPoints(), 3);

        testTile = favourTiles.get(2);
        assertEquals(testTile.getVictoryPoints(), 4);

    }

    @Test
    public void getTileState() {

        testTile = favourTiles.get(0);
        assertEquals(testTile.getTileState(), TileState.INACTIVE);

        testTile = favourTiles.get(1);
        assertEquals(testTile.getTileState(), TileState.INACTIVE);

        testTile = favourTiles.get(2);
        assertEquals(testTile.getTileState(), TileState.INACTIVE);

    }

    @Test
    public void setTileState() {

        favourTiles.get(0).setTileState(TileState.ACTIVE);
        assertEquals( favourTiles.get(0).getTileState(), TileState.ACTIVE);

        favourTiles.get(1).setTileState(TileState.INACTIVE);
        assertEquals(favourTiles.get(1).getTileState(), TileState.INACTIVE);

        favourTiles.get(2).setTileState(TileState.DISCARDED);
        assertEquals(favourTiles.get(2).getTileState(), TileState.DISCARDED);

    }
}