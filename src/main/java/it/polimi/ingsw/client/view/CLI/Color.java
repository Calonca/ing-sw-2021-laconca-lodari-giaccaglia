package it.polimi.ingsw.client.view.CLI;


public enum Color {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    Color(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }

    public static String colorString(String s,Color c){
        return c.escape()+s+Color.RESET;
    }

}
