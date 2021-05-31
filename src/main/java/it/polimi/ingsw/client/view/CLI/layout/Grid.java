package it.polimi.ingsw.client.view.CLI.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid {
    private List<HorizontalList> lists;
    private int lastIndex;
    public boolean showNumbers;

    public Grid() {
        lists = new ArrayList<>();
    }

    /**
     * Sets whether showing the number under each option or not
     */
    public void setShowNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
        lists.forEach(l->l.setShowNumber(showNumbers));
    }

    public void addRow(HorizontalList toAdd){
        toAdd.setStartIndex(lastIndex);
        lists.add(toAdd);
        lastIndex+=toAdd.options.size();
        toAdd.setShowNumber(showNumbers);
    }

    public void addColumn(VerticalList toAdd){
        //Extending existing lines
        lists = Stream.concat(lists.stream(),Stream.generate(()->new HorizontalList(toAdd.getMaxHeight())))
                .limit(Math.max(lists.size(),toAdd.getOptionNumber()))
                .collect(Collectors.toList());
        lists = IntStream.range(0,toAdd.getOptionNumber()).mapToObj(i-> {
            HorizontalList hor = lists.get(i);
            hor.addOption(toAdd.options.get(i));
            hor.setStartIndex(hor.globalPos(0)+i);
            return hor;
        }).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return lists.stream().map(HorizontalList::toString).reduce("",(a, b)->a+b);
    }
}
