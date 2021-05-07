package it.polimi.ingsw.network.assets;

import javafx.util.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class CardAsset {
    private final UUID cardId;
    private final Pair<Path, Path> cardPaths;

    public CardAsset(String front, String back, UUID cardId){
        this.cardPaths = new Pair<>(Paths.get(front), Paths.get(back));
        this.cardId = cardId;
    }

    public Pair<Path,Path> getCardPaths(){
        return cardPaths;
    }
    public UUID getCardId(){
        return cardId;
    }
}
