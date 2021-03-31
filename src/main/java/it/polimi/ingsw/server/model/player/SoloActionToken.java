package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.DevelopmentCardColor;
import it.polimi.ingsw.server.model.GameModel;


public enum SoloActionToken {

    DISCARD2GREEN{

        final DevelopmentCardColor color = DevelopmentCardColor.YELLOW;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCardFromShop(color, gameModel, 2);
        }
    },

    DISCARD2BLUE {

        final DevelopmentCardColor color = DevelopmentCardColor.YELLOW;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCardFromShop(color, gameModel, 2);
        }
    },

    DISCARD2YELLOW{

        final DevelopmentCardColor color = DevelopmentCardColor.YELLOW;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCardFromShop(color, gameModel, 2);
        }
    },

    DISCARD2PURPLE{
        final DevelopmentCardColor color = DevelopmentCardColor.PURPLE;

        @Override
        public void applyEffect(GameModel gameModel) {
            discardCardFromShop(color, gameModel, 2);

        }
    },

    SHUFFLE_ADD1FAITH{
        @Override
        public void applyEffect(GameModel gameModel) {
            gameModel.addFaithPointToOtherPlayers();
            gameModel.shuffleSoloDeck();
        }
    },

    ADD2FAITH {
        @Override
        public void applyEffect(GameModel gameModel) {
            gameModel.addFaithPointToOtherPlayers();
            gameModel.addFaithPointToOtherPlayers();
        }
    };

    public abstract void applyEffect(GameModel gameModel);

    public void discardCardFromShop(DevelopmentCardColor color, GameModel model, int amount){
        model.discardCardFromShop(color, amount);
    }
}

