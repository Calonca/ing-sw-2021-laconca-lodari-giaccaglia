package it.polimi.ingsw.network.simplemodel;

import it.polimi.ingsw.network.assets.tokens.ActionTokenAsset;

import java.util.UUID;

public class SimpleSoloActionToken extends SimpleModelElement{

    ActionTokenAsset soloActionToken;
    UUID soloActionTokenId;

    public SimpleSoloActionToken(){}

    public SimpleSoloActionToken(UUID soloActionTokenId){
        this.soloActionTokenId = soloActionTokenId;
    }

    @Override
    public void update(SimpleModelElement element) {

        SimpleSoloActionToken serverActionToken = (SimpleSoloActionToken) element;
        soloActionTokenId = serverActionToken.soloActionTokenId;

        soloActionToken = ActionTokenAsset.fromUUID(soloActionTokenId);

    }

    public ActionTokenAsset getSoloActionToken(){
        return soloActionToken;
    }

}
