package it.polimi.ingsw.client.view.CLI.CLIelem.body;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.CommonData;
import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.SetupPhase;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;
import it.polimi.ingsw.network.messages.servertoclient.state.SETUP_PHASE;

import java.util.*;

public class WaitingForMatchToStart extends SpinnerBody{

    /**
     * Spinner for match start.
     * Updates its meanwhileShow from matchesData.
     * Goes to {@link SetupPhase} when the state goes to SetupPhase
     */
    public static WaitingForMatchToStart test(Client client){
        WaitingForMatchToStart test = new WaitingForMatchToStart();
        test.setUpdater(()->{
            if (test.getEvt().getPropertyName().equals(CommonData.matchesDataString)) {
                //Handling message before thisMatchData and after
                Optional<Map<UUID,String[]>> data = (Optional<Map<UUID,String[]>>) test.getEvt().getNewValue();

                client.getCommonData().getMatchId().ifPresentOrElse((o)->
                        {
                            String players = Arrays.toString(data.map(d->d.get(o)).orElse(new String[]{"No players"}));
                            String toShow="Your match: "+ o.toString()+"\n"+players;
                            test.meanwhileShow = Canvas.fromText(CLI.width,test.cli.getMaxBodyHeight(),toShow);
                        },()->{
                            String toShow="Waiting for server answer";
                            test.meanwhileShow = Canvas.fromText(CLI.width,test.cli.getMaxBodyHeight(),toShow);
                        }
                        );

                test.cli.refreshCLI();
            } else if (test.getEvt().getPropertyName().equals(CommonData.thisMatchData)) {
                Optional<UUID> matchID = (Optional<UUID>) test.getEvt().getNewValue();

                matchID.ifPresentOrElse((o)->
                        {
                            String players = Arrays.toString(client.getCommonData().playersOfMatch().orElse(new String[]{"No players"}));
                            String toShow="Your match: "+ o.toString()+"\n"+players;
                            test.meanwhileShow = Canvas.fromText(CLI.width,test.cli.getMaxBodyHeight(),toShow);
                        },()->{
                            String toShow="Waiting for matches data";
                    test.meanwhileShow = Canvas.fromText(CLI.width,test.cli.getMaxBodyHeight(),toShow);
                        }
                );

                test.cli.refreshCLI();
            }
            else if (test.getEvt().getPropertyName().equals(SETUP_PHASE.class.getSimpleName()))
                test.perform();
        });
        test.setPerformer(()-> {
            client.changeViewBuilder(new SetupPhase());
        });
        return test;
    }

}
