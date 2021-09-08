package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.network.assets.devcards.NetworkDevelopmentCardColor;

public enum DevCardCLI {

    GREEN("Green " , Color.GREEN, Background.ANSI_GREEN_BACKGROUND),
    BLUE("Blue  ", Color.CYAN,Background.ANSI_BLUE_BACKGROUND),
    PURPLE("Purple", Color.PURPLE,Background.ANSI_PURPLE_BACKGROUND),
    YELLOW("Yellow" , Color.YELLOW, Background.ANSI_YELLOW_BACKGROUND);

    private final Color c;
    private final String nameWithSpaces;
    private final Background b;

    DevCardCLI(String nameWithSpaces, Color c, Background background) {
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