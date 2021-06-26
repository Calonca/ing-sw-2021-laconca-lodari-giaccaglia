package it.polimi.ingsw.network.messages.servertoclient.state;

import it.polimi.ingsw.network.messages.servertoclient.ServerToClientMessage;
import it.polimi.ingsw.network.simplemodel.SimpleModelElement;

import java.util.List;
import java.util.Map;

public class ElementsInNetwork extends ServerToClientMessage {

    protected Map<Integer, List<SimpleModelElement>> playerElementsMap;
    protected List<SimpleModelElement> playerSimpleModelElements;
    protected int playerIndex;

    public ElementsInNetwork(){}

    public ElementsInNetwork(List<SimpleModelElement> commonSimpleModelElements, Map<Integer, List<SimpleModelElement>> playersElementsMap) {

        super();
        this.playerSimpleModelElements = commonSimpleModelElements;
        this.playerElementsMap = playersElementsMap;

    }

    public List<SimpleModelElement> getCommonSimpleModelElements(){
        return playerSimpleModelElements;
    }

    public Map<Integer, List<SimpleModelElement>> getPlayerElements(){
        return playerElementsMap;
    }


}
