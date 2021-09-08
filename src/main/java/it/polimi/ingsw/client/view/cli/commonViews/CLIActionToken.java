package it.polimi.ingsw.client.view.cli.commonViews;

import it.polimi.ingsw.client.view.cli.CLIelem.Title;
import it.polimi.ingsw.client.view.cli.CLIelem.body.CanvasBody;
import it.polimi.ingsw.client.view.cli.layout.Option;
import it.polimi.ingsw.client.view.cli.layout.drawables.Drawable;
import it.polimi.ingsw.client.view.cli.layout.drawables.DrawableLine;
import it.polimi.ingsw.client.view.cli.layout.drawables.Timer;
import it.polimi.ingsw.client.view.cli.layout.recursivelist.Column;
import it.polimi.ingsw.client.view.cli.textutil.Background;
import it.polimi.ingsw.client.view.cli.textutil.Color;
import it.polimi.ingsw.client.view.abstractview.ViewBuilder;
import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;
import it.polimi.ingsw.network.simplemodel.SimpleSoloActionToken;

import java.beans.PropertyChangeEvent;

public class CLIActionToken extends ViewBuilder{


    /**
     * This method shows the retrieved action token's information on the screen
     */
    @Override
    public void run() {

        Title title = new Title("Here is this turn Action Token! . Seconds left for viewing : 4", Color.GREEN);

        getCLIView().setTitle(title.toString());

        Column grid = new Column();
        Option tokenBox = Option.noNumber(buildActionTokenBox());
        grid.addElem(tokenBox);
        CanvasBody body = CanvasBody.centered(grid);

        getCLIView().setBody(body);

        getCLIView().show();
        getCLIView().runOnInput("", () -> {});
        Timer.showSecondsOnCLI(getCLIView(), "Here is this turn Action Token! . Seconds left for viewing : ", 3);
        getClient().changeViewBuilder(getClient().getSavedViewBuilder());


    }

    /**
     * This method is used to synchronize with the listener's update from server
     */
    public static void showActionTokenBeforeTransition(){

        SimpleSoloActionToken soloActionToken = getSimpleModel().getPlayerCache(0).getElem(SimpleSoloActionToken.class).get();

        if(!soloActionToken.hasTokenBeenShown()){
            soloActionToken.tokenWillBeShown();
            if(getClient().isCLI()) {
                getClient().changeViewBuilder(new CLIActionToken());
            }
        }
    }


    /**
     * This method fills the screen with fancy ASCII
     * @return the corresponding action token's Drawable
     */
    private static Drawable buildActionTokenBox(){

        SimpleSoloActionToken actionToken = getSimpleModel().getPlayerCache(0).getElem(SimpleSoloActionToken.class).orElseThrow();
        ActionTokenAsset tokenAsset = actionToken.getSoloActionToken();

        String[] words = tokenAsset.getEffectDescription().split(" ");
        int lettersCounter = 0;
        int yPos = 3;
        int xPos = 2;

        Drawable drawable= new Drawable();


        drawable.add(0,"╔════════════════════════════════╗");
        drawable.add(0,"║                                ║");
        drawable.add(new DrawableLine(8, 1, "Action Token", Color.BRIGHT_GREEN, Background.DEFAULT));
        drawable.add(0,"║ ────────────────────────────── ║");

        drawable.add(0,"║                                ║");

        for(String word : words){
            lettersCounter = lettersCounter + word.length() + 1;

            if(lettersCounter>30){
                drawable.add(0,"║                                ║");
                yPos++;
                xPos = 2;
                lettersCounter = word.length();
            }

            drawable.add(new DrawableLine(xPos, yPos, word + " ", Color.YELLOW, Background.DEFAULT));
            xPos = xPos + word.length() + 1;

        }
        drawable.add(0,"║ ────────────────────────────── ║");
        drawable.add(0,"║                                ║");
        drawable.add(0,"╚════════════════════════════════╝");

        return drawable;

    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // emtpy method because there's no need to handle property changes
    }


}
