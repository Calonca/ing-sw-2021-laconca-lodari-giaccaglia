package it.polimi.ingsw.client.view.CLI.textUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DrawableList {
    private List<Drawable> drawables;
    private UUID id = UUID.randomUUID();

    public DrawableList() {
        this.drawables = new ArrayList<>();
    }

    public DrawableList(List<Drawable> drawables) {
        this.drawables = drawables;
    }

    public DrawableList(DrawableList first, DrawableList second) {
        drawables = Stream.concat(first.drawables.stream(),second.drawables.stream()).collect(Collectors.toList());
    }

    public static DrawableList copyShifted(int shiftX, int shiftY, DrawableList list) {
        DrawableList shifted = new DrawableList();
        shifted.drawables = list.drawables.stream()
                .map(d->Drawable.shifted(shiftX,shiftY,d)).collect(Collectors.toList());
        return shifted;
    }

    public static DrawableList selectedDrawableList(DrawableList drawableList){
        DrawableList dList = DrawableList.copyShifted(0,0,drawableList);
        dList.get().forEach(e->e.setColor(Color.ANSI_BLACK));
        dList.get().forEach(e->e.setBackground(Background.ANSI_WHITE_BACKGROUND));
        return dList;
    }

    public DrawableList(Drawable first, DrawableList second) {
        drawables = Stream.concat(Stream.of(first),second.drawables.stream()).collect(Collectors.toList());
    }

    public DrawableList(DrawableList first, Drawable second) {
        drawables = Stream.concat(first.drawables.stream(),Stream.of(second)).collect(Collectors.toList());
    }

    public void add(Drawable d){
        drawables.add(d);
    }

    public void add(DrawableList list){
        drawables.addAll(list.drawables);
    }

    public void add(int x, String s){
        drawables.add(new Drawable(x, getHeight(), s));
    }

    public void addToCenter(int canvasWidth,String s){
        drawables.add(new Drawable(StringUtil.startCenterWritingX(s, canvasWidth+2), getHeight(), s));
    }

    public void shift(int shiftX,int shiftY) {
        drawables = drawables.stream()
                .map(d->Drawable.shifted(shiftX,shiftY,d)).collect(Collectors.toList());
    }

    public void addEmptyLine(){
        drawables.add(new Drawable(0, getHeight(), ""));
    }

    public void add(int x, String s,Color c, Background b){
        drawables.add(new Drawable(x, getHeight(), s,c,b));
    }

    public List<Drawable> get(){
        return drawables;
    }

    public int getHeight(){
        return drawables.stream().mapToInt(Drawable::getHeight).sum();
    }

    public int getWidth(){
        return drawables.stream().mapToInt(Drawable::getWidth).max().orElse(0);
    }

    public UUID getId() {
        return id;
    }
}
