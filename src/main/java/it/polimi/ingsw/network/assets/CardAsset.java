package it.polimi.ingsw.network.assets;

import javafx.util.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardAsset {

    private UUID cardId;
    private Pair<Path, Path> activeCardPaths;
    private Pair<Path, Path> inactiveCardPaths;

    public CardAsset(){}

    public CardAsset(String frontActive, String backActive, String frontInactive, String backInactive,  UUID cardId){
        this.activeCardPaths = new Pair<>(Paths.get(frontActive), Paths.get(backActive));
        this.inactiveCardPaths = new Pair<>(Paths.get(frontInactive), Paths.get(backInactive));
        this.cardId = cardId;
    }

    public Pair<Path,Path> getCardPaths(){
        return activeCardPaths;
    }

    public Pair<Path,Path> getActiveCardPaths(){
        return activeCardPaths;
    }


    public Pair<Path,Path> getInactiveCardPaths(){
        return inactiveCardPaths;
    }


    public UUID getCardId(){
        return cardId;
    }

}
