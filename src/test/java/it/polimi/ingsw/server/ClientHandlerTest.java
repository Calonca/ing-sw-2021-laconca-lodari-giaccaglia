package it.polimi.ingsw.server;

import com.google.gson.*;
import it.polimi.ingsw.network.messages.clienttoserver.*;
import it.polimi.ingsw.network.messages.servertoclient.*;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import java.net.Socket;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class ClientHandlerTest {
    Socket server1,server2;
    ObjectOutputStream output1,output2;
    ObjectInputStream input1,input2;
    private static final int testPort = 2653;

    @Before
    public void setUp() throws InterruptedException, IOException {
        Thread serverThread = new Thread(() -> Server.main(new String[]{"-s",String.valueOf(testPort)}));
        serverThread.start();
        Thread.sleep(100);
        try {
            server1 = new Socket("127.0.0.1",testPort);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        output1 = new ObjectOutputStream(server1.getOutputStream());
        input1 = new ObjectInputStream(server1.getInputStream());
        try {
            server2 = new Socket("127.0.0.1",testPort);
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
        Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData1 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData1)),
                toJsonObject(input1.readObject().toString()));

        //2 Received matches data
        Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData2 = new HashMap<>();
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
        JsonObject expected = toJsonObject(new CreatedMatchStatus(matchId,null));
        assertEquals(expected,
                jsonObject);

        //2 Send join request
        request = new JoinMatchRequest(matchId,"Name2");
        output2.writeObject(request.serialized());
        //1 Received matches data
        assertEquals(MatchesData.class.getSimpleName(),
                toJsonObject(input1.readObject().toString()).get("type").getAsString());
        //1 Received matches data
        assertEquals(MatchesData.class.getSimpleName(),
                toJsonObject(input2.readObject().toString()).get("type").getAsString());
        //2 Receive join status
        jsonObject = toJsonObject(input2.readObject().toString());
        expected = toJsonObject(new JoinStatus(matchId,null,1));
        assertEquals(expected,
                jsonObject);
        //1 Received state message
        JsonObject obj = toJsonObject(input1.readObject().toString());
        assertEquals(StateInNetwork.class.getSimpleName(),obj.get("type").getAsString());
        assertEquals("SETUP_PHASE",obj.get("state").getAsString());
    }

    public void singlePlayerCreation() throws IOException, ClassNotFoundException {

        //1 Received matches data
        Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData1 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData1)),
                toJsonObject(input1.readObject().toString()));

        //2 Received matches data
        Map<Pair<UUID,Boolean>,Pair<String[], String[]>> matchesData2 = new HashMap<>();
        assertEquals(
                toJsonObject(new MatchesData(matchesData2)),
                toJsonObject(input2.readObject().toString()));

        //1 Send create request
        ClientToServerMessage request = new CreateMatchRequest(1,"Name1");
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
        JsonObject expected = toJsonObject(new CreatedMatchStatus(matchId,null));
        assertEquals(expected,
                jsonObject);
        //1 Received state message
        JsonObject obj = toJsonObject(input1.readObject().toString());
        assertEquals(StateInNetwork.class.getSimpleName(),obj.get("type").getAsString());
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