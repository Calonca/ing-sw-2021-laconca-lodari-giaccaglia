package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.clienttoserver.JoinMatchRequest;
import it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus;
import it.polimi.ingsw.network.messages.servertoclient.JoinStatus;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.*;

import static org.junit.Assert.*;

public class ClientHandlerTest {
    Socket server1,server2;
    ObjectOutputStream output1,output2;
    ObjectInputStream input1,input2;

    @Before
    public void setUp() throws InterruptedException, IOException {
        Thread serverThread = new Thread(() -> Server.main(null));
        serverThread.start();
        Thread.sleep(100);
        try {
            server1 = new Socket("127.0.0.1",Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        output1 = new ObjectOutputStream(server1.getOutputStream());
        input1 = new ObjectInputStream(server1.getInputStream());
        try {
            server2 = new Socket("127.0.0.1",Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        output2 = new ObjectOutputStream(server2.getOutputStream());
        input2 = new ObjectInputStream(server2.getInputStream());
    }

    @After
    public void after() throws IOException {
        server1.close();
        output1.close();
        input1.close();
        server2.close();
        output2.close();
        input2.close();
    }



    public void createMatch() throws IOException, ClassNotFoundException {

        //1 Received matches data
        Map<UUID,String[]> matchesData1 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData1)),
                toJsonObject(input1.readObject().toString()));

        //2 Received matches data
        Map<UUID,String[]> matchesData2 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData2)),
                toJsonObject(input2.readObject().toString()));

        //1 Send create request
        ClientToServerMessage request = new CreateMatchRequest(2,"Name1");
        output1.writeObject(request.serialized());
        //1 Received matches data
        assertEquals(MatchesData.class.getSimpleName(),
                toJsonObject(input1.readObject().toString()).get("type").getAsString());
        //2 Received matches data
        assertEquals(MatchesData.class.getSimpleName(),
                toJsonObject(input2.readObject().toString()).get("type").getAsString());
        //1 Receive create match status
        JsonObject jsonObject = toJsonObject(input1.readObject().toString());
        UUID matchId = UUID.fromString(jsonObject.get("matchId").getAsString());
        JsonObject expected = toJsonObject(new CreatedMatchStatus(request,matchId,null));
        assertEquals(expected,
                jsonObject);

        //2 Send join request
        request = new JoinMatchRequest(matchId,"Name2");
        output2.writeObject(request.serialized());
        //1 Received state message
        assertEquals(SETUP_PHASE.class.getSimpleName(),
                toJsonObject(input1.readObject().toString()).get("stateMessage").getAsJsonObject().get("type").getAsString());
        //1 Received matches data
        assertEquals(MatchesData.class.getSimpleName(),
                toJsonObject(input1.readObject().toString()).get("type").getAsString());
        //2 Receive join status
        jsonObject = toJsonObject(input2.readObject().toString());
        expected = toJsonObject(new JoinStatus(request,matchId,null,1));
        assertEquals(expected,
                jsonObject);

    }



    public void singlePlayerCreation() throws IOException, ClassNotFoundException {

        //1 Received matches data
        Map<UUID,String[]> matchesData1 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData1)),
                toJsonObject(input1.readObject().toString()));

        //2 Received matches data
        Map<UUID,String[]> matchesData2 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData2)),
                toJsonObject(input2.readObject().toString()));

        //1 Send create request
        ClientToServerMessage request = new CreateMatchRequest(1,"Name1");
        output1.writeObject(request.serialized());
        //1 Received state message
        assertEquals(SETUP_PHASE.class.getSimpleName(),
                toJsonObject(input1.readObject().toString()).get("stateMessage").getAsJsonObject().get("type").getAsString());
        //2 Received matches data
        assertEquals(MatchesData.class.getSimpleName(),
                toJsonObject(input2.readObject().toString()).get("type").getAsString());
        //1 Receive create match status
        JsonObject jsonObject = toJsonObject(input1.readObject().toString());
        UUID matchId = UUID.fromString(jsonObject.get("matchId").getAsString());
        JsonObject expected = toJsonObject(new CreatedMatchStatus(request,matchId,null));
        assertEquals(expected,
                jsonObject);
    }


    public void testEvents() throws IOException, ClassNotFoundException {
        String matchData = input1.readObject().toString();
        ClientToServerMessage request = new CreateMatchRequest(2,"Name1");

    }

    public void setInput(String s){
        System.setIn( new ByteArrayInputStream(s.getBytes()));
    }

    public JsonObject toJsonObject(String s){
        JsonObject jsonObject = new Gson().fromJson(s,JsonObject.class);
        jsonObject.remove("identifier");
        return jsonObject;
    }

    public JsonObject toJsonObject(ServerToClientMessage s){
        JsonObject jsonObject = new Gson().fromJson(s.serialized(),JsonObject.class);
        jsonObject.remove("identifier");
        return jsonObject;
    }

}