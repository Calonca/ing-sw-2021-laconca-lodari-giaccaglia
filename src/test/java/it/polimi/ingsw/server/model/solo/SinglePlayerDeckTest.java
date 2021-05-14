package it.polimi.ingsw.server.model.solo;
import java.lang.reflect.Field;

import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.cards.CardShop;
import it.polimi.ingsw.server.model.cards.DevelopmentCardColor;
import it.polimi.ingsw.server.model.cards.DevelopmentCardDeck;
import org.junit.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class SinglePlayerDeckTest {

    private SinglePlayerDeck deckTest;
    private List<SoloActionToken> soloTokensTest, deckTokens;
    private Class<?> DeckClass;
    private Field field;

    @Before
    public void setUp() {
        deckTest = new SinglePlayerDeck();
        soloTokensTest = Arrays.asList(SoloActionToken.values());
    }

    @Test
    public void testActionTokens() throws IllegalAccessException, IllegalArgumentException, ClassCastException, NoSuchFieldException {

        DeckClass = deckTest.getClass();
        field = DeckClass.getDeclaredField("actionTokens");
        field.setAccessible(true);
        deckTokens = (List<SoloActionToken>) field.get(deckTest);

        Assert.assertTrue(deckTokens.size() == soloTokensTest.size() &&
                deckTokens.containsAll(soloTokensTest) && soloTokensTest.containsAll(deckTokens));
    }

    @Test
    public void testShuffleActionTokens() throws  IllegalAccessException, IllegalArgumentException, ClassCastException, NoSuchFieldException {

        deckTest.shuffleActionTokens();
        DeckClass = deckTest.getClass();

        field = DeckClass.getDeclaredField("actionTokens");
        field.setAccessible(true);
        deckTokens = (List<SoloActionToken>) field.get(deckTest);

        Assert.assertTrue(deckTokens.size() == soloTokensTest.size() &&
                deckTokens.containsAll(soloTokensTest) && soloTokensTest.containsAll(deckTokens));
    }

    @Test
    public void testShowToken() throws NoSuchFieldException, IllegalAccessException {

        SoloActionToken testFirstToken = deckTest.showToken();
        DeckClass = deckTest.getClass();
        field = DeckClass.getDeclaredField("actionTokens");
        field.setAccessible(true);
        deckTokens = (List<SoloActionToken>) field.get(deckTest);
        assertEquals(testFirstToken, deckTokens.get(0));

        deckTest.shuffleActionTokens();
        //same test after shuffle
        testFirstToken = deckTest.showToken();
        DeckClass = deckTest.getClass();
        field = DeckClass.getDeclaredField("actionTokens");
        field.setAccessible(true);
        deckTokens = (List<SoloActionToken>) field.get(deckTest);
        assertEquals(testFirstToken, deckTokens.get(0));

    }

    @Test
    public void testActivateToken() throws NoSuchFieldException, IllegalAccessException {
        for(int i=0; i<20; i++){
            testToken();
        }
    }

    private void testToken() throws NoSuchFieldException, IllegalAccessException {


        List<String> players = new ArrayList<>();
        players.add("testPlayer");
        boolean isSinglePlayer = true;
        GameModel gameModelTest = new GameModel(players, isSinglePlayer,null);
        SoloActionToken tokenTest = gameModelTest.showSoloActionToken();
        gameModelTest.activateSoloActionToken();

        Class<?> gameModelTestClass = gameModelTest.getClass();
        field = gameModelTestClass.getDeclaredField("cardShop");
        field.setAccessible(true);
        CardShop cardShopTest = (CardShop) field.get(gameModelTest);

        Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>> devDecks;
        Class<?> cardShopClassTest = cardShopTest.getClass();
        field = cardShopClassTest.getDeclaredField("devDecks");
        field.setAccessible(true);
        Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>> devDecksTest = (Map<DevelopmentCardColor, Map<Integer, DevelopmentCardDeck>>) field.get(cardShopTest);

        System.out.println("token Tested : " + tokenTest.name());

        if(tokenTest == SoloActionToken.DISCARD2GREEN) {
            assertEquals(2, devDecksTest.get(DevelopmentCardColor.GREEN).get(1).getDeckSize());
        }
        else if(tokenTest == SoloActionToken.DISCARD2YELLOW){
            assertEquals(2, devDecksTest.get(DevelopmentCardColor.YELLOW).get(1).getDeckSize());
        }

        else if(tokenTest == SoloActionToken.DISCARD2BLUE){
            assertEquals(2, devDecksTest.get(DevelopmentCardColor.BLUE).get(1).getDeckSize());
        }

        else if(tokenTest == SoloActionToken.DISCARD2PURPLE){
            assertEquals(2, devDecksTest.get(DevelopmentCardColor.PURPLE).get(1).getDeckSize());
        }

        else if(tokenTest == SoloActionToken.SHUFFLE_ADD1FAITH){
            assertEquals(1, gameModelTest.getLorenzoPosition(gameModelTest.getCurrentPlayer()));
        }

        else if(tokenTest == SoloActionToken.ADD2FAITH){
            assertEquals(2, gameModelTest.getLorenzoPosition(gameModelTest.getCurrentPlayer()));
            testShuffleActionTokens();
        }



    }
}