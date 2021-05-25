package it.polimi.ingsw.server.messages.messagebuilders;

public class tem {
}

/*

SETUP
(

                    gameModel.getPlayerIndex(gameModel.getCurrentPlayer()),

                    gameModel.getCurrentPlayer().getLeadersUUIDs(),

                    Util.resourcesToChooseOnSetup(gameModel.getPlayerIndex(gameModel.getCurrentPlayer())),

                    gameModel.getMatchID(),

                    gameModel.getOnlinePlayers().values().stream().map(Player::getNickName).toArray(String[]::new)
                    );

INITIAL
                    SimpleDepotsMessageBuilder.getSimpleWarehouseLeadersDepots(gameModel.getCurrentPlayer().getPersonalBoard().getSimpleWarehouseLeadersDepots()),

                    SimpleDepotsMessageBuilder.getSimpleStrongBox(gameModel.getCurrentPlayer().getPersonalBoard().getSimpleStrongBox()),

                    SimpleCardsCellsMessageBuilder.cardCellsAdapter(gameModel.getCurrentPlayer().getPersonalBoard().getVisibleCardsOnCells()),

                    gameModel.getCurrentPlayer().getSerializedFaithTrack()
 */

