package it.polimi.ingsw.client.view.CLI;

/**
 * It gets thrown when the CLI is switching viewBuilder before the user writes the requires input
 */
public class ChangingViewBuilderBeforeTakingInput extends Exception{

    public ChangingViewBuilderBeforeTakingInput(String error){
        super(error);
    }
    public ChangingViewBuilderBeforeTakingInput(){
        super();
    }
}
