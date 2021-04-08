package it.polimi.ingsw.server.model.player;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.cards.*;
import it.polimi.ingsw.server.model.player.leaders.Leader;
import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.MarketBoard;
import it.polimi.ingsw.server.model.market.MarketLine;
import it.polimi.ingsw.server.model.player.board.*;


/**
 *  <p>Enum class for player turn phases represented as FSM States. Game turn is divided in three macro phases.<br>
 *  Execution of {@link #INITIAL_PHASE} and {@link #FINAL_PHASE} are up to player decision,
 *  meanwhile {@link #MIDDLE_PHASE} <em>"normal action"</em> is mandatory to perform</p>
 *  <ol>
 *
 *  <li>{@link #INITIAL_PHASE}
 *      <ul>
 *          <li>
 *          <li>{@link #SHOWING_LEADERS_INITIAL}  <em>"Leader action"</em>
 *          <li>
 *      </ul>
 *  <li>{@link #MIDDLE_PHASE} <em>"Normal actions"</em>
 *      <ul>
 *          <li><em>Activate the Production :</em>
 *          <li>{@link #CHOOSING_CARD_FOR_PRODUCTION}
 *          <li>{@link #CHOOSING_RESOURCE_FOR_PRODUCTION}
 *
 *          <li><em>Take Resources from Market :</em>
 *          <li>{@link #SHOWING_MARKET_RESOURCES}
 *          <li>{@link #CHOOSING_WHITEMARBLE_CONVERSION}
 *          <li>{@link #CHOOSING_POSITION_FOR_RESOURCES}
 *
 *          <li><em>Buy one Development Card :</em>
 *          <li>{@link #SHOWING_CARD_SHOP}
 *          <li>{@link #CHOOSING_DEVELOPMENT_CARD}
 *          <li>
 *      </ul>
 *  <li>{@link #FINAL_PHASE}
 *      <ul>
 *          <li>
 *          <li> {@link #SHOWING_LEADERS_FINAL} <em>"Leader action"</em>
 *          <li>{@link #IDLE}
 *          <li>{@link #WINNING_STATE}
 *          <li>{@link #LOSING_STATE}
 *          <li>
 *      </ul>
 * </ol>
 */
public enum State {

    /**
     * Initial turn phase where player can either perform a <em>"Leader action"</em> or skip to {@link State#MIDDLE_PHASE}
     * for <em>Normal Action</em> performance, since <em>Leader actions</em> are optional.
     */
    INITIAL_PHASE,

    /**
     * Optional beginning turn phase to show player's current available {@link Leader Leaders}
     * for <em>Leader Action</em> performance.
     */
    SHOWING_LEADERS_INITIAL,


    /**
     * Core turn phase where player perform a <em>Normal Action</em>.
     */
    MIDDLE_PHASE,

    /**
     * <em>Normal Action</em> phase to choose a {@link DevelopmentCard} from {@link PersonalBoard}
     * to activate the <em>Production</em>
     */
    CHOOSING_CARD_FOR_PRODUCTION,

    /**
     * <em>Normal Action</em> phase following {@link State#CHOOSING_CARD_FOR_PRODUCTION}, where player chooses
     * {@link Resource Resources} stored in any available type of {@link Box}, to complete the production process.
     */
    CHOOSING_RESOURCE_FOR_PRODUCTION,

    /**
     * <em>Normal Action</em> phase to show {@link MarketBoard} and let player choose a
     * {@link MarketLine line}(row or column) to pick {@link Marble Marbles} from.
     */
    SHOWING_MARKET_RESOURCES,

    /**
     * <em>Normal Action</em> phase to choose the corresponding {@link Marble#WHITE}Marble's {@link Resource}
     * when two <em>Production</em> {@link Leader Leaders} have been previously played.
     */
    CHOOSING_WHITEMARBLE_CONVERSION,

    /**
     * <em>Normal Action</em> phase to place the {@link Resource Resources} taken from the {@link MarketBoard} in the
     * <em>Wharehouse</em> depot.
     */
    CHOOSING_POSITION_FOR_RESOURCES,

    /**
     * <em>Normal Action</em> phase to show {@link DevelopmentCard DevelopmentCards} from {@link CardShop},
     */
    SHOWING_CARD_SHOP,

    /**
     * <em>Normal Action</em> phase following {@link State#SHOWING_CARD_SHOP}
     * where player chooses one {@link DevelopmentCard DevelopmentCard} to purchase.
     */
    CHOOSING_DEVELOPMENT_CARD,


    /**
     * Final turn phase where player can either perform a <em>"Leader action"</em> or finish his turn,
     * since <em>Leader actions</em> are optional.
     * {@link #IDLE} is the default state during other player's turn, whereas {@link State#WINNING_STATE} and
     * {@link State#LOSING_STATE} are the game ending phases.
     */
    FINAL_PHASE,

    /**
     * Optional ending turn phase to show player's current available {@link Leader Leaders}
     * for <em>Leader Action</em> performance.
     */
    SHOWING_LEADERS_FINAL,

    /**
     * Default player state during others' turn.
     */
    IDLE,

    /**
     * Winning player phase when game ends, after <em>Victory Points</em> calculation.
     */
    WINNING_STATE,

    /**
     * Losing players phase when game ends, after <em>Victory Points</em> calculation.
     */
    LOSING_STATE,

}
