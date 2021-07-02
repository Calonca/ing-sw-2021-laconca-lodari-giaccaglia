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
    /**
     * Title is the top part of the CLI view
     */
    private Optional<Title> title;
    /**
     * Subtitle is below the title
     */
    private Optional<Title> subTitle;
    /**
     * Body is the main part of the CLI view
     */
    private Optional<CLIelem> body;

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

    public void setTitleWhenBlocked(Title title){

        this.title.ifPresent(t->t.removeFromListeners(client));
        title.addToListeners(client);
        this.title = Optional.of(title);

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

    /**
     * @return the last digited input in the cli
     */
    public int getLastInt() {
        return lastInt;
    }

    /**
     * @return the last digited string in the cli
     */
    public String getLastInput() {
        return lastInput;
    }

    /**
     * Resets the cli
     */
    public void clearScreen(){
        try {
            clearOptions(this);
        } catch (ChangingViewBuilderBeforeTakingInput e){
            e.printStackTrace();
        }
    }

    /**
     * Removes the old cli title and body and delectes text from the terminal
     */
    private static void clearOptions(CLI cli) throws ChangingViewBuilderBeforeTakingInput {

        cli.title.ifPresent(t->t.removeFromListeners(cli.client));
        cli.title = Optional.empty();

        cli.body.ifPresent(b->b.removeFromListeners(cli.client));
        cli.body = Optional.empty();


        cli.deleteText();
    }

    /**
     * Prints the new cli
     */
    public void show(){
        deleteText();
        display();
    }

    /**
     * Runs the provided runnable after the user presses enter
     * @param message the message to display to the user
     * @param r1 the runnable to run
     */
    public void runOnInput(String message, Runnable r1){
        inputMessage = message;
        errorMessage = null;
        afterInput = r1;
    }

    /**
     * Runs the provided runnable if the user inputs a number or displays the
     * error message and asks again for input if the input is not acceptable.
     * @param message Shown to the user to specify the kind of input the function needs
     * @param errorMessage Shown in case the input is not acceptable
     * @param min The minimum accepted number
     * @param max The maximum accepted number
     * @param r1 The runnable to run when the input number is acceptable
     */
    public void runOnIntInput(String message, String errorMessage, int min, int max, Runnable r1){
        runOnIntInput(message, errorMessage, min, max, r1, null);
    }

    /**
     * Runs the provided runnable if the user inputs a number, the onEnter runnable if the user inputs nothing or displays the
     * error message and asks again for input if the input is not acceptable.
     * @param message Shown to the user to specify the kind of input the function needs
     * @param errorMessage Shown in case the input is not acceptable
     * @param min The minimum accepted number
     * @param max The maximum accepted number
     * @param r1 The runnable to run when the input number is acceptable
     * @param onEnter The runnable to run when the user input nothing and presses enter
     */
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

    /**
     * Runs the provided runnable if the user inputs a number present in the possible values,
     * the onEnter runnable if the user inputs nothing or displays the
     * error message and asks again for input if the input is not acceptable.
     * @param message Shown to the user to specify the kind of input the function needs
     * @param errorMessage Shown in case the input is not acceptable
     * @param possibleValues The list of the possible accepted values
     * @param onInt The runnable to run when the input number is acceptable
     * @param onEnter The runnable to run when the user input nothing and presses enter
     */
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

    /**
     * Prints the error string formatted as and error (red text)
     */
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

    /**
     * Puts spaces between a cli view and the next
     */
    public void scroll(){
        System.out.println("\n".repeat(height/10));
    }


    /**
     * Clears the terminal
     */
    public void deleteText(){
        //System.out.print("\033[H\033[2J");
        //System.out.flush();
        scroll();//Used in intellij terminal, can be disabled if using linux terminal
        System.out.print("\033\143");//Tested on linux terminal
    }



}
