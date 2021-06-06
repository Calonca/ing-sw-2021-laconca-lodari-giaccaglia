package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class OptionList extends GridElem{

    protected List<GridElem> elems;
    private Option lastSelectedOption;

    public OptionList(){
        elems = new ArrayList<>();
        alignment = Alignment.START;
    }

    public OptionList(Stream<Option> elem){
        this();
        elem.forEach(this::addElem);
    }

    public void addElem(GridElem elem){
        elem.setFirstIdx(getLastIndex()+1);
        elems.add(elem);
    }

    public void addElemNoIndexChange(GridElem elem){
        elems.add(elem);
    }

    public Column addAndGetColumn(){
        Column c = new Column(); 
        elems.add(c);
        return c;
    }

    public Row addAndGetRow(){
        Row r = new Row();
        elems.add(r);
        return r;
    }

    @Override
    public void setFirstIdx(int firstIdx) {
        super.setFirstIdx(firstIdx);
        if (!elems.isEmpty())
            IntStream.range(0,elems.size()).forEach(i->elems.get(i).setFirstIdx(firstIdx+i));
    }

    @Override
    public int getLastIndex() {
        if (elems.isEmpty())
            return getFirstIdx()-1;
        return elems.get(elems.size()-1).getLastIndex();
    }

    @Override
    public Optional<Option> getOptionWithIndex(int i) {
        return elems.stream().filter(e->i>=e.getFirstIdx()&&i<=e.getLastIndex())
                .map(e2->e2.getOptionWithIndex(i))
                .filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    @Override
    public List<Option> getAllEnabledOption() {
        return elems.stream()
                .flatMap(e->e.getAllEnabledOption().stream())
                .collect(Collectors.toList());
    }

    private void selectOptionAtPosition(int choice)
    {
        lastSelectedOption = getOptionWithIndex(choice).orElse(null);
        if (lastSelectedOption!=null)
            lastSelectedOption.setSelected(!lastSelectedOption.isSelected());
    }

    public void performLastChoice(){
        if (lastSelectedOption!=null)
            lastSelectedOption.perform();
    }

    public void selectAndRunOption(CLI cli)
    {
        Runnable r = ()->{
            int choice = cli.getLastInt();
            selectOptionAtPosition(choice);
            performLastChoice();
        };
        cli.runOnIntInput("Select a choice:","Select a valid choice", getFirstIdx(),getLastIndex(),r);
    }

    public void selectInEnabledOption(CLI cli, String message)
    {
        Runnable r = ()->{
            int choice = cli.getLastInt();
            selectOptionAtPosition(choice);
            performLastChoice();
        };
        cli.runOnIntListInput(message,"Select a valid choice", getAllEnabledOption().stream().mapToInt(GridElem::getFirstIdx),r);
    }

}
