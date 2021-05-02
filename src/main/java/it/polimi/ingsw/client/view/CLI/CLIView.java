package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import it.polimi.ingsw.client.view.CLI.CLIelem.OptionList;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Builds the CLI
 * Usage: add the elements to the cli by calling addOption()
 * You can then select an option and execute the code contained in that option
 * Todo add elements like text instead of options
 * Todo add selection with arrows
 */
public class CLIView {
    private Title title;
    private Spinner spinner;
    private OptionList[] optionListAtPos;
    private Optional<Option> lastChoice=Optional.empty();


    public CLIView() {
        optionListAtPos = Stream.generate(OptionList::new).limit(CLIPos.values().length).toArray(OptionList[]::new);
    }

    public void resetCLIelems(){
        clearOptions(this);
    }

    void setOptionList(CLIPos pos,OptionList optionList){
        optionList.setCLIView(this);
        optionListAtPos[pos.ordinal()]=optionList;
    }

    public static void clearOptions(CLIView cliView)
    {
        cliView.title = null;
        if (cliView.spinner!=null) cliView.spinner.stop();
        cliView.spinner=null;
        cliView.optionListAtPos = Stream.generate(OptionList::new).limit(CLIPos.values().length).toArray(OptionList[]::new);
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
        this.title = title;
    }

    public void setSpinner(Spinner spinner){
        if (this.spinner!=null)
            spinner.stop();
        this.spinner = spinner;
        spinner.setCLIView(this);
    }

    private void print(String s){
        System.out.println(s);
    }

    public void printError(String error){
        System.out.println(Color.colorString(error,Color.ANSI_RED));
    }

    public String getIN(String message){
        print(Color.colorString(message,Color.ANSI_GREEN));
        return getIN();
    }

    private String getIN(){
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
        if (spinner!=null)
            spinner.run();

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
        print("------------------------------------------------------------");
    }

    public void scroll(){
        for (int i = 0; i < 40; i++) {
            System.out.println();}
    }

    public void setLastChoice(Optional<Option> integerOptionPair ) {
        this.lastChoice = integerOptionPair;
    }

    public void updateTitle() {
        update();
    }
}
