package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleSoloActionToken extends SimpleModelElement{

    ActionTokenAsset soloActionToken;
    UUID soloActionTokenId;

    private final AtomicBoolean hasTokenBeenShown = new AtomicBoolean(true);


    public SimpleSoloActionToken(){}

    public SimpleSoloActionToken(UUID soloActionTokenId){
        this.soloActionTokenId = soloActionTokenId;
    }

    @Override
    public void update(SimpleModelElement element) {

        SimpleSoloActionToken serverActionToken = (SimpleSoloActionToken) element;
        soloActionTokenId = serverActionToken.soloActionTokenId;
        soloActionToken = ActionTokenAsset.fromUUID(soloActionTokenId);
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
