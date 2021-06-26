package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleSoloActionToken extends SimpleModelElement{

    ActionTokenAsset soloActionToken;
    String soloActionTokenName;

    private final AtomicBoolean hasTokenBeenShown = new AtomicBoolean(true);


    public SimpleSoloActionToken(){}

    public SimpleSoloActionToken(String soloActionTokenName){
        this.soloActionTokenName = soloActionTokenName;
    }

    @Override
    public void update(SimpleModelElement element) {

        SimpleSoloActionToken serverActionToken = (SimpleSoloActionToken) element;
        soloActionTokenName = serverActionToken.soloActionTokenName;
        soloActionToken = ActionTokenAsset.fromName(soloActionTokenName);
        hasTokenBeenShown.set(false);

    }

    public void tokenWillBeShown(){
        hasTokenBeenShown.set(true);
    }

    public boolean hasTokenBeenShown(){
        return hasTokenBeenShown.get();
    }

    public ActionTokenAsset getSoloActionToken(){
        return soloActionToken;
    }

}
