package it.polimi.ingsw.server.model.states;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.server.messages.messagebuilders.Element;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.Resource;
import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.board.Box;
import it.polimi.ingsw.server.model.player.board.PersonalBoard;
import it.polimi.ingsw.server.model.player.leaders.Leader;

import java.util.List;

/**
 *  <p>Enum class for player turn phases represented as FSM States. Game turn is divided in three macro phases.<br>
 *  Execution of {@link #INITIAL_PHASE} and {@link #FINAL_PHASE} are up to player decision,
 *  meanwhile {@link #MIDDLE_PHASE} <em>"normal action"</em> is mandatory to perform. {@link #SETUP_PHASE} is the
 *  beginning match phase, where each player receives initial resources, faith points and leader cards.</p>
 *  <ol>
 *
 *  <li>{@link #SETUP_PHASE}</li> <br>
 *
 *  <li>{@link #INITIAL_PHASE}</li>  <br>
 *
 *  <li>{@link #LEADER_END}</li>    <br>
 *
 *  <li>{@link #MIDDLE_PHASE} <em>"Normal actions"</em>
 *      <ul>
 *          <li><em>Activate the Production :</em>
 *          <li>{@link #CHOOSING_PRODUCTION}
 *          <li>{@link #CHOOSING_RESOURCE_FOR_PRODUCTION}
 *
 *          <li><em>Take Resources from Market :</em>
 *          <li>{@link #CHOOSING_MARKET_LINE}
 *          <li>{@link #CHOOSING_WHITEMARBLE_CONVERSION}
 *          <li>{@link #CHOOSING_POSITION_FOR_RESOURCES}
 *
 *          <li><em>Buy one Development Card :</em>
 *          <li>{@link #CHOOSING_DEVELOPMENT_CARD}
 *          <li>{@link #CHOOSING_RESOURCES_FOR_DEVCARD}
 *          <li>{@link #CHOOSING_POSITION_FOR_DEVCARD}</li>
 *      </ul>
 *  <li> {@link #FINAL_PHASE} <em>"Leader action"</em> </li><br>
 *  <li> {@link #LEADER_END}</li><br>
 *  <li>{@link #IDLE}</li><br>
 *  <li>{@link #END_PHASE}</li><br>
 *
 *
 * </ol>
 */
public enum State {

    /**
     * InitialOrFinalStrategy game phase where each player receives two {@link Leader LeaderCards}, faithPoints and {@link Resource Resources}
     * according to players' numbering.
     */
    SETUP_PHASE,

    /**
     * InitialOrFinalStrategy turn phase where player can either perform a <em>"Leader action"</em> or skip to {@link State#MIDDLE_PHASE}
     * for <em>Normal Action</em> performance, since <em>Leader actions</em> are optional.
     */
    INITIAL_PHASE,

    /**
     * Core turn phase where player perform a <em>Normal Action</em>.
     */
    MIDDLE_PHASE,

    /**
     * <em>Normal Action</em> phase to choose a {@link DevelopmentCard} from {@link PersonalBoard}
     * to activate the <em>Production</em>
     */
    CHOOSING_PRODUCTION,

    /**
     * <em>Normal Action</em> phase following {@link State#CHOOSING_PRODUCTION}, where player chooses
     * {@link Resource Resources} stored in any available type of {@link Box}, to complete the production process.
     */
    CHOOSING_RESOURCE_FOR_PRODUCTION,

    /**
     * <em>Normal Action</em> phase to show {@link MarketBoard} and let player choose a
     * {@link MarketLine line}(row or column) to pick {@link Marble Marbles} from.
     */
    CHOOSING_MARKET_LINE,

    /**
     * <em>Normal Action</em> phase to choose the corresponding {@link Marble#WHITE} Marble's {@link Resource}
     * when two <em>Production</em> {@link Leader Leaders} have been previously played.
     */
    CHOOSING_WHITEMARBLE_CONVERSION,

    /**
     * <em>Normal Action</em> phase to place the {@link Resource Resources} taken from the {@link MarketBoard} in the
     * <em>Wharehouse</em> depot.
     */
    CHOOSING_POSITION_FOR_RESOURCES,

    /**
     * <em>Normal Action</em> phase following {@link #MIDDLE_PHASE}
     * where player chooses one {@link DevelopmentCard DevelopmentCard} to purchase.
     */
    CHOOSING_DEVELOPMENT_CARD,

    /**
     * <em>Normal Action</em> phase following {@link #CHOOSING_DEVELOPMENT_CARD} where player chooses resources
     * among available ones in {@link PersonalBoard}.
     */
    CHOOSING_RESOURCES_FOR_DEVCARD,

    /**
     * Normal action phase following {@link State#CHOOSING_RESOURCES_FOR_DEVCARD} performed after correctly choosing the resources.
     * In this phase player chooses a position among ones in {@link PersonalBoard} to place purchased {@link DevelopmentCard}
     */
    CHOOSING_POSITION_FOR_DEVCARD,

    /**
     * Final turn phase where player can perform a <em>"Leader action"</em>
     */
    FINAL_PHASE,

    /**
     * When a Leader Turn ends, this state decides if it's a leader action before or after middle phase,
     * returning the correct next state as the game rules.
     */
    LEADER_END,


    TOKEN_PHASE,

    /**
     * Default player state during others' turn.
     */
    IDLE,

    /**
     * Ending game phase, after <em>Victory Points</em> calculation.
     */
    END_PHASE;


    /**
     * Serializes objects useful for the view corresponding to that state in json
     * @param gameModel the {@link GameModel gamemodel} of the curent game
     * @return a String in json format
     */
    public StateInNetwork toStateMessage(GameModel gameModel, List<Element> elementsToUpdate, int playerRequestingUpdate) {


        return new StateInNetwork(
                playerRequestingUpdate,
                this.toString(),
                Element.buildPlayerSimpleModelElements(gameModel, elementsToUpdate, playerRequestingUpdate),
                Element.buildCommonSimpleModelElements(gameModel, elementsToUpdate, playerRequestingUpdate));

    }


}
