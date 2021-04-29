package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Option;
import javafx.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Builds the CLI
 * Usage: add the elements to the cli by calling addOption()
 * You can then select an option and execute the code contained in that option
 * Todo add elements like text instead of options
 * Todo add selection with arrows
 */
public class CLIBuilder {


    private List<Option>[] optionListAtPos;
    private Optional<Pair<CLIPos,Option>> lastChoice=Optional.empty();

    public CLIBuilder() {
        optionListAtPos = Stream.generate(()->new ArrayList<Option>()).limit(CLIPos.values().length).toArray(List[]::new);
    }

    public void resetCLIelems(){
        optionListAtPos = Stream.generate(()->new ArrayList<Option>()).limit(CLIPos.values().length).toArray(List[]::new);
    }

    void addOption(CLIPos position,Option o){
        optionListAtPos[position.ordinal()].add(o);
        if (optionListAtPos[position.ordinal()].size()==1)
            o.setSelected(true);
    }

    public void clearOptions()
    {
        optionListAtPos=Stream.generate(()->new ArrayList<Option>()).limit(CLIPos.values().length).toArray(List[]::new);
    }

    public void selectOptionAtGlobalPosition(int globalPosition){
        List<Pair<Integer,Option>> a = IntStream.range(0,optionListAtPos.length).boxed()
                .flatMap(i->
                        optionListAtPos[i]
                                .stream()
                                .map(option -> new Pair<>(i,option)))
                .collect(Collectors.toList());
        int listPos = a.get(globalPosition).getKey();
        selectOptionAtPosition(CLIPos.values()[listPos],globalPosition-numOfOptionsForList(listPos));
    }

    public void performLastChoice(){
        lastChoice.ifPresent(op->op.getValue().perform());
    }

    private void selectOptionAtPosition(CLIPos pos,int numberInList)
    {
        optionListAtPos[pos.ordinal()].forEach(option -> option.setSelected(false));
        optionListAtPos[pos.ordinal()].get(numberInList).setSelected(true);
        lastChoice = Optional.of(new Pair<>(pos,optionListAtPos[pos.ordinal()].get(numberInList)));
    }

    /**
     * Used to refresh the screen
     */
    void display(){
        scroll();

        String spaces = "                                                    ";
        String finalSpaces = spaces;
        optionsAtPos(CLIPos.TOP)
                .map(s -> finalSpaces +s.replace("\n","\n"+finalSpaces))
                .forEach(System.out::println);

        spaces ="                                                    ";
        String finalSpaces1 = spaces;
        optionsAtPos(CLIPos.CENTER)
                .map(s -> finalSpaces1 +s.replace("\n","\n"+finalSpaces1))
                .forEach(System.out::println);

        optionsAtPos(CLIPos.BOTTOM_LEFT)
                .forEach(System.out::println);

        spaces = "                                                    ";
        String finalSpaces2 = spaces;
        optionsAtPos(CLIPos.BOTTOM_RIGHT)
                .map(s -> finalSpaces2 +s.replace("\n","\n"+finalSpaces2))
                .forEach(System.out::println);

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

    /**
     * Used to get a global number for options before the specified {@link CLIPos}
     * @param i the ordinal of the CLIPos
     * @return
     */
    private int numOfOptionsForList(int i){
        return i>0?optionListAtPos[i-1].size()+numOfOptionsForList(i-1):0;
    }

    private Stream<String> optionsAtPos(CLIPos pos){
        List<Option> options = optionListAtPos[pos.ordinal()];
        return IntStream.range(0,options.size())
                .mapToObj(i->(numOfOptionsForList(pos.ordinal())+i)+": "+options.get(i).toString());
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



    public static void scroll(){
        for (int i = 0; i < 40; i++) {
            System.out.println();}
    }
}
