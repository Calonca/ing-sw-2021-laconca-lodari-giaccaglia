package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.CLI.CreateJoinLoadMatch;

public class ViewHandler implements Runnable{
    Client client;


    @Override
    public void run() {
        client.changeViewBuilder(new CreateJoinLoadMatch(),null);
    };


}
