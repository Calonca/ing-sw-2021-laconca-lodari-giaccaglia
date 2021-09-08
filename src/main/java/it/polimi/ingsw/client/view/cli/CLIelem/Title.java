package it.polimi.ingsw.client.view.cli.CLIelem;

import it.polimi.ingsw.client.view.cli.CLI;
import it.polimi.ingsw.client.view.cli.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.cli.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;

import java.util.Objects;

public class Title extends CLIelem{
    private final String titleText;
    private Color color = null;

    public Title(String title) {
        this.titleText = title;
    }

    public Title(String title, Color color){
        this.titleText = title;
        this.color = color;
    }

    @Override
    public String toString() {
        Canvas canvas = Canvas.withBorder(CLI.WIDTH ,1);
        Drawable dwl = new Drawable();

        if(Objects.isNull(color))
            color = Color.DEFAULT;

        dwl.addToCenter(CLI.WIDTH ,titleText, color, Background.DEFAULT);

        canvas.addDrawable(dwl);
        return canvas.toString();
    }


}
