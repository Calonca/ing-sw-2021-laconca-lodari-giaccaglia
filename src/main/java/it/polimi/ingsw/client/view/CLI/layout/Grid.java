package it.polimi.ingsw.client.view.CLI.layout;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final List<HorizontalList> lists;
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

    @Override
    public String toString() {
        return lists.stream().map(HorizontalList::toString).reduce("",(a, b)->a+b);
    }
}
