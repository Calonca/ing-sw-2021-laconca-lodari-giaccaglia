package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import javafx.util.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class DevelopmentCardAsset {
    private UUID cardId;
    private DevelopmentCard developmentCard;
    private Pair<Path, Path> cardPaths;

    public DevelopmentCardAsset(){}

    public DevelopmentCardAsset(DevelopmentCard developmentCard, String front, String back, UUID cardId){
        this.developmentCard = developmentCard;

        this.cardPaths = new Pair<>(Paths.get(front), Paths.get(front));
        this.cardId = cardId;
    }

  /*  public Pair<Path, Path> getCardPaths(){
        return cardPaths;
    } */

    public Pair<Path,Path> getCardPaths(){
        return cardPaths;
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    public UUID getCardId(){
        return cardId;
    }

}
