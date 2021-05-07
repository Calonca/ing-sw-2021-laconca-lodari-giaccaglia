package it.polimi.ingsw.network.assets;

import it.polimi.ingsw.server.model.cards.DevelopmentCard;
import javafx.util.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class DevelopmentCardAsset extends CardAsset{

    private final DevelopmentCard developmentCard;

    public DevelopmentCardAsset(DevelopmentCard developmentCard, String front, String back, UUID cardId){
        super(front, back, cardId);
        this.developmentCard = developmentCard;
    }

    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

}
