package it.polimi.ingsw.client.view.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CLIBuilderTest {
    CLIBuilder cliBuilder;
    Gson gson = new Gson();


    @Before
    public void setUp() {
        cliBuilder=new CLIBuilder();
    }

    @Test
    public void selectionTest(){
        AtomicInteger test = new AtomicInteger();
        Option o1 = new Option("Op1","sub",()-> test.set(1));
        Option o2 = new Option("Op2","2",()->test.set(2));
        Option o3 = new Option("Op3","3",()->test.set(3));
        cliBuilder.addOption(CLIPos.TOP,o1);
        cliBuilder.addOption(CLIPos.CENTER,o2);
        cliBuilder.addOption(CLIPos.CENTER,o3);

        cliBuilder.selectOptionAtGlobalPosition(0);
        cliBuilder.performLastChoice();
        assertEquals(1,test.get());

        cliBuilder.selectOptionAtGlobalPosition(2);
        cliBuilder.performLastChoice();
        assertEquals(3,test.get());

        cliBuilder.selectOptionAtGlobalPosition(1);
        cliBuilder.performLastChoice();
        assertEquals(2,test.get());

    }

}