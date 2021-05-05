package it.polimi.ingsw.client.view.CLI.CLIelem;

public class RunnableWithString{
    private String string;
    private Runnable r;

    public RunnableWithString(){
    }

    public String getString() {
        return string;
    }

    public Runnable getR() {
        return r;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setR(Runnable r) {
        this.r = r;
    }

    public void runCode(){
        r.run();
    }
}
