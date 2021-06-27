package it.polimi.ingsw.client.view.CLI.textUtil;


public enum Color {

    DEFAULT(""),
    BACKSPACE("\010"),
    ANSI_RESET("\u001B[0m"),
    DISABLED("\u001B[2m"),

    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[97m"),



    BRIGHT_BLACK("\u001B[90m"),
    BRIGHT_RED("\u001B[91m"),
    BRIGHT_GREEN("\u001B[92m"),
    BRIGHT_YELLOW("\u001B[93m"),
    BRIGHT_BLUE("\u001B[94m"),
    BRIGHT_PURPLE("\u001B[95m"),
    BRIGHT_CYAN("\u001B[96m"),
    BRIGHT_WHITE("\u001B[97m"),

    OPTION("\u001B[94m"),
    GOLD("\u001B[33m"),
    SERVANT("\u001B[35m"),
    SHIELD("\u001B[96m"),
    STONE("\u001B[37m");


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
