package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.cli.textutil.StringUtil;

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
    private boolean selected;

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
        shifted.selected = list.isSelected();
        return shifted;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public static Color getColorWithSelected(Drawable dwl,DrawableLine d){
        return (d.getColor().equals(Color.DEFAULT)&&dwl.isSelected())?Color.BRIGHT_WHITE: d.getColor();
    }

    public static Background getBackWithSelected(Drawable dwl,DrawableLine d){
        return (d.getBackground().equals(Background.DEFAULT)&&dwl.isSelected())? Background.ANSI_BRIGHT_BLACK_BACKGROUND:d.getBackground();
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
        int minY = getMinY();
        return maxY-minY;
    }

    public int getMinY() {
        return drawableLines.stream().mapToInt(DrawableLine::getYPos).min().orElse(0);
    }

    public int getWidth(){
        int maxX = drawableLines.stream().mapToInt(l->l.getXPos()+l.getWidth()).max().orElse(0);
        int minX = getMinX();
        return maxX-minX;
    }

    public int getMinX() {
        return drawableLines.stream().mapToInt(DrawableLine::getXPos).min().orElse(0);
    }

    public UUID getId() {
        return id;
    }
}
