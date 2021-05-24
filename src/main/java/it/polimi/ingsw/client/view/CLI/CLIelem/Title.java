package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.textUtil.Canvas;

public class Title extends CLIelem{
    private String title;

    public Title(String title) {
        this.title = title;
    }


    public void setTitle(String title) {
        if (!this.title.equals(title)){
            this.title = title;
            cli.refreshCLI();
        }
    }

    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,1);
        canvas.drawWithDefaultColor(CLI.getCenterX()-title.length()/2,0,title);
        return canvas.toString();
    }

    @Override
    public int horizontalSize() {
        return title.length();
    }
}
