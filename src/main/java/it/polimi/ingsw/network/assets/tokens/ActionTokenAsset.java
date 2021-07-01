package it.polimi.ingsw.network.assets.tokens;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
/**
 *  <p>Enum class for <em>Solo Action Tokens</em> for the <em>Solo Mode</em>.<br>
 *  Each Token represents an effect applied once it is picked from the SinglePlayerDeck.</p>
 *  <ul>
 *  <li>{@link #DISCARD2GREEN}
 *  <li>{@link #DISCARD2BLUE}
 *  <li>{@link #DISCARD2YELLOW}
 *  <li>{@link #DISCARD2PURPLE}
 *  <li>{@link #SHUFFLE_ADD1FAITH}
 *  <li>{@link #ADD2FAITH}
 *  </ul>
 */
public enum ActionTokenAsset {

    /**
     * When this effect is applied 2 green DevelopmentCards are discarded
     * from the belonging DevelopmentCardDeck  following the applyEffect rules.
     */
    DISCARD2GREEN(Paths.get("assets/tokens/DISCARD2GREEN.png")){

        String effectDescription = "" +
                "2 Green Development Cards have been discarded from the bottom of the grid, " +
                "from the lowest level to the highest " +
                "(if there are no more Level I cards, Level II cards have been discarded, and so on)";

        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }
    },

    /**
     * When this effect is applied 2 blue DevelopmentCards are discarded
     * from the belonging DevelopmentCardDeck following the applyEffect rules.
     */
    DISCARD2BLUE(Paths.get("assets/tokens/DISCARD2BLUE.png")){

        String effectDescription = "" +
                "2 Blue Development Cards have been discarded from the bottom of the grid, " +
                "from the lowest level to the highest " +
                "(if there are no more Level I cards, Level II cards have been discarded, and so on)";

        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }
    },

    /**
     * When this effect is applied 2 yellow DevelopmentCards are discarded
     * from the belonging DevelopmentCardDeck following the
     * discardCardFromShop() rules.
     */
    DISCARD2YELLOW(Paths.get("assets/tokens/DISCARD2YELLOW.png")){

        String effectDescription = "" +
                "2 Yellow Development Cards have been discarded from the bottom of the grid, " +
                "from the lowest level to the highest " +
                "(if there are no more Level I cards, Level II cards have been discarded, and so on)";


        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }
    },

    /**
     * When this effect is applied 2 purple DevelopmentCards are discarded
     * from the belonging DevelopmentCardDeck following the
     * discardCardFromShop() rules.
     */
    DISCARD2PURPLE(Paths.get("assets/tokens/DISCARD2PURPLE.png")){

        String effectDescription = "" +
                "2 Purple Development Cards have been discarded from the bottom of the grid, " +
                "from the lowest level to the highest " +
                "(if there are no more Level I cards, Level II cards have been discarded, and so on)";

        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }
    },

    /**
     * When this effect is applied the lorenzoPiece of the FaithTrack representing the
     * <em>Faith Marker</em> of <em>Lorenzo il Magnifico</em>, is moved forward by 1 spaces through the
     * GameModel method  addFaithPointToOtherPlayers().<br><br>
     * Finally the SinglePlayerDeck is shuffled by calling the shuffleSoloDeck() method, in order to create a new order in stack.
     */
    SHUFFLE_ADD1FAITH(Paths.get("assets/tokens/SHUFFLE_ADD1FAITH.png")){

        String effectDescription = "" +
                "The Black Cross token has been moved forward by 1 space and " +
                "all the Solo Action tokens have been shuffled!";

        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }
    },

    /**
     * When this effect is applied the lorenzoPiece of the FaithTrack, representing the Faith Marker
     * of <em>Lorenzo il Magnifico</em>, is moved forward by 2 spaces by calling the GameModel method
     * addFaithPointToOtherPlayers() addFaithPointToOtherPlayers}.
     */
    ADD2FAITH(Paths.get("assets/tokens/ADD2FAITH.png")){

        String effectDescription = "The Black Cross token is moved forward by 2 spaces!";

        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }

    },

    EMPTY(Paths.get("assets/tokens/TOKEN_BACK.png")){

        String effectDescription = "Invalid token request!";

        @Override
        public String getEffectDescription(){
            return effectDescription;
        }

        @Override
        public void setEffectDescription(String effectDescription){
            this.effectDescription = effectDescription;
        }

    };

    private Path actionTokenAssetPath;

    private final Path backOfActionTokenAssetPath = Paths.get("assets/tokens/TOKEN_BACK.png");

    private static final ActionTokenAsset[] vals = ActionTokenAsset.values();

    ActionTokenAsset(){}

    ActionTokenAsset(final Path actionTokenAssetPath) {
        this.actionTokenAssetPath = actionTokenAssetPath;
    }

    public void setFrontPath(Path path){
        this.actionTokenAssetPath = path;
    }

    public Path getFrontPath(){
        return actionTokenAssetPath;
    }

    public abstract String getEffectDescription();

    public abstract void setEffectDescription(String effectDescription);

    public static ActionTokenAsset fromName(String tokenName){
        return Arrays.stream(vals)
                .filter(token -> token.name().equals(tokenName))
                .findFirst()
                .orElse(EMPTY);
    }


    public boolean isDiscardingCard(){
        return
                this.equals(DISCARD2BLUE)||
                this.equals(DISCARD2GREEN)||
                this.equals(DISCARD2YELLOW)||
                this.equals(DISCARD2PURPLE);
    }

}
