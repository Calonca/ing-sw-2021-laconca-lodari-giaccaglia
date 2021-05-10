package it.polimi.ingsw.client.view.abstractview;

public abstract class ConnectToServerViewBuilder extends ViewBuilder {

    /**
     * Using regex to verify
     * @param ip "should" be and ip address
     * @return true if is ip address
     */
    public static boolean isIPAddr(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

}
