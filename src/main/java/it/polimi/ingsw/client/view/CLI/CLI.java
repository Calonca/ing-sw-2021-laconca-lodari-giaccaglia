package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Builds the CLI
 * Usage: add the elements to the cli by calling addOption()
 * You can then select an option and execute the code contained in that option
 * Todo add selection with arrows
 */
public class CLI {
    private final Client client;
    private Title title;
    private Optional<Spinner> spinner;
    private OptionList[] optionListAtPos;
    private Optional<Option> lastChoice=Optional.empty();
    public final AtomicBoolean stopASAP;
    private boolean isTakingInput = false;
    private Thread inputThread;
    private String inputMessage;


    public CLI(Client client) {
        optionListAtPos = Stream.generate(OptionList::new).limit(CLIPos.values().length).toArray(OptionList[]::new);
        this.client = client;
        stopASAP = new AtomicBoolean(false);
        spinner = Optional.empty();
    }

    public void resetCLI(){
        clearOptions(this);
    }

    void setOptionList(CLIPos pos,OptionList optionList){
        optionList.setCLIAndAddToPublishers(this, client);
        optionListAtPos[pos.ordinal()]=optionList;
    }

    private static void clearOptions(CLI cli)
    {
        if (cli.title!=null)
            cli.title.removeFromPublishers(cli.client);
        cli.title = null;
        cli.spinner.ifPresent(s->s.removeFromPublishers(cli.client));
        cli.spinner = Optional.empty();
        Arrays.stream(cli.optionListAtPos).forEach(o->o.removeFromPublishers(cli.client));
        cli.optionListAtPos = Stream.generate(OptionList::new).limit(CLIPos.values().length).toArray(OptionList[]::new);
    }

    public OptionList getOptionsAt(CLIPos cliPos){
        return optionListAtPos[cliPos.ordinal()];
    }

    public void performLastChoice(){
        lastChoice.ifPresent(Option::perform);
    }

    public void update(){
        displayWithDivider();
    }

    public void setTitle(Title title){
        if (this.title!=null)
            title.removeFromPublishers(client);
        this.title = title;
        this.title.setCLIAndAddToPublishers(this,client);
    }

    public void setSpinner(Spinner spinner){
        this.spinner.ifPresent(s->s.removeFromPublishers(client));
        this.spinner = Optional.of(spinner);
        spinner.setCLIAndAddToPublishers(this, client);
    }

    private void print(String s){
        System.out.println(s);
    }

    public void printError(String error){
        System.out.println(Color.colorString(error,Color.ANSI_RED));
    }

    public void getInputAndLaunchRunnable(String message, RunnableWithString rs){
        inputThread = null;
        Runnable r = ()-> {
            print(Color.colorString(message,Color.ANSI_GREEN));
            putEndDiv();
            isTakingInput = true;
            String s = getInAndCallRunnable();
            isTakingInput = false;
            rs.setString(s);
            rs.runCode();
        };
        inputMessage = message;
        inputThread = new Thread(r);
    }

    private String getInAndCallRunnable(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void displayWithScroll(){
        scroll();
        display();
    }

    public void displayWithDivider(){
        putDivider();
        display();
    }

    /**
     * Used to refresh the screen
     */
    private void display(){

        if (title!=null){
            print(title.toString()+"\n");}

        String spaces = "                                                    ";
        String finalSpaces = spaces;
        getOptionsAt(CLIPos.TOP).toStringStream()
                .map(s -> finalSpaces +s.replace("\n","\n"+finalSpaces))
                .forEach(this::print);

        spaces ="                                                    ";
        String finalSpaces1 = spaces;
        getOptionsAt(CLIPos.CENTER).toStringStream()
                .map(s -> finalSpaces1 +s.replace("\n","\n"+finalSpaces1))
                .forEach(this::print);

        getOptionsAt(CLIPos.BOTTOM_LEFT).toStringStream()
                .forEach(this::print);

        spaces = "                                                    ";
        String finalSpaces2 = spaces;
        getOptionsAt(CLIPos.BOTTOM_RIGHT).toStringStream()
                .map(s -> finalSpaces2 +s.replace("\n","\n"+finalSpaces2))
                .forEach(this::print);
        
        spinner.ifPresent(spinner1 -> print(spinner1.toString()));

        if (isTakingInput){
            print(Color.colorString(inputMessage,Color.ANSI_GREEN));
            putEndDiv();}
        else if (inputThread!=null) {
            inputThread.start();
            inputThread=null;
        } else putEndDiv();
    }

    @Override
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
        display();
        System.out.flush();
        System.setOut(System.out);
        return baos.toString();
    }

    static void cleanConsole() {
        final String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Runtime.getRuntime().exec("clear");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void putDivider(){
        print("||-----------------------------------------------------------");
    }

    public void putEndDiv(){
        print("-----------------------------------------------------------||");
    }

    public void scroll(){
        for (int i = 0; i < 40; i++) {
            System.out.println();}
    }

    public void setLastChoice(Optional<Option> integerOptionPair ) {
        this.lastChoice = integerOptionPair;
    }

}
