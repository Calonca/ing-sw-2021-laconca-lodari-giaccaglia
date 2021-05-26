package it.polimi.ingsw.network.simplemodel;

public abstract class SimpleModelElement {

    private Object object;

    public SimpleModelElement(){}

    public Object getElement(){
        return object;
    }

    public void update(SimpleModelElement element){
        this.object = element.getElement();
    }

}
