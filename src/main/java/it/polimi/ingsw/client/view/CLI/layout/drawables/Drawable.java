package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An element that can be drawn in a canvas
 * Contains a colored string and a position in the canvas
 */
public class Drawable {
    private List<DrawableLine> drawableLines;
    private UUID id = UUID.randomUUID();

    public Drawable() {
        this.drawableLines = new ArrayList<>();
    }

    public Drawable(Drawable first, Drawable second) {
        drawableLines = Stream.concat(first.drawableLines.stream(),second.drawableLines.stream()).collect(Collectors.toList());
    }

    public static Drawable copyShifted(int shiftX, int shiftY, Drawable list) {
        Drawable shifted = new Drawable();
        shifted.id = list.id;
        shifted.drawableLines = list.drawableLines.stream()
                .map(d-> DrawableLine.shifted(shiftX,shiftY,d)).collect(Collectors.toList());
        return shifted;
    }

    public static Drawable selectedDrawableList(Drawable drawable){
        Drawable dList = Drawable.copyShifted(0,0, drawable);
        dList.get().forEach(e-> {
            if (e.getColor().equals(Color.DEFAULT))
                e.setColor(Color.BRIGHT_WHITE);
        });
        dList.get().forEach(e-> {
            if (e.getBackground().equals(Background.DEFAULT))
                e.setBackground(Background.ANSI_BRIGHT_BLACK_BACKGROUND);
        });
        return dList;
    }

    public Drawable(DrawableLine first, Drawable second) {
        drawableLines = Stream.concat(Stream.of(first),second.drawableLines.stream()).collect(Collectors.toList());
    }

    public Drawable(Drawable first, DrawableLine second) {
        drawableLines = Stream.concat(first.drawableLines.stream(),Stream.of(second)).collect(Collectors.toList());
    }

    public void add(DrawableLine d){
        drawableLines.add(d);
    }

    public void add(Drawable list){
        drawableLines.addAll(list.drawableLines);
    }

    /**
     * Adds the drawable line to the bottom of the drawable
     */
    public void add(int x, String s){
        drawableLines.add(new DrawableLine(x, getHeight(), s));
    }

    public void addToCenter(int width,String s){
        addToCenter(width,s,Color.DEFAULT,Background.DEFAULT);
    }

    public void addToCenter(int width,String s,Color c,Background b){
        drawableLines.add(new DrawableLine(StringUtil.startCenterWritingX(s, width+2), getHeight(), s,c,b));
    }

    public void addEmptyLine(){
        drawableLines.add(new DrawableLine(0, getHeight(), ""));
    }

    public void add(int x, String s,Color c, Background b){
        drawableLines.add(new DrawableLine(x, getHeight(), s,c,b));
    }

    public void addRight(int y, String s,Color c, Background b){
        DrawableLine dwLine = drawableLines.stream().filter(dwl->dwl.getYPos()<=y||y<=dwl.getYPos()+dwl.getHeight())
                .max(Comparator.comparing(d2->d2.getXPos()+d2.getWidth())).orElse(new DrawableLine(0,0,""));
        drawableLines.add(new DrawableLine(dwLine.getXPos()+dwLine.getWidth(), y, s,c,b));
    }

    public void addBottomRight(String s,Color c, Background b){
        drawableLines.add(new DrawableLine(getWidth(), getHeight(), s,c,b));
    }

    public List<DrawableLine> get(){
        return drawableLines;
    }

    public int getHeight(){
        int maxY = drawableLines.stream().mapToInt(l->l.getYPos()+l.getHeight()).max().orElse(0);
        int minY = drawableLines.stream().mapToInt(DrawableLine::getYPos).min().orElse(0);
        return maxY-minY;
    }

    public int getWidth(){
        int maxX = drawableLines.stream().mapToInt(l->l.getXPos()+l.getWidth()).max().orElse(0);
        int minX = drawableLines.stream().mapToInt(DrawableLine::getXPos).min().orElse(0);
        return maxX-minX;
    }

    public UUID getId() {
        return id;
    }
}
