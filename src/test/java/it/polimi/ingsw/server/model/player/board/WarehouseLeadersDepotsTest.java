package it.polimi.ingsw.server.model.player.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.server.model.LeaderDepot;
import it.polimi.ingsw.server.model.Resource;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class WarehouseLeadersDepotsTest {
    private WarehouseLeadersDepots house;

    @Before
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setUp() throws Exception {
        house = new WarehouseLeadersDepots();
        house.addDepot(new LeaderDepot(house.getNextGlobalPosition(), Resource.GOLD));
        Pair[] pa = Stream.of(
                new Pair<>(0,Resource.STONE),
                new Pair<>(2,Resource.STONE),
                new Pair<>(5,Resource.GOLD),
                new Pair<>(7,Resource.GOLD)
        ).toArray(Pair[]::new);
        house.addResources(pa);
    }

    @Test
    public void setUpCorrectly() {
        assertEquals(8,house.getNextGlobalPosition());
        assertEquals(4,house.getOccupiedSpotsNum());
        assertEquals("{0:[1],1:[],2:[1],3:[],4:[],5:[3,4,6],6:[],7:[3,4,6]}",
                house.allAvbPosToJson().replace("\"","") );
        assertEquals("" +
                        "{0:[{key:STONE,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:STONE,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "3:[{key:EMPTY,value:false},{key:GOLD,value:false}]}",
                house.structuredTableJson().replace("\"","") );
    }

    @Test
    public void moveResource() {
        house.moveResource(0,1);
        assertEquals(8,house.getNextGlobalPosition());
        assertEquals(4,house.getOccupiedSpotsNum());
        assertEquals("{0:[],1:[0],2:[0],3:[],4:[],5:[0,3,4,6],6:[],7:[0,3,4,6]}",
                house.allAvbPosToJson().replace("\"","") );
        assertEquals("" +
                        "{0:[{key:EMPTY,value:false}]," +
                        "1:[{key:STONE,value:false},{key:STONE,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "3:[{key:EMPTY,value:false},{key:GOLD,value:false}]}",
                house.structuredTableJson().replace("\"","") );
    }

    @Test
    public void addDepot() {
        house.addDepot(new LeaderDepot(house.getNextGlobalPosition(),Resource.GOLD));
        assertEquals(10,house.getNextGlobalPosition());
        assertEquals(4,house.getOccupiedSpotsNum());
        assertEquals("{0:[1],1:[],2:[1],3:[],4:[],5:[3,4,6,8,9],6:[],7:[3,4,6,8,9],8:[],9:[]}",
                house.allAvbPosToJson().replace("\"","") );
        assertEquals("" +
                        "{0:[{key:STONE,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:STONE,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "3:[{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "4:[{key:EMPTY,value:false},{key:EMPTY,value:false}]}",
                house.structuredTableJson().replace("\"","") );
    }


    @Test
    public void enoughResourcesForProductions() {
        assertTrue(house.enoughResourcesForProductions(new int[]{1,0,0,1}));

        assertFalse(house.enoughResourcesForProductions(new int[]{1,1,0,0}));

        assertFalse(house.enoughResourcesForProductions(new int[]{1,0,0,4}));
    }

    @Test
    public void removeSelected() {
        house.setSelected(true,0);
        house.toggleSelected(7);
        house.setSelected(false,0);
        house.setSelected(true,2);
        assertEquals(8,house.getNextGlobalPosition());
        assertEquals(4,house.getOccupiedSpotsNum());
        assertEquals("{0:[1],1:[],2:[1],3:[],4:[],5:[3,4,6],6:[],7:[3,4,6]}",
                house.allAvbPosToJson().replace("\"","") );
        assertEquals("" +
                        "{0:[{key:STONE,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:STONE,value:true}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "3:[{key:EMPTY,value:false},{key:GOLD,value:true}]}",
                house.structuredTableJson().replace("\"","") );


        //To remove, 2,7
        house.removeSelected();
        assertEquals(8,house.getNextGlobalPosition());
        assertEquals(2,house.getOccupiedSpotsNum());
        assertEquals("{0:[1,2],1:[],2:[],3:[],4:[],5:[1,2,3,4,6,7],6:[],7:[]}",
                house.allAvbPosToJson().replace("\"","") );
        assertEquals("" +
                        "{0:[{key:STONE,value:false}]," +
                        "1:[{key:EMPTY,value:false},{key:EMPTY,value:false}]," +
                        "2:[{key:EMPTY,value:false},{key:EMPTY,value:false},{key:GOLD,value:false}]," +
                        "3:[{key:EMPTY,value:false},{key:EMPTY,value:false}]}",
                house.structuredTableJson().replace("\"","") );
    }



}