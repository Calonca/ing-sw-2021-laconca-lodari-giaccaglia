package it.polimi.ingsw.client.view.CLI.CLIelem;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.client.view.CLI.textUtil.StringUtil;

import java.util.Arrays;

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



    public int horizontalSize() {
        return Math.max(StringUtil.maxWidth(name),StringUtil.maxWidth(subtitle)+4);
    }

    @Override
    public String toString() {
        if (subtitle == null || subtitle.equals(" ") || subtitle.equals("")) {
            return colorIfSelected(name);
        } else {
            String formattedSub = "\n   " + colorIfSelected(" "+subtitle);
            return colorIfSelected(name) +formattedSub;
        }
    }

    private String colorIfSelected(String s){
        selected = false;
        if (selected)
            return Color.colorStringAndBackground(StringUtil.stringUntilReachingSize(s,horizontalSize()),Color.ANSI_BLACK, Background.ANSI_WHITE_BACKGROUND);
        else return StringUtil.stringUntilReachingSize(s,horizontalSize());
    }

}
