package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ResourceCLI {
    GOLD(ResourceAsset.GOLD,"GO", "Gold", "  Gold   ", Color.GOLD, Background.ANSI_GOLD_BACKGROUND),
    SERVANT(ResourceAsset.SERVANT, "SE", "Servant", " Servant ",Color.SERVANT,Background.ANSI_PURPLE_BACKGROUND),
    SHIELD(ResourceAsset.SHIELD, "SH", "Shield", " Shield  ",Color.SHIELD,Background.ANSI_CYAN_BACKGROUND),
    STONE(ResourceAsset.STONE, "ST", "Stone", "  Stone  ",Color.STONE,Background.ANSI_BRIGHT_BLACK_BACKGROUND),
    FAITH(ResourceAsset.FAITH, "FA", "Faith",      "  Faith  ",Color.RED,Background.ANSI_RED_BACKGROUND),
    TO_CHOSE(ResourceAsset.TOCHOOSE, "??", "To choose", "To choose",Color.BRIGHT_WHITE,Background.ANSI_BLACK_BACKGROUND),
    EMPTY(ResourceAsset.EMPTY, "EE", "Empty", "  Empty  ",Color.DEFAULT,Background.DEFAULT);


    private final ResourceAsset res;
    private final String symbol;
    private final String nameWithSpaces;
    private final String fullName;
    private final Color c;
    private final Background b;

    ResourceCLI(ResourceAsset res, String symbol, String fullName, String nameWithSpaces, Color c, Background b) {
        this.res = res;
        this.symbol = symbol;
        this.fullName = fullName;
        this.nameWithSpaces = nameWithSpaces;
        this.c = c;
        this.b = b;
    }

    public Drawable toBigDrawableList(boolean isSelected){
        Background back = isSelected?b:Background.DEFAULT;
        Color cl = c;
        Drawable dwList = new Drawable();
        dwList.add(0,"------------",cl,back);
        dwList.add(0,"|          |",cl,back);
        dwList.add(0,"|    "+ symbol +"    |",cl,back);
        dwList.add(0,"|          |",cl,back);
        dwList.add(0,"|----------|",cl,back);
        dwList.add(0,"| "+ nameWithSpaces +"|",cl,back);
        dwList.add(0,"------------",cl,back);
        if (isSelected)
            return Drawable.selectedDrawableList(dwList);
        else
            return dwList;
    }

    public static List<Drawable> toList(){
        return Arrays.stream(ResourceCLI.values()).limit(4).map(res->res.toBigDrawableList(false)).collect(Collectors.toList());
    }

    public static int width(){
        return TO_CHOSE.toBigDrawableList(false).getWidth();
    }
    public static int height(){
        return TO_CHOSE.toBigDrawableList(false).getHeight();
    }

    public ResourceAsset getRes() {
        return res;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getNameWithSpaces() {
        return nameWithSpaces;
    }

    public String getFullName() {
        return fullName;
    }

    public Color getC() {
        return c;
    }

    public Background getB() {
        return b;
    }

    public static ResourceCLI fromInt(int i){
        ResourceCLI[] val = ResourceCLI.values();
        return i>=val.length||i<0 ? EMPTY: val[i];

    }

    public static ResourceCLI fromAsset(ResourceAsset asset){
         return Arrays.stream(ResourceCLI.values()).filter(r->r.getRes().equals(asset)).findFirst().orElse(ResourceCLI.EMPTY);
    }



}
