package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.drawables.Drawable;

/**
 * A selectable option in a CLIView that will execute the given code when perform is called
 */
public class Option {
    private boolean selected;
    private Runnable performer;
    private final Drawable drawable;
    private final Drawable selectedDrawable;

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

    public static Option from(Drawable drawable, Runnable performer){
        Option option = new Option(drawable, Drawable.selectedDrawableList(drawable));
        option.setPerformer(performer);
        return option;
    }

    public static Option from(Drawable normalDwl, Drawable selectedDwl, Runnable performer){
        Option option = new Option(normalDwl,selectedDwl);
        option.setPerformer(performer);
        return option;
    }

    private Option(Drawable normalDwl, Drawable selectedDwl) {
        this.drawable = normalDwl;
        selectedDrawable = selectedDwl;
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
        drawable = new Drawable();
        drawable.add(0, name);
        drawable.add(3,subtitle);
        this.selected = false;
        selectedDrawable = Drawable.selectedDrawableList(drawable);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Drawable toDrawableList(){
        if (selected)
            return selectedDrawable;
        else
            return drawable;
    }

    public int horizontalSize() {
        return drawable.getWidth();
    }

    public int height(){
        return drawable.getHeight();
    }

}
