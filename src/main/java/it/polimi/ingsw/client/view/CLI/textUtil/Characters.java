package it.polimi.ingsw.client.view.CLI.textUtil;


public enum Characters {

    VERT_DIVIDER('|'),
    HOR_DIVIDER('-');


    private final char escape;

    Characters(char escape) {
        this.escape = escape;
    }
    public char character(){
        return escape;
    }

    public String getString(){
        return String.valueOf(character());
    }

    public String repeated(int n){
        return String.valueOf(character()).repeat(n);
    }

}
