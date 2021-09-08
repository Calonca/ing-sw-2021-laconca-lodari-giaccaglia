package it.polimi.ingsw.client.view.cli.layout.drawables;

import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.network.assets.marbles.MarbleAsset;

public enum MarbleCLI {

    WHITE(Color.BRIGHT_WHITE, ResourceCLI.TO_CHOOSE),
    BLUE(Color.SHIELD, ResourceCLI.SHIELD),
    GRAY(Color.BRIGHT_BLACK,ResourceCLI.STONE),
    YELLOW(Color.GOLD, ResourceCLI.GOLD),
    PURPLE(Color.SERVANT, ResourceCLI.SERVANT),
    RED(Color.RED, ResourceCLI.FAITH);

    private final Color c;
    private final ResourceCLI resourceCLI;

    MarbleCLI(Color c , ResourceCLI resourceCLI) {
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


    public static int width(){
        return WHITE.toBigDrawable().getWidth();
    }
    public static int height(){
        return WHITE.toBigDrawable().getHeight();
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
