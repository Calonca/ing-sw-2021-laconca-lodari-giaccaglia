package it.polimi.ingsw.client.view.CLI.layout.drawables;

import it.polimi.ingsw.client.view.CLI.textUtil.Background;
import it.polimi.ingsw.client.view.CLI.textUtil.Color;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MarbleCLI {

    WHITE(MarbleAsset.WHITE,Color.BRIGHT_WHITE, ResourceCLI.TO_CHOOSE),
    BLUE(MarbleAsset.BLUE,Color.SHIELD, ResourceCLI.SHIELD),
    GRAY(MarbleAsset.GRAY,Color.BRIGHT_BLACK,ResourceCLI.STONE),
    YELLOW(MarbleAsset.YELLOW,Color.GOLD, ResourceCLI.GOLD),
    PURPLE(MarbleAsset.PURPLE,Color.SERVANT, ResourceCLI.SERVANT),
    RED(MarbleAsset.RED,Color.RED, ResourceCLI.FAITH);

    private final MarbleAsset res;
    private final Color c;
    private final ResourceCLI resourceCLI;

    MarbleCLI(MarbleAsset res, Color c, ResourceCLI resourceCLI) {
        this.res = res;
        this.c = c;
        this.resourceCLI = resourceCLI;
    }

    public Drawable toBigDrawable(){
        return toBigDrawableCustomXPos(0);
    }

    public Drawable toBigDrawableCustomXPos(int xPos){
        Drawable dwList = new Drawable();
        dwList.add(xPos,"    █      ",c,Background.DEFAULT);
        dwList.add(xPos," ███████   ",c,Background.DEFAULT);
        dwList.add(xPos,"█████████  ",c,Background.DEFAULT);
        dwList.add(xPos," ███████   ",c,Background.DEFAULT);
        dwList.add(xPos,"    █      ",c,Background.DEFAULT);
        return dwList;
    }

    public Drawable toSmallDrawable(){

        Drawable dwList = new Drawable();
        dwList.add(0,"      ██   ",c,Background.DEFAULT);
        dwList.add(0,"    ██████ ",c,Background.DEFAULT);
        dwList.add(0,"      ██   ",c,Background.DEFAULT);
        return dwList;
    }

    public static List<Drawable> toList(){
        return Arrays.stream(MarbleCLI.values()).limit(6).map(MarbleCLI::toBigDrawable).collect(Collectors.toList());
    }

    public static int width(){
        return WHITE.toBigDrawable().getWidth();
    }
    public static int height(){
        return WHITE.toBigDrawable().getHeight();
    }

    public MarbleAsset getRes() {
        return res;
    }

    public Color getC() {
        return c;
    }

    public static MarbleCLI fromAsset(MarbleAsset asset){
        int rNum = asset.ordinal();
        MarbleCLI[] val = MarbleCLI.values();
        return rNum>val.length ? MarbleCLI.WHITE: val[rNum];
    }

    public ResourceCLI fromMarbleCLI(){
        return resourceCLI;
    }

}
