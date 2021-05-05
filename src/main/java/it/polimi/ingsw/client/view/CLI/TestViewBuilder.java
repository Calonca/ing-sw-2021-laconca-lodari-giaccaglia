package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLIelem.Spinner;
import it.polimi.ingsw.client.view.CLI.CLIelem.Title;

public class TestViewBuilder extends it.polimi.ingsw.client.view.abstractview.TestViewBuilder implements CLIBuilder {

    @Override
    public void run() {
        getCLIView().resetCLI();
        Runnable r = (()->
                {
                    String input = getCLIView().getLastInput();
                    getCLIView().setTitle(new Title("Printed after input "+input));
                    getCLIView().displayWithDivider();
                    //getClient().changeViewBuilder(new CreateJoinLoadMatch(),this);

                }
                );
        getCLIView().runOnInput("Write something",r);


        Spinner spinner = new Spinner("matches data");
        spinner.setUpdater(()->
                {
                    if (spinner.getEvt().getPropertyName().equals(CommonData.matchesDataString)) {
                        spinner.setMeanwhileShow("Last matches data: "+spinner.getEvt().getNewValue());
                        getCLIView().displayWithDivider();

                    }
                }
        );
        getCLIView().setSpinner(spinner);

        getCLIView().displayWithDivider();
    }
}
