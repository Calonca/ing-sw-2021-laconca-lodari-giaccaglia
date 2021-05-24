package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.*;
import it.polimi.ingsw.client.view.CLI.CLIelem.body.SpinnerBody;
import it.polimi.ingsw.client.view.CLI.textUtil.Characters;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Builds the CLI.<br>
 * Usage: Add {@link it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem elements}
 * like optionList, body, title, spinner, and then call {@link #refreshCLI()}.
 * Call {@link #refreshCLI()} to update.
 */
public class CLI {
    private final Client client;
    private Optional<Title> title;
    private Optional<CLIelem> body;
    private Optional<CLIelem> bottom;
    private Optional<Option> lastChoice=Optional.empty();
    public final AtomicBoolean stopASAP;

    private String inputMessage, errorMessage;
    private String lastInput;
    private int lastInt;
    private Runnable afterInput;

    private int writtenChars;

    //Min is 21
    public static final int height =23;//Usually 45
    //Min is 21
    public static final int width =185;//Usually 47


    public CLI(Client client) {
        stopASAP = new AtomicBoolean(false);
        this.client = client;
        title = Optional.empty();
        body = Optional.empty();
        bottom = Optional.empty();
        writtenChars = 0;
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                lastInput = scanner.nextLine();
                Runnable toRun = afterInput;
                afterInput = this::refreshCLI;
                inputMessage = "Not asking for input";
                errorMessage = null;
                toRun.run();
            }
        });

        inputThread.start();
    }

    public static int getCenterX(){
        return width/2;
    }

    public static int getCenterY(){
        return height/2;
    }

    public int getMaxBodyHeight(){return height-5;}

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

        cli.title.ifPresent(t->t.removeFromPublishers(cli.client));
        cli.title = Optional.empty();

        cli.body.ifPresent(b->b.removeFromPublishers(cli.client));
        cli.body = Optional.empty();

        cli.bottom.ifPresent(b->b.removeFromPublishers(cli.client));
        cli.bottom = Optional.empty();

        cli.deleteText();
    }

    public void refreshCLI(){
        deleteText();
        display();
    }

    public void updateListeners(){
        title.ifPresent(t->t.setCLIAndUpdateSubscriptions(this,client));

        body.ifPresent(b->b.setCLIAndUpdateSubscriptions(this,client));

        bottom.ifPresent(b->b.setCLIAndUpdateSubscriptions(this,client));
    }

    public void performLastChoice(){
        lastChoice.ifPresent(Option::perform);
    }

    public void runOnInput(String message, Runnable r1){
        inputMessage = message;
        errorMessage = null;
        afterInput = r1;
    }

    public void runOnIntInput(String message, String errorMessage, int min, int max, Runnable r1){
        inputMessage = message;
        afterInput = ()->{
            try
            {
                int choice = Integer.parseInt(lastInput);
                if (choice<min||choice>max)
                {
                    this.errorMessage = errorMessage;
                    if (choice<min) {
                        this.errorMessage += "Insert a GREATER number!";
                    }
                    else {
                        this.errorMessage += "Insert a SMALLER number!";
                    }
                    runOnIntInput(message,errorMessage,min,max,r1);
                    refreshCLI();
                }else {
                    lastInt = choice;
                    r1.run();
                }
            }
            catch (NumberFormatException e){
                this.errorMessage = errorMessage+"Insert a NUMBER!";
                runOnIntInput(message,errorMessage,min,max,r1);
                refreshCLI();
            }
        };
    }


    private void printLine(String s){
        writtenChars += s.length()+1;
        System.out.println(s);
    }

    private void print(String s){
        writtenChars += s.length();
        System.out.print(s);
    }

    private void printError(String error){
        print(Color.colorString(error,Color.ANSI_RED));
    }

    /**
     * Used to refresh the screen
     */
    private synchronized void display(){
        putDivider();
        title.ifPresent(t-> print(t.toString()));
        putDivider();
        body.ifPresent(b-> print(b.toString()));
        putDivider();
        bottom.ifPresent(b-> printLine(b.toString()));

        putEndDiv();
        if (errorMessage!=null)
            printError(errorMessage+" ");
        printLine(Color.colorString(inputMessage,Color.ANSI_GREEN));
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
        printLine(
                Characters.VERT_DIVIDER.getString()+
                Characters.HOR_DIVIDER.repeated(width)+
                Characters.VERT_DIVIDER.getString()
                );
    }

    public void putEndDiv(){
        printLine(
                Characters.HOR_DIVIDER.getString()+
                        Characters.HOR_DIVIDER.repeated(width)+
                        Characters.VERT_DIVIDER.getString()
        );
    }

    public void scroll(){
        System.out.println("\n".repeat(height/10));
    }

    public void deleteText(){
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        scroll();//Used in intellij terminal, can be disabled if using linux terminal
        System.out.print("\033\143");//Tested on linux terminal
    }

}
