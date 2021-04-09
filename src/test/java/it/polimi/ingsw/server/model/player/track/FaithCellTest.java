package it.polimi.ingsw.server.model.player.track;

import com.google.gson.Gson;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class FaithCellTest {
    List<FaithCell> myList;
    FaithCell testFaithCell;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        String json = Files.readString(Path.of("src/main/resources/config/FaithTrackConfig.json"), StandardCharsets.US_ASCII);
        JsonObject jsonFaithTrackClass = gson.fromJson(json, JsonObject.class);
        JsonElement jsonFaithTrackList = jsonFaithTrackClass.get("track");
        Type myListType = new TypeToken<List<FaithCell>>() {
        }.getType();
        myList = gson.fromJson(jsonFaithTrackList, myListType);
    }


    @Test
    public void testGetPoints() {

        testFaithCell = myList.get(8);
        assertEquals(testFaithCell.getPoints(), 0);

        testFaithCell = myList.get(10);
        assertEquals(testFaithCell.getPoints(), 0);

        testFaithCell = myList.get(18);
        assertEquals(testFaithCell.getPoints(), 12);

        testFaithCell = myList.get(21);
        assertEquals(testFaithCell.getPoints(), 16);

        testFaithCell = myList.get(14);
        assertEquals(testFaithCell.getPoints(), 0);

    }

    @Test
    public void testGetZone() {

        testFaithCell = myList.get(8);
        assertEquals(testFaithCell.getZone(), FaithZone.ZONE1);

        testFaithCell = myList.get(10);
        assertEquals(testFaithCell.getZone(), FaithZone.NORMAL);

        testFaithCell = myList.get(18);
        assertEquals(testFaithCell.getZone(), FaithZone.NORMAL);

        testFaithCell = myList.get(21);
        assertEquals(testFaithCell.getZone(), FaithZone.ZONE3);

        testFaithCell = myList.get(14);
        assertEquals(testFaithCell.getZone(), FaithZone.ZONE2);

    }

    @Test
    public void testIsPopeSpace() {

        testFaithCell = myList.get(8);
        assertTrue(testFaithCell.isPopeSpace());

        testFaithCell = myList.get(10);
        assertFalse(testFaithCell.isPopeSpace());

        testFaithCell = myList.get(18);
        assertFalse(testFaithCell.isPopeSpace());

        testFaithCell = myList.get(21);
        assertFalse(testFaithCell.isPopeSpace());

        testFaithCell = myList.get(14);
        assertFalse(testFaithCell.isPopeSpace());

    }

}
