package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Builds the CLI.<br>
 * Usage: Add {@link it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem elements}
 * like optionList, body, title, spinner, and then call {@link #displayWithDivider()} or {@link #displayWithScroll()}.
 * Call {@link #refreshCLI()} to update.
 */
public class CLI {
    private final Client client;
    private Optional<Title> title;
    private Optional<CLIelem> body;
    private Optional<CLIelem> bottom;
    private Optional<Spinner> spinner;
    private Optional<Option> lastChoice=Optional.empty();
    public final AtomicBoolean stopASAP;
    private Thread inputThread;
    private String inputMessage;
    private String lastInput;
    private int lastInt;
    private boolean isTakingInput;
    private Runnable afterInput;

    private int writtenChars;



    /**
     * Initializes a client that asks the user for ip and port or if given arguments from the given arguments
     * @param args the first argument is the ip and the second the port.
     */
    public static void main(String[] args)
    {
        Client client = Client.getInstance();
        if (args.length==2)
        {
            client.setCLI();
            client.setServerConnection(args[0],Integer.parseInt(args[1]));
            client.run();
            client.changeViewBuilder(new CreateJoinLoadMatch());
        } else {
            client.setCLI();
            client.changeViewBuilder(new ConnectToServer());
        }
    }

    public CLI(Client client) {
        stopASAP = new AtomicBoolean(false);
        this.client = client;
        title = Optional.empty();
        body = Optional.empty();
        bottom = Optional.empty();
        spinner = Optional.empty();
        writtenChars = 0;
    }

    public void setTitle(Title title){
        this.title.ifPresent(t->t.removeFromPublishers(client));
        title.setCLIAndUpdateSubscriptions(this,client);
        this.title = Optional.of(title);
    }

    public void setBottom(CLIelem bottom) {
        this.bottom.ifPresent(b->b.removeFromPublishers(client));
        bottom.setCLIAndUpdateSubscriptions(this,client);
        this.bottom = Optional.of(bottom);
    }


    public void setLastChoice(Optional<Option> integerOptionPair ) {
        this.lastChoice = integerOptionPair;
    }

    public void setBody(CLIelem body){
        this.body.ifPresent(b->b.removeFromPublishers(client));
        this.body = Optional.ofNullable(body);
        this.body.ifPresent(b->b.setCLIAndUpdateSubscriptions(this,client));
    }

    public void setSpinner(Spinner spinner){
        this.spinner.ifPresent(s->s.removeFromPublishers(client));
        this.spinner = Optional.of(spinner);
        spinner.setCLIAndUpdateSubscriptions(this, client);
    }

    public int getLastInt() {
        return lastInt;
    }

    public String getLastInput() {
        return lastInput;
    }

    public void resetCLI(){
        try {
            clearOptions(this);
        } catch (ChangingViewBuilderBeforeTakingInput e){
            e.printStackTrace();
        }
    }

    private static void clearOptions(CLI cli) throws ChangingViewBuilderBeforeTakingInput {
        if (cli.isTakingInput)
        {
            throw new ChangingViewBuilderBeforeTakingInput();
        }
        cli.title.ifPresent(t->t.removeFromPublishers(cli.client));
        cli.title = Optional.empty();

        cli.body.ifPresent(b->b.removeFromPublishers(cli.client));
        cli.body = Optional.empty();

        cli.bottom.ifPresent(b->b.removeFromPublishers(cli.client));
        cli.bottom = Optional.empty();
        
        cli.spinner.ifPresent(s->s.removeFromPublishers(cli.client));
        cli.spinner = Optional.empty();
    }

    public void refreshCLI(){
        deleteText();
        displayWithDivider();
    }

    public void updateListeners(){
        title.ifPresent(t->t.setCLIAndUpdateSubscriptions(this,client));

        body.ifPresent(b->b.setCLIAndUpdateSubscriptions(this,client));

        bottom.ifPresent(b->b.setCLIAndUpdateSubscriptions(this,client));

        spinner.ifPresent(s->s.setCLIAndUpdateSubscriptions(this,client));
    }

    public void performLastChoice(){
        lastChoice.ifPresent(Option::perform);
    }

    private synchronized void commonRunOnInput(String message, Runnable r, Runnable afterInput){
        if (inputThread!= null && inputThread.isAlive() && isTakingInput) {
            inputThread.interrupt();
            isTakingInput = false;
        }
        this.afterInput = afterInput;
        inputMessage = message;
        inputThread = new Thread(r);
    }

    private synchronized void callRunnableAfterGettingInput(){
        afterInput.run();
        inputThread = null;
    }

    public synchronized void runOnInput(String message, Runnable r1){
        Runnable r = ()-> {
            print(Color.colorString(message,Color.ANSI_GREEN));
            putEndDiv();
            lastInput = getInString();
            callRunnableAfterGettingInput();
        };
        commonRunOnInput(message,r,r1);
    }

    public synchronized void runOnIntInput(String message, String errorMessage, int min, int max, Runnable r1){
        Runnable r = ()-> {
            int choice;
            do  {
                print(Color.colorString(message,Color.ANSI_GREEN));
                putEndDiv();
                String in = getInString();
                try
                {
                    choice = Integer.parseInt(in);
                    if (choice<min||choice>max)
                    {
                        printError(errorMessage);
                        if (choice<min)
                            printError("Insert a GREATER number!");
                        else printError("Insert a SMALLER number!");
                    }else {
                        break;
                    }
                }
                catch (NumberFormatException e){
                    printError(errorMessage);
                    printError("Insert a NUMBER!");
                }
            }while(true);
            lastInt = choice;
            lastInput = Integer.toString(choice);
            callRunnableAfterGettingInput();
        };
        commonRunOnInput(message,r,r1);
    }

    private String getInString(){
        isTakingInput = true;
        Scanner scanner = new Scanner(System.in);
        isTakingInput = false;
        return scanner.nextLine();
    }


    private void print(String s){
        writtenChars += s.length();
        System.out.println(s);
    }

    private void printError(String error){
        print(Color.colorString(error,Color.ANSI_RED));
    }

    public void displayWithScroll(){
        scroll();
        display();
    }

    public void displayWithDivider(){
        deleteText();
        putDivider();
        display();
    }

    /**
     * Used to refresh the screen
     */
    private synchronized void display(){

        title.ifPresent(t->print(t.toString()+"\n"));

        body.ifPresent(b->print(b.toString()+"\n"));

        bottom.ifPresent(b->print(b.toString()));

        spinner.ifPresent(spinner1 -> print(spinner1.toString()));

        //Todo: Sometimes the choices are not updated
        //Can be solved via a check after taking the input
        //printError("Todo: Sometimes the choices are not updated, sometimes you hase to select new match 2 times");
        if (isTakingInput){
            //Should never get here
            print(Color.colorString(inputMessage,Color.ANSI_GREEN));
            putEndDiv();
        }
        else if (inputThread!=null && !inputThread.isAlive()) {
            inputThread.start();
        } else putEndDiv();
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
            print("");
        }
    }
    private static final String BACKSPACE = "\010";
    public void deleteText(){
        return;
        //cleanConsole();
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        //System.out.print("Line 1");
        //System.out.print("\n");
        //System.out.print("Line 2");
        //
        //for (int i = 0; i < 100; i++)
        //            System.out.print(BACKSPACE);
        //writtenChars=0;
    }

}
