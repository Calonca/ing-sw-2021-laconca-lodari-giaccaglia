package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;
import it.polimi.ingsw.client.view.CLI.textUtil.Characters;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Builds the CLI.<br>
 * Usage: Add {@link it.polimi.ingsw.client.view.CLI.CLIelem.CLIelem elements}
 * like optionList, body, title, spinner, and then call {@link #show()}.
 * Call {@link #show()} to update.
 */
public class CLI {

    private final Client client;
    private Optional<Title> title;
    private Optional<Title> subTitle;
    private Optional<CLIelem> body;
    public final AtomicBoolean stopASAP;

    private String inputMessage, errorMessage;
    private String lastInput;
    private int lastInt;
    private Runnable afterInput;
    private boolean isViewMode;
    private boolean isTitleBlocked;

    //Min is 52
    public static final int height =53;//Usually 53
    //Min is 180
    public static final int width =190;//Usually 190

    public CLI(Client client) {
        stopASAP = new AtomicBoolean(false);
        this.client = client;
        title = Optional.empty();
        subTitle = Optional.empty();
        body = Optional.empty();
        Thread inputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                lastInput = scanner.nextLine();
                Runnable toRun = afterInput;
                afterInput = this::show;
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

    public void setTitle(String title){

        if(isTitleBlocked)
            return;

        Title title1 = new Title(title);
        this.title.ifPresent(t->t.removeFromListeners(client));
        title1.addToListeners(client);
        this.title = Optional.of(title1);
    }

    public void setSubTitle(String subTitle, Color color, int height){

        if(isTitleBlocked)
            return;

        Title subTitle1 = new Title(subTitle, color, height);
        this.subTitle.ifPresent(t->t.removeFromListeners(client));
        subTitle1.addToListeners(client);
        this.subTitle = Optional.of(subTitle1);
    }

    public void removeSubTitle(){
        subTitle.ifPresent(b->b.removeFromListeners(client));
        subTitle = Optional.empty();
    }

    public void setTitleWhenBlocked(String title){

        Title title1 = new Title(title);
        this.title.ifPresent(t->t.removeFromListeners(client));
        title1.addToListeners(client);
        this.title = Optional.of(title1);

    }

    public void setTitle(Title title){

        this.title.ifPresent(t->t.removeFromListeners(client));
        title.addToListeners(client);
        this.title = Optional.of(title);
    }

    public void setBody(CLIelem body){
        this.body.ifPresent(b->b.removeFromListeners(client));
        this.body = Optional.ofNullable(body);
        this.body.ifPresent(b->b.addToListeners(client));
    }

    public void enableViewMode(){
        isViewMode = true;
    }

    public void disableViewMode(){
        isViewMode = false;
    }

    public void blockTitleChanges(){ isTitleBlocked = true;}

    public void enableTitleChanges(){ isTitleBlocked = false;}

    public int getLastInt() {
        return lastInt;
    }

    public String getLastInput() {
        return lastInput;
    }

    public void clearScreen(){
        try {
            clearOptions(this);
        } catch (ChangingViewBuilderBeforeTakingInput e){
            e.printStackTrace();
        }
    }

    private static void clearOptions(CLI cli) throws ChangingViewBuilderBeforeTakingInput {

        cli.title.ifPresent(t->t.removeFromListeners(cli.client));
        cli.title = Optional.empty();

        cli.body.ifPresent(b->b.removeFromListeners(cli.client));
        cli.body = Optional.empty();


        cli.deleteText();
    }

    public void show(){
        deleteText();
        display();
    }

    public void runOnInput(String message, Runnable r1){
        inputMessage = message;
        errorMessage = null;
        afterInput = r1;
    }

    public void runOnIntInput(String message, String errorMessage, int min, int max, Runnable r1){
        runOnIntInput(message, errorMessage, min, max, r1, null);
    }

    public void runOnIntInput(String message, String errorMessage, int min, int max, Runnable r1,Runnable onEnter){

        inputMessage = message;

        afterInput = ()->{
            try
            {
                int choice = Integer.parseInt(lastInput);
                if(isViewMode)
                    return;

                if (choice<min||choice>max)
                {
                    this.errorMessage = errorMessage;
                    if (choice<min) {
                        this.errorMessage += ", insert a GREATER number!";
                    }
                    else {
                        this.errorMessage += ", insert a SMALLER number!";
                    }
                    runOnIntInput(message,errorMessage,min,max,r1,onEnter);
                    show();
                }else {
                    lastInt = choice;
                    r1.run();
                }
            }
            catch (NumberFormatException e) {
                if (onEnter != null)
                    onEnter.run();
                else{
                    this.errorMessage = errorMessage + ", insert a NUMBER!";
                runOnIntInput(message, errorMessage, min, max, r1, onEnter);
                show();
                }
            }
        };

    }

    public void runOnIntListInput(String message, String errorMessage, IntStream possibleValues, Runnable onInt){
        runOnIntListInput(message, errorMessage, possibleValues, onInt,null);
    }

    public void runOnIntListInput(String message, String errorMessage, IntStream possibleValues, Runnable onInt, Runnable onEnter){

        int[] supplier = possibleValues.toArray();
        int max = Arrays.stream(supplier).max().orElse(0);
        int min = Arrays.stream(supplier).min().orElse(0);
        Runnable r2 = ()->{
            if (Arrays.stream(supplier).noneMatch(i->i==getLastInt())) {
                this.errorMessage = errorMessage+", select an active option";
                runOnIntListInput(message,errorMessage, Arrays.stream(supplier),onInt,onEnter);
                show();
            }
            else {
                onInt.run();
            }
        };
        runOnIntInput(message,errorMessage,min,max,r2,onEnter);
    }

    private void printLine(String s){
        System.out.println(s);
    }

    private void print(String s){
        System.out.print(s);
    }

    private void printError(String error){
        print(Color.colorString(error,Color.RED));
    }

    /**
     * Used to refresh the screen
     */
    private synchronized void display(){
        putStartDiv();
        title.ifPresent(t-> print(t.toString()));
        subTitle.ifPresent(value -> print(value.toString()));
        putDivider();
        body.ifPresent(b-> print(b.toString()));

        putEndDiv();
        if (errorMessage!=null)
            printError(errorMessage+" ");
        printLine(Color.colorString(inputMessage,Color.GREEN));
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
                Characters.HOR_DIVIDER.repeated(width-2)+
                Characters.VERT_DIVIDER.getString()
                );
    }

    public void putStartDiv(){
        printLine(
                Characters.TOP_LEFT_DIV.getString()+
                        Characters.HOR_DIVIDER.repeated(width-2)+
                        Characters.TOP_RIGHT_DIV.getString()
        );
    }

    public void putEndDiv(){
        printLine(
                Characters.BOTTOM_LEFT_DIV.getString()+
                        Characters.HOR_DIVIDER.repeated(width-2)+
                        Characters.BOTTOM_RIGHT_DIV.getString()
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
