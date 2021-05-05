package it.polimi.ingsw.client.view.CLI.CLIelem;

public class Title extends CLIelem{
    private String title;

    public Title(String title) {
        this.title = title;
    }


    public void setTitle(String title) {
        if (!this.title.equals(title)){
            this.title = title;
            cli.update();
        }
    }

    @Override
    public String toString() {
        return title;
    }
}
