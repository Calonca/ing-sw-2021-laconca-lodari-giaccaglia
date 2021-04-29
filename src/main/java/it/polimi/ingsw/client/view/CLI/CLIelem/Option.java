package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.Color;

/**
 * A selectable option in a CLIView that will execute the given code when perform is called
 */
public class Option extends CLIelem{
    private boolean selected;
    private String name;
    private String subtitle;
    private Runnable r;

    public Option(String name,String subtitle,Runnable r){
        this.name = name;
        this.selected = false;
        this.r = r;
        this.subtitle = subtitle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void perform() {
        r.run();
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
