package it.polimi.ingsw.client.view.abstractview;

import it.polimi.ingsw.client.view.CLI.ConnectToServer;

public abstract class ConnectToServerViewBuilder extends ViewBuilder {

    public static ViewBuilder getBuilder(boolean isCLI){
        if (isCLI)
            return new ConnectToServer();
        else return new it.polimi.ingsw.client.view.GUI.ConnectToServer();
    }

    /**
     * Using regex to verify
     * @param ip "should" be and ip address
     * @return true if is ip address
     */
    public static boolean isIPAddr(String ip) {
        String ipRegex = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(ipRegex);
    }

}
