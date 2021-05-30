package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.CLI.CLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OptionList {
    protected List<Option> options;
    private Option lastSelectedOption;
    private int optStartIndex;

    public OptionList(){
        options = new ArrayList<>();
    }

    public void setStartIndex(int optStartIndex) {
        this.optStartIndex = optStartIndex;
    }

    public void performLastChoice(){
        lastSelectedOption.perform();
    }

    public OptionList(Stream<Option> optionStream){
        options = optionStream.collect(Collectors.toList());
    }

    public void addOption(Option o){
        options.add(o);
    }

    public void updateOptions(Stream<Option> optionStream,Client client){
        options = optionStream.collect(Collectors.toList());
    }

    private void selectOptionAtPosition(int numberInList)
    {
        lastSelectedOption = options.get(numberInList);
        lastSelectedOption.setSelected(!lastSelectedOption.isSelected());
    }

    protected int globalPos(int i) {
        return i+optStartIndex;
    }

    public void selectAndRunOption(CLI cli)
    {
        Runnable r = ()->{
            int choice = cli.getLastInt()-optStartIndex;
            selectOptionAtPosition(choice);
            performLastChoice();
        };
        cli.runOnIntInput("Select a choice:","Select a valid choice",optStartIndex,optStartIndex+options.size()-1,r);
    }

}
