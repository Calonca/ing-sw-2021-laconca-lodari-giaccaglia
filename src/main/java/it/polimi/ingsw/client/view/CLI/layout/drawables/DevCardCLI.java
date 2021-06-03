package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

public enum DevCardCLI {

    GREEN(NetworkDevelopmentCardColor.GREEN,  "Green " , Color.GREEN, Background.ANSI_GREEN_BACKGROUND),
    BLUE(NetworkDevelopmentCardColor.BLUE,    "Blue  ", Color.CYAN,Background.ANSI_BLUE_BACKGROUND),
    PURPLE(NetworkDevelopmentCardColor.PURPLE,"Purple", Color.PURPLE,Background.ANSI_PURPLE_BACKGROUND),
    YELLOW(NetworkDevelopmentCardColor.YELLOW,"Yellow" , Color.YELLOW, Background.ANSI_YELLOW_BACKGROUND);

    private final NetworkDevelopmentCardColor res;
    private final Color c;
    private final String nameWithSpaces;
    private final Background b;

    DevCardCLI(NetworkDevelopmentCardColor res, String nameWithSpaces, Color c, Background background) {
        this.res = res;
        this.c = c;
        this.nameWithSpaces = nameWithSpaces;
        b = background;
    }

    public Background getB() {
        return b;
    }

    public Color getC() {
        return c;
    }

    public static DevCardCLI fromNetworkColor(NetworkDevelopmentCardColor asset){
        int rNum = asset.ordinal();
        DevCardCLI[] val = DevCardCLI.values();
        return rNum>val.length ? DevCardCLI.GREEN: val[rNum];
    }

    public String getNameWithSpaces() {
        return nameWithSpaces;
    }
}