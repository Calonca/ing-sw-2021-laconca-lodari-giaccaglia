package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.body.GridBody;
import it.polimi.ingsw.client.view.CLI.layout.Grid;
import it.polimi.ingsw.client.view.CLI.layout.HorizontalList;
import it.polimi.ingsw.client.view.CLI.layout.Option;
import it.polimi.ingsw.client.view.CLI.layout.OptionList;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;

import java.beans.PropertyChangeEvent;

public class TestGridBody extends ViewBuilder {

    @Override
    public void run() {

        Grid grid = new Grid();
        OptionList hiddenOptionList = new HorizontalList(2);

        HorizontalList hor1 = new HorizontalList(3);
        hor1.addOption(Option.from("R1 O1",buildTestRunnable(1)));
        hor1.addOption(Option.from("R1 O2",buildTestRunnable(2)));
        hor1.addOption(Option.from("R1 O3",buildTestRunnable(3)));
        hiddenOptionList.addOption(Option.from("R1 O1",buildTestRunnable(1)));
        hiddenOptionList.addOption(Option.from("R1 O2",buildTestRunnable(2)));
        hiddenOptionList.addOption(Option.from("R1 O3",buildTestRunnable(3)));

        HorizontalList hor2 = new HorizontalList(3);
        hor2.addOption(Option.from("R2 O1",buildTestRunnable(4)));
        hor2.addOption(Option.from("R2 O2",buildTestRunnable(5)));
        hor2.addOption(Option.from("R2 O3",buildTestRunnable(6)));
        hiddenOptionList.addOption(Option.from("R2 O1",buildTestRunnable(4)));
        hiddenOptionList.addOption(Option.from("R2 O2",buildTestRunnable(5)));
        hiddenOptionList.addOption(Option.from("R2 O3",buildTestRunnable(6)));

        grid.addRow(hor1);
        grid.addRow(hor2);

        grid.setShowNumbers(true);

        getCLIView().setBody(new GridBody(grid));
        hiddenOptionList.selectAndRunOption(getCLIView());
        getCLIView().show();


    }

    private Runnable buildTestRunnable(int i){
        return ()->{
            getCLIView().setTitle("The result is "+i);
            getCLIView().show();
        };
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
