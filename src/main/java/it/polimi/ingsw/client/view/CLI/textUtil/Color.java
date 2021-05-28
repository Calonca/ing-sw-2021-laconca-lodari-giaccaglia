package it.polimi.ingsw.client.view.CLI.textUtil;


public enum Color {

    DEFAULT(""),
    BACKSPACE("\010"),
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_STONE("\u001B[97m"),
    ANSI_SERVANT("\u001B[35m"),
    ANSI_GOLD("\u001B[33m"),
    ANSI_SHIELD("\u001B[96m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[90m");


    public static final String RESET = "\u001B[0m";

    private final String escape;

    Color(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }

    public static String colorString(String s,Color c){
        return c.escape()+s+Color.RESET;
    }

    public static String startColorStringBackground(String s,Color c,Background background){
        return background.escape()+c.escape()+s;
    }

    public static String endColorString(String s){
        return s+Color.RESET;
    }

    public static String colorStringAndBackground(String s, Color c,Background background){
        return background.escape()+c.escape()+s+Color.RESET;
    }

}
