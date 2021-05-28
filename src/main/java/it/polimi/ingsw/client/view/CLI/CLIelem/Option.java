package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.textUtil.*;

/**
 * A selectable option in a CLIView that will execute the given code when perform is called
 */
public class Option extends CLIelem{
    private boolean selected;
    private boolean isSelectable;
    private final DrawableList drawableList;

    public static Option from(String name,String subtitle,Runnable performer){
        Option option = new Option(name,subtitle);
        option.setPerformer(performer);
        return option;
    }

    public static Option from(String name, Runnable performer){
        Option option = new Option(name,"");
        option.setPerformer(performer);
        return option;
    }

    public static Option from(DrawableList drawableList,Runnable performer){
        Option option = new Option(drawableList);
        option.setPerformer(performer);
        return option;
    }

    private Option(DrawableList drawableList) {
        this.drawableList = drawableList;
        this.selected = false;
        this.isSelectable = false;
    }

    private Option(String name, String subtitle){
        drawableList = new DrawableList();
        drawableList.add(0, name);
        drawableList.add(3,subtitle);
        this.selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (!isSelectable) return;
        if (selected) {
            drawableList.get().forEach(e->e.setColor(Color.ANSI_BLACK));
            drawableList.get().forEach(e->e.setBackground(Background.ANSI_WHITE_BACKGROUND));
        } else {
            drawableList.get().forEach(e->e.setColor(Color.DEFAULT));
            drawableList.get().forEach(e->e.setBackground(Background.DEFAULT));
        }

    }

    public DrawableList toDrawableList(){
        return drawableList;
    }

    public int horizontalSize() {
        return drawableList.getWidth();
    }

    public int height(){
        return drawableList.getHeight();
    }


}
