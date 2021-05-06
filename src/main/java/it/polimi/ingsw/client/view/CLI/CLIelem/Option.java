package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.Color;

import java.util.Optional;

/**
 * A selectable option in a CLIView that will execute the given code when perform is called
 */
public class Option extends CLIelem{
    private boolean selected;
    private final String name;
    private final String subtitle;

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

    public Option(String name,String subtitle){
        this.name = name;
        this.selected = false;
        this.subtitle = subtitle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public String toString() {
        String correctedSubtitle;
        if (subtitle == null || subtitle.equals(" ") || subtitle.equals("")) {
            correctedSubtitle = "";
        } else {
            correctedSubtitle = "\n    " + subtitle;
        }

        String toPrint = name+correctedSubtitle;
        if (selected)
            return Color.colorString(toPrint,Color.ANSI_BLUE);
        else return toPrint;
    }

}
