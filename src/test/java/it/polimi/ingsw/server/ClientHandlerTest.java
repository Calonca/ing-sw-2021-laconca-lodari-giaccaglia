package it.polimi.ingsw.server;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.messages.servertoclient.ClientMessage;
import it.polimi.ingsw.client.messages.servertoclient.CreatedMatchStatus;
import it.polimi.ingsw.network.messages.clienttoserver.ClientToServerMessage;
import it.polimi.ingsw.network.messages.clienttoserver.CreateMatchRequest;
import org.junit.Before;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientHandlerTest {
    Socket server;
    ObjectOutputStream output;
    ObjectInputStream input;

    @Before
    public void setUp() throws InterruptedException, IOException {
        new Thread(() -> Server.main(null)).start();
        Thread.sleep(100);
        try {
            server = new Socket("127.0.0.1",7890);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        output = new ObjectOutputStream(server.getOutputStream());
        input = new ObjectInputStream(server.getInputStream());
    }


    public void generalTest() throws IOException, ClassNotFoundException {
        ClientToServerMessage request = new CreateMatchRequest(2,"a");
        output.writeChars(request.serialized());
        String next = input.readObject().toString();
        ClientMessage command = ClientMessage.deserialize(next);
        assertEquals(new CreatedMatchStatus(request,true),command);
        //command.processMessage(this);
    }


    public void test2() throws InterruptedException, FileNotFoundException {
        new Thread(() -> Server.main(null)).start();
        Thread.sleep(100);
        File file = new File("src/main/resources/redirectInputTest/DefaultClientInput");
        System.setIn(new FileInputStream(file));
        //System.setIn(System.in);
        Client.main(null);
    }

    public void setInput(String s){
        System.setIn( new ByteArrayInputStream(s.getBytes()));
    }
}