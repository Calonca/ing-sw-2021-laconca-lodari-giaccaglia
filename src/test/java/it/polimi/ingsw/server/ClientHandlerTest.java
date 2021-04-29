package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.servertoclient.ClientMessage;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import it.polimi.ingsw.network.messages.servertoclient.CreatedMatchStatus;
import it.polimi.ingsw.network.messages.servertoclient.MatchesData;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

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
        //try {
        //    server2 = new Socket("127.0.0.1",Server.SOCKET_PORT);
        //} catch (IOException e) {
        //    System.out.println("server unreachable");
        //    return;
        //}
        //output2 = new ObjectOutputStream(server2.getOutputStream());
        //input2 = new ObjectInputStream(server2.getInputStream());
    }

    @Test
    public void createMatch() throws IOException, ClassNotFoundException {
        String matchData = input1.readObject().toString();
        assertEquals(
                mapWithoutIDFrom(new MatchesData(new Pair[]{}).serialized()),
                mapWithoutIDFrom(matchData));

        ClientToServerMessage request = new CreateMatchRequest(2,"Name1");
        output1.writeObject(request.serialized());
        matchData = input1.readObject().toString();
        Map<String, Object> inputMap = mapWithoutIDFrom(matchData);
        UUID uuid = UUID.fromString((String) ((LinkedTreeMap) ((ArrayList) inputMap.get("matchesData")).get(0)).get("key"));
        Pair<UUID,String[]> data = new Pair<>(uuid,new String[]{"Name1",null});
        assertEquals(
                mapWithoutIDFrom(new MatchesData(new Pair[]{data}).serialized()),
                mapWithoutIDFrom(matchData));
        String command = input1.readObject().toString();
        assertEquals(
                mapWithoutIDFrom(new CreatedMatchStatus(request,true,null).serialized()),//
                mapWithoutIDFrom(command));
        //Todo test false validation
    }

    public void setInput(String s){
        System.setIn( new ByteArrayInputStream(s.getBytes()));
    }

    public Map<String, Object> mapWithoutIDFrom(String s){
        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> map = new Gson().fromJson(s, mapType);
        map.remove("identifier");
        return map;
    }
}