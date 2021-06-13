package it.polimi.ingsw.server.controller;

/**
 * It is thrown when a resource is added to a non empty space in the depots
 */

public class EventValidationFailedException extends Exception{

    EventValidationFailedException(){super();}

    EventValidationFailedException(String message){
        super(message);
    }
}
