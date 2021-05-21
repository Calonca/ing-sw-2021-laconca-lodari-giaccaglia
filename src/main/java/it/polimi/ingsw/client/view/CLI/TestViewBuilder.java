package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;

import java.beans.PropertyChangeEvent;

public class TestViewBuilder extends it.polimi.ingsw.client.view.abstractview.TestViewBuilder implements CLIBuilder {



    @Override
    public void run() {
        getCLIView().runOnInput("Write something",()->
        {
            String input = getCLIView().getLastInput();
            getCLIView().setTitle(new Title("Printed after input "+input));
            getCLIView().refreshCLI();
            //getClient().changeViewBuilder(new CreateJoinLoadMatch(),this);
        });


        Spinner spinner = new Spinner("matches data");
        spinner.setUpdater(()->
                {
                    if (spinner.getEvt().getPropertyName().equals(CommonData.matchesDataString)) {
                        spinner.setMeanwhileShow("Last matches data: "+spinner.getEvt().getNewValue());
                        getCLIView().refreshCLI();

                    }
                }
        );
        //getCLIView().setSpinner(spinner);

        getCLIView().refreshCLI();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
