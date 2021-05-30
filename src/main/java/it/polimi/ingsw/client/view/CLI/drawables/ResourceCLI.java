package it.polimi.ingsw.client.view.CLI.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.assets.resources.ResourceAsset;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ResourceCLI {
    GOLD(ResourceAsset.GOLD,"G",       "  Gold   ", Color.GOLD, Background.ANSI_GOLD_BACKGROUND),
    SERVANT(ResourceAsset.SERVANT, "S"," Servant ",Color.SERVANT,Background.ANSI_GOLD_BACKGROUND),
    SHIELD(ResourceAsset.SHIELD, "H",  " Shield  ",Color.SHIELD,Background.ANSI_GOLD_BACKGROUND),
    STONE(ResourceAsset.STONE, "T",    "  Stone  ",Color.STONE,Background.ANSI_GOLD_BACKGROUND),
    TO_CHOSE(ResourceAsset.EMPTY, "C", "To choose",Color.RIGHT_WHITE,Background.ANSI_WHITE_BACKGROUND),
    FAITH(ResourceAsset.FAITH, "F",    "  Faith  ",Color.DEFAULT,Background.DEFAULT),
    EMPTY(ResourceAsset.EMPTY, "E",    "  Empty  ",Color.DEFAULT,Background.DEFAULT);


    private final ResourceAsset res;
    private final String simbol;
    private final String fullName;
    private final Color c;
    private final Background b;

    ResourceCLI(ResourceAsset res, String symbol, String fullName, Color c, Background b) {
        this.res = res;
        this.simbol = symbol;
        this.fullName = fullName;
        this.c = c;
        this.b = b;
    }

    public Drawable toBigDrawableList(boolean isSelected){
        Background back = isSelected?b:Background.DEFAULT;
        Color cl = isSelected?Color.BLACK :c;
        Drawable dwList = new Drawable();
        dwList.add(0,"-----------",cl,back);
        dwList.add(0,"|         |",cl,back);
        dwList.add(0,"|    "+simbol+"    |",cl,back);
        dwList.add(0,"|         |",cl,back);
        dwList.add(0,"-----------",cl,back);
        dwList.add(0,"|"+fullName+"|",cl,back);
        dwList.add(0,"-----------",cl,back);
        return dwList;
    }

    public static List<Drawable> toList(){
        return Arrays.stream(ResourceCLI.values()).limit(4).map(res->res.toBigDrawableList(false)).collect(Collectors.toList());
    }

    public static int width(){
        return 2+TO_CHOSE.toBigDrawableList(false).getWidth();
    }
    public static int height(){
        return TO_CHOSE.toBigDrawableList(false).getHeight();
    }

    public ResourceAsset getRes() {
        return res;
    }

    public String getSimbol() {
        return simbol;
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

    public static ResourceCLI fromAsset(ResourceAsset asset){
        int rNum = asset.getResourceNumber();
        ResourceCLI[] val = ResourceCLI.values();
        return rNum>val.length||rNum<0 ? EMPTY: val[rNum];
    }



}
