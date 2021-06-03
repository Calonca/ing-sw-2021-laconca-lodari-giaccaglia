package it.polimi.ingsw.client.view.CLI.layout;

import it.polimi.ingsw.client.view.CLI.layout.drawables.Canvas;
import it.polimi.ingsw.client.view.CLI.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.CLI.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;

import java.util.Optional;

/**
 * A selectable option in a CLIView that will execute the given code when perform is called
 */
public class Option extends GridElem{


    public enum VisMode {
        NUMBER_TO_BOTTOM {
            @Override
            public Drawable getDrawable(int idx, Drawable drawable) {
                Drawable dw = Drawable.copyShifted(0,0,drawable);
                DrawableLine dwl = Option.numberLine(0,dw.getHeight(), idx);
                dw.addToCenter(dw.getWidth(),dwl.getString(),dwl.getColor(),dwl.getBackground());
                return dw;
            }
        },
        NUMBER_TO_BOTTOM_SPACED {
            @Override
            public Drawable getDrawable(int idx, Drawable drawable) {
                Drawable dw = Drawable.copyShifted(0,0,drawable);
                DrawableLine dwl = Option.numberLine(0,dw.getHeight(), idx);
                dw.addToCenter(dw.getWidth(),dwl.getString(),dwl.getColor(),dwl.getBackground());
                dw.addEmptyLine();
                return dw;
            }
        },
        NUMBER_TO_LEFT {
            @Override
            public Drawable getDrawable(int idx, Drawable drawable) {
                DrawableLine index = Option.numberLine(0,0,idx);
                Drawable dw = Drawable.copyShifted(index.getWidth(),0,drawable);
                dw.add(index);
                return dw;
            }
        }
        ,NO_NUMBER {
            @Override
            public Drawable getDrawable(int idx, Drawable drawable) {
                Drawable dw = Drawable.copyShifted(0,0,drawable);
                dw.addEmptyLine();
                return  dw;
            }
        };

        public abstract Drawable getDrawable(int idx, Drawable drawable);

    }


    private VisMode mode;
    private boolean selected;
    private boolean enabled;
    private Runnable performer;
    private final Drawable drawable;
    private final Drawable selectedDrawable;

    public static Option from(String name){
        return Option.from(fromNameAndSub(name,""),()->{});
    }

    public static Option noNumber(String name){
        Option o = Option.from(fromNameAndSub(name,""),()->{});
        o.setMode(VisMode.NO_NUMBER);
        return o;
    }


    public static Option from(String name, Runnable performer){
        Option option = Option.from(name,"",performer);
        option.setPerformer(performer);
        return option;
    }

    public static Option from(String name,String subtitle,Runnable performer){
        Option option = Option.from(fromNameAndSub(name,subtitle),performer);
        option.setPerformer(performer);
        return option;
    }

    public static Option noNumber(Drawable drawable){
        Option o = Option.from(drawable,()->{});
        o.setMode(VisMode.NO_NUMBER);
        return o;
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
        mode = VisMode.NUMBER_TO_LEFT;
        this.drawable = normalDwl;
        selectedDrawable = selectedDwl;
        this.selected = false;
        this.setEnabled(true);
    }

    private static Drawable fromNameAndSub(String name,String subtitle){
        Drawable drawable = new Drawable();
        drawable.add(0, name);
        if (!subtitle.equals("")) {
            drawable.add(3,subtitle);
        }
        return drawable;
    }

    public void setMode(VisMode mode) {
        this.mode = mode;
    }

    public void setPerformer(Runnable performer) {
        this.performer = performer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private Drawable getDrawableWithoutNumber(){
        if (selected)
            return selectedDrawable;
        else
            return drawable;
    }


    public void perform(){
        if (performer!=null)
            performer.run();
    }

    private static DrawableLine numberLine(int x, int y, int idx){
        return new DrawableLine(x,y,idx+": ", Color.OPTION, Background.DEFAULT);
    }


    @Override
    public int getLastIndex() {
        return getFirstIdx();
    }

    @Override
    public Optional<Option> getOptionWithIndex(int i) {
        return Optional.ofNullable((getFirstIdx()==i&&!mode.equals(VisMode.NO_NUMBER))?this:null);
    }

    @Override
    public void addToCanvas(Canvas canvas, int x, int y) {
        Drawable toDisplay = mode.getDrawable(getFirstIdx(), getDrawableWithoutNumber());
        Drawable copy = Drawable.copyShifted(x,y,toDisplay);
        canvas.addDrawable(copy);
    }

    @Override
    public int getMinWidth() {
        return mode.getDrawable(getFirstIdx(), drawable).getWidth();
    }

    @Override
    public int getMinHeight() {
        return mode.getDrawable(getFirstIdx(), drawable).getHeight();
    }


}
