package it.polimi.ingsw.server.model.cards;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class DevelopmentCardDeck {

    private int level;
    private DevelopmentCardColor color;
    private List<DevelopmentCard> deck;
    private int deckCapacity;
    private int deckSize;

    public static DevelopmentCardDeck initializeDeck (String configFilePath) {

        Gson gson = new Gson();
        DevelopmentCardDeck test = null;

        try {
            String DevelopmentCardDeckConfig = Files.readString(Path.of(configFilePath), StandardCharsets.US_ASCII);
            test = gson.fromJson(DevelopmentCardDeckConfig, DevelopmentCardDeck.class);

        } catch (IOException e) {
            System.out.println("Error while class initialization with json config file at path: " + configFilePath);
            e.printStackTrace();
        }

        if (test != null) {
            Collections.shuffle(test.deck);
        }
        return test;
    }

    public boolean isDeckEmpty(){
        return deckSize == 0;
    }

    public DevelopmentCard getCard(){
        deckSize--;
        return deck.remove(deckSize);
    }

    public DevelopmentCardColor getColor(){
        return color;
    }











}
