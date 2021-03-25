package it.polimi.ingsw.server.model.player.board;

/**
 * It is thrown when a resource is added to a non empty space in the depots
 */

public class ResourceNotEmptyException extends Exception{
    ResourceNotEmptyException(){
    }

    ResourceNotEmptyException(String message){
        super(message);
    }
}
