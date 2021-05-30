package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;

public class Title extends CLIelem{
    private String title;

    public Title(String title) {
        this.title = title;
    }


    public void setTitle(String title) {
        if (!this.title.equals(title)){
            this.title = title;
            cli.show();
        }
    }

    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,1);
        Drawable dwl = new Drawable();
        dwl.addToCenter(CLI.width,title);
        canvas.addDrawable(dwl);
        return canvas.toString();
    }


}
