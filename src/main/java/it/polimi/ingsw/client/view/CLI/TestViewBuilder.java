package it.polimi.ingsw.client.view.CLI;

import it.polimi.ingsw.client.view.CLI.CLIelem.Title;

import java.beans.PropertyChangeEvent;

public class TestViewBuilder extends it.polimi.ingsw.client.view.abstractview.TestViewBuilder implements CLIBuilder {



    @Override
    public void run() {
        //getCLIView().runOnIntInput("Write 0","ERR1, ",0,0,()->
        //{
        //    int in = getCLIView().getLastInt();
        //    getCLIView().setTitle(new Title("Printed after input "+in));
        //    getCLIView().refreshCLI();
        //
        //    //getClient().changeViewBuilder(new CreateJoinLoadMatch(),this);
        //});
        getCommonData().setStartData(null,2);

        //getCLIView().runOnInput("Get in",()->
        //{
        //    String in = getCLIView().getLastInput();
        //    getCLIView().setTitle(new Title("Printed after input "+in));
        //    getCLIView().refreshCLI();
        //    //getClient().changeViewBuilder(new CreateJoinLoadMatch(),this);
        //});
        //getCLIView().runOnInput("2 Get in",()->
        //{
        //    String in = getCLIView().getLastInput();
        //    getCLIView().setTitle(new Title("2 Printed after input "+in));
        //    getCLIView().refreshCLI();
        //    //getClient().changeViewBuilder(new CreateJoinLoadMatch(),this);
        //});


        //Spinner spinner = new Spinner("matches data");
        //spinner.setUpdater(()->
        //        {
        //            if (spinner.getEvt().getPropertyName().equals(CommonData.matchesDataString)) {
        //                spinner.setMeanwhileShow("Last matches data: "+spinner.getEvt().getNewValue());
        //                getCLIView().refreshCLI();
        //
        //            }
        //        }
        //);
        //getCLIView().setSpinner(spinner);
        getCLIView().refreshCLI();

        //Simulating matches updates
        //getCLIView().runOnIntInput("Get1, getting 1","ERR1",1,1,()->
        //{
        //    int in = getCLIView().getLastInt();
        //    getCLIView().setTitle(new Title("Printed after input "+in));
        //    getCLIView().refreshCLI();
        //    //getClient().changeViewBuilder(new CreateJoinLoadMatch(),this);
        //});
        //
        //getCLIView().refreshCLI();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName());
    }
}
