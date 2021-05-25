package it.polimi.ingsw.client.view.CLI.textUtil;


public enum Background {

    DEFAULT(""),
    ANSI_BLACK_BACKGROUND("\u001B[40m"),
    ANSI_RED_BACKGROUND("\u001B[41m"),
    ANSI_GREEN_BACKGROUND("\u001B[42m"),
    ANSI_YELLOW_BACKGROUND("\u001B[43m"),
    ANSI_BLUE_BACKGROUND("\u001B[44m"),
    ANSI_PURPLE_BACKGROUND("\u001B[45m"),
    ANSI_CYAN_BACKGROUND("\u001B[46m"),
    ANSI_WHITE_BACKGROUND("\u001B[47m");

    private final String escape;

    Background(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }

}
