package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;

public class DrawableLine {
    private final Color color;
    private final Background background;
    private final String string;
    private final int xPos;
    private final int yPos;
    private int height;
    private int width;

    public DrawableLine(int xPos, int yPos, String string, Color color, Background background) {
        this.color = color;
        this.background = background;
        this.string = string;
        this.xPos = xPos;
        this.yPos = yPos;
        setHeightAndWidth(string);
    }

    public DrawableLine(int xPos, int yPos, String string) {
        this(xPos,yPos,string,Color.DEFAULT,Background.DEFAULT);
    }

    public static DrawableLine shifted(int shiftX, int shiftY, DrawableLine d)
    {
        int xPos=d.xPos +shiftX;
        int yPos=d.yPos+shiftY;
        return new DrawableLine(xPos,yPos,d.string,d.color,d.background);
    }

    private void setHeightAndWidth(String s){
        height = StringUtil.maxHeight(s);
        width = StringUtil.maxWidth(s);
    }

    public Color getColor() {
        return color;
    }

    public Background getBackground() {
        return background;
    }

    public String getString() {
        return string;
    }



    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
