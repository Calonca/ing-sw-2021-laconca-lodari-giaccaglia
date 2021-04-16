package it.polimi.ingsw.server.controller.strategy;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.player.State;
/**
 * On turn end, the player can decide wether to play a Leader or a Normal Move. If they decide to
 * play a leader, but its not possible, that part will be skipped.
 */
public class Final extends GameStrategy {
    public State execute(GameModel gamemodel)
    {
        //MESSAGE IS SKIPLEADER OR CHOOSELEADER (0 or 1)
        int msg=0;
        if(msg==0)
            return State.IDLE;
        else
            return State.SHOWING_LEADERS_INITIAL;
    }
}
