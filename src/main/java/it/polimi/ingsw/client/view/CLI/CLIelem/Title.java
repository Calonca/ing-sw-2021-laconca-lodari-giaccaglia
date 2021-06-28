package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.CLI;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import java.util.Objects;

public class Title extends CLIelem{
    private String title;
    private Color color = null;
    private int height = 1;

    public Title(String title) {
        this.title = title;
    }

    public Title(String title, Color color){
        this.title = title;
        this.color = color;
    }


    public void setTitle(String title) {
        if (!this.title.equals(title)){
            this.title = title;
            cli.show();
        }
    }

    public Title(String title, Color color, int height) {
            this.title = title;
            this.height = height;
            this.color = color;
    }

    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.width,1);
        Drawable dwl = new Drawable();

        if(Objects.isNull(color))
            color = Color.DEFAULT;

        dwl.addToCenter(CLI.width,title, color, Background.DEFAULT);

        canvas.addDrawable(dwl);
        return canvas.toString();
    }


}
