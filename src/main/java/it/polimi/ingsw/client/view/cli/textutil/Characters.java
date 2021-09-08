package it.polimi.ingsw.client.view.cli.textutil;


public enum Characters {

    VERT_DIVIDER('║'),
    BOTTOM_LEFT_DIV('╚'),
    BOTTOM_RIGHT_DIV('╝'),
    TOP_LEFT_DIV('╔'),
    TOP_RIGHT_DIV('╗'),
    HOR_DIVIDER('═');


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
