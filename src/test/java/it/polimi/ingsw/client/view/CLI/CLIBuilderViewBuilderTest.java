package it.polimi.ingsw.client.view.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.VerticalListBody;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CLIBuilderViewBuilderTest {
    CLI cli;
    Gson gson = new Gson();


    @Before
    public void setUp() {
        cli =new CLI(Client.getInstance());
    }


    public void selectionTest(){
        AtomicInteger test = new AtomicInteger();
        Option o1 = Option.from("Op1","sub",()-> test.set(1));
        Option o2 = Option.from("Op2","2",()->test.set(2));
        Option o3 = Option.from("Op3","3",()->test.set(3));
        VerticalListBody optionList1 = new VerticalListBody();
        VerticalListBody optionList2 = new VerticalListBody();

        o1.perform();
        assertEquals(1,test.get());

        optionList1.addOption(o1);
        cli.setBody(optionList1);

        optionList1.addOption(o2);
        optionList1.addOption(o3);
        cli.setBody(optionList2);

        cli.performLastChoice();
        assertEquals(1,test.get());

        cli.performLastChoice();
        assertEquals(1,test.get());

        cli.performLastChoice();
        assertEquals(1,test.get());

    }

}