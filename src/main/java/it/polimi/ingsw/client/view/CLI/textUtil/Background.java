package it.polimi.ingsw.client.view.CLI.textUtil;


public enum Background {

    DEFAULT(""),
    ANSI_ULTRA_BLACK_BACKGROUND("\u001B[7m"),
    ANSI_BLACK_BACKGROUND("\u001B[40m"),
    ANSI_RED_BACKGROUND("\u001B[41m"),
    ANSI_GREEN_BACKGROUND("\u001B[42m"),
    ANSI_YELLOW_BACKGROUND("\u001B[43m"),
    ANSI_GOLD_BACKGROUND("\u001B[43m"),
    ANSI_BLUE_BACKGROUND("\u001B[44m"),
    ANSI_PURPLE_BACKGROUND("\u001B[45m"),
    ANSI_CYAN_BACKGROUND("\u001B[46m"),
    ANSI_WHITE_BACKGROUND("\u001B[47m"),
    ANSI_BRIGHT_BLACK_BACKGROUND("\u001B[100m"),
    ANSI_BRIGHT_RED_BACKGROUND("\u001B[101m"),
    ANSI_BRIGHT_GREEN_BACKGROUND("\u001B[102m"),
    ANSI_BRIGHT_YELLOW_BACKGROUND("\u001B[103m"),
    ANSI_BRIGHT_GOLD_BACKGROUND("\u001B[103m"),
    ANSI_BRIGHT_BLUE_BACKGROUND("\u001B[1010m"),
    ANSI_BRIGHT_PURPLE_BACKGROUND("\u001B[105m"),
    ANSI_BRIGHT_CYAN_BACKGROUND("\u001B[106m"),
    ANSI_BRIGHT_WHITE_BACKGROUND("\u001B[107m");

    private final String escape;

    Background(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }

}
