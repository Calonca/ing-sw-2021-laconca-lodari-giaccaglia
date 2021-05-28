package it.polimi.ingsw.client.view.CLI.textUtil;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Drawable {
    private Color color;
    private Background background;
    private String string;
    private int XPos;
    private int YPos;
    private int height;
    private int width;

    public Drawable(int XPos, int YPos, String string, Color color, Background background) {
        this.color = color;
        this.background = background;
        this.string = string;
        this.XPos = XPos;
        this.YPos = YPos;
        setHeightAndWidth(string);
    }

    public Drawable(int XPos, int YPos, String string) {
        this(XPos,YPos,string,Color.DEFAULT,Background.DEFAULT);
    }

    public static Drawable shifted(int shiftX, int shiftY, Drawable d)
    {
        int XPos=d.XPos+shiftX;
        int YPos=d.YPos+shiftY;
        return new Drawable(XPos,YPos,d.string,d.color,d.background);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBackground(Background background) {
        this.background = background;
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
        return XPos;
    }

    public int getYPos() {
        return YPos;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
