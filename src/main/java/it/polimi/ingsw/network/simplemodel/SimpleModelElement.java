package it.polimi.ingsw.network.simplemodel;

public abstract class SimpleModelElement {

    public SimpleModelElement(){}

    public abstract Object getElement();

    public abstract void update(SimpleModelElement element);

}
