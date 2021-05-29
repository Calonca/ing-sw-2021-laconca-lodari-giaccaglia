package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.textUtil.*;

/**
 * A selectable option in a CLIView that will execute the given code when perform is called
 */
public class Option {
    private boolean selected;
    private Runnable performer;
    private final DrawableList drawableList;
    private final DrawableList selectedDrawableList;

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
        Option option = new Option(drawableList,DrawableList.selectedDrawableList(drawableList));
        option.setPerformer(performer);
        return option;
    }

    public static Option from(DrawableList normalDwl,DrawableList selectedDwl,Runnable performer){
        Option option = new Option(normalDwl,selectedDwl);
        option.setPerformer(performer);
        return option;
    }

    private Option(DrawableList normalDwl, DrawableList selectedDwl) {
        this.drawableList = normalDwl;
        selectedDrawableList = selectedDwl;
        this.selected = false;
    }

    public void perform(){
        if (performer!=null)
            performer.run();
    }

    public void setPerformer(Runnable performer) {
        this.performer = performer;
    }

    private Option(String name, String subtitle){
        drawableList = new DrawableList();
        drawableList.add(0, name);
        drawableList.add(3,subtitle);
        this.selected = false;
        selectedDrawableList = DrawableList.selectedDrawableList(drawableList);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public DrawableList toDrawableList(){
        if (selected)
            return selectedDrawableList;
        else
            return drawableList;
    }

    public int horizontalSize() {
        return drawableList.getWidth();
    }

    public int height(){
        return drawableList.getHeight();
    }

}
