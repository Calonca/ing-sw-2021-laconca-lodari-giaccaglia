package it.polimi.ingsw.client.view.CLI;

import com.google.gson.Gson;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CLIBuilderViewBuilderTest {
    CLI cli;
    Gson gson = new Gson();


    @Before
    public void setUp() {
        cli =new CLI(new Client());
    }


    public void selectionTest(){
        AtomicInteger test = new AtomicInteger();
        Option o1 = Option.from("Op1","sub",()-> test.set(1));
        Option o2 = Option.from("Op2","2",()->test.set(2));
        Option o3 = Option.from("Op3","3",()->test.set(3));
        OptionList optionList1 = new OptionList(),optionList2 = new OptionList();

        o1.getPerformer().run();
        assertEquals(1,test.get());

        optionList1.addOption(o1);
        cli.setOptionList(CLIPos.TOP,optionList1);

        optionList1.addOption(o2);
        optionList1.addOption(o3);
        cli.setOptionList(CLIPos.CENTER,optionList2);

        cli.performLastChoice();
        assertEquals(1,test.get());

        cli.performLastChoice();
        assertEquals(1,test.get());

        cli.performLastChoice();
        assertEquals(1,test.get());

    }

}