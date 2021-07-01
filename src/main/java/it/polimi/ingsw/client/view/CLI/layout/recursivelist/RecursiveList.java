package it.polimi.ingsw.client.view.CLI.layout.recursivelist;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.GridElem;
import it.polimi.ingsw.client.view.CLI.layout.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A list of {@link GridElem}, but since it also extends {@link GridElem} it can contain other {@link RecursiveList}.<br>
 * It draws each {@link GridElem element} it contains recursively using each element height, width and alignment
 */
public abstract class RecursiveList extends GridElem {

    protected List<GridElem> elems;
    private Option lastSelectedOption;

    public RecursiveList(){
        elems = new ArrayList<>();
        alignment = Alignment.START;
    }

    public RecursiveList(Stream<? extends GridElem> elem){
        this();
        elem.forEach(this::addElem);
    }

    public void addElem(GridElem elem){
        elem.setFirstIdx(getNextElemIndex());
        elems.add(elem);
    }

    public void updateElem(int pos, GridElem elem){
        elem.setFirstIdx(getNextElemIndex());
        elems.set(pos, elem);
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
        if (!elems.isEmpty()){
            List<GridElem> oldElems = elems;
            elems = new ArrayList<>();
            oldElems.forEach(this::addElem);
            }
    }



    @Override
    public int getNextElemIndex() {
        if (elems.isEmpty())
            return getFirstIdx();
        return elems.get(elems.size()-1).getNextElemIndex();
    }

    @Override
    public Optional<Option> getOptionWithIndex(int i) {
        return elems.stream().filter(e->i>=e.getFirstIdx()&&i<=e.getNextElemIndex())
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

    public void selectInEnabledOption(CLI cli, String message)
    {
        selectInEnabledOption(cli,message,null);
    }

    public void selectInEnabledOption(CLI cli, String message,Runnable onEnterPressed)
    {
        Runnable r = ()->{
            int choice = cli.getLastInt();
            selectOptionAtPosition(choice);
            performLastChoice();
        };

        cli.runOnIntListInput(message,"Select a valid choice", getAllEnabledOption().stream().mapToInt(GridElem::getFirstIdx),r,onEnterPressed);
    }

}
