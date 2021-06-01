package it.polimi.ingsw.server.messages.messagebuilders;

import com.google.gson.GsonBuilder;
import it.polimi.ingsw.client.simplemodel.SimpleModel;
import it.polimi.ingsw.network.jsonUtils.JsonUtility;
import it.polimi.ingsw.network.messages.servertoclient.state.StateInNetwork;
import it.polimi.ingsw.network.simplemodel.*;
import it.polimi.ingsw.server.controller.Match;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.states.State;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage.elementAdapter;

public class tem {

    public static List<Element> elements = new ArrayList<>();



    public static void main(String[] args) {


        List<String> players = new ArrayList<>();
        players.add("player1");
        players.add("player2");
        GameModel model = new GameModel(players, false, new Match(2));
      //  CardAssetsContainer.setCardAssetsContainer(Deserializator.networkDevCardsAssetsDeserialization());
        elements.add(Element.SimplePlayerLeaders);
        //elements.add(Element.TestElem);
         elements.add(Element.SimpleCardShop);
        elements.add(Element.SimpleFaithTrack);
        State state = State.SETUP_PHASE;
        StateInNetwork stateInNetwork = state.toStateMessage(model, elements);
        String stateInNet = stateInNetwork.serialized();




        StateInNetwork deserializedStateInNetworl = JsonUtility.deserializeFromString(stateInNet, StateInNetwork.class, new GsonBuilder().registerTypeAdapterFactory(elementAdapter()).create());
        List<SimpleModelElement> commonlist = deserializedStateInNetworl.getCommonSimpleModelElements();
        List<SimpleModelElement> playerList = deserializedStateInNetworl.getPlayerSimpleModelElements();
        int player = stateInNetwork.getPlayerNumber();
        SimpleModel simpleModel = new SimpleModel(2);

        for(SimpleModelElement element : commonlist){
            simpleModel.updateSimpleModelElement(element.getClass().getSimpleName(), element);
        }

        for(SimpleModelElement element : playerList){
            simpleModel.getPlayerCache(player).updateSimpleModelElement(element);
        }

        int ciao = 5;


    }
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
 *
 */

