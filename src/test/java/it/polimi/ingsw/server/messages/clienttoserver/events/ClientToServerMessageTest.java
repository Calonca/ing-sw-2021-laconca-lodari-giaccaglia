package it.polimi.ingsw.server.messages.clienttoserver.events;

import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientToServerMessageTest {

    @Test
    public void serialized() {
        CreateMatchRequest matchRequest = new CreateMatchRequest(2,"testName");
        String serialized = matchRequest.serialized();
        assertNotEquals("{\"type\":\"CreateMatchRequest\",\"maxPlayers\":2,\"nickName\":\"testName\",\"identifier\":\"",serialized.substring(0,81));
        assertEquals("{\"type\":\"CreateMatchRequest\",\"maxPlayers\":2,\"nickName\":\"testName\",\"identifier\":\"",serialized.substring(0,80));
    }
}