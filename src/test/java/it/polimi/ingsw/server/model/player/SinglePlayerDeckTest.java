package it.polimi.ingsw.server.model.player;
import java.lang.reflect.Field;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class SinglePlayerDeckTest {

    private SinglePlayerDeck deckTest;
    private List<SoloActionToken> soloTokensTest, deckTokens;
    private static final Object[] EMPTY = {};
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
        Field field = DeckClass.getDeclaredField("actionTokens");
        field.setAccessible(true);
        deckTokens = (List<SoloActionToken>) field.get(deckTest);

        Assert.assertTrue(deckTokens.size() == soloTokensTest.size() &&
                deckTokens.containsAll(soloTokensTest) && soloTokensTest.containsAll(deckTokens));
    }

    @Test
    public void testShuffleActionTokens() throws  IllegalAccessException, IllegalArgumentException, ClassCastException, NoSuchFieldException {

        deckTest.shuffleActionTokens();
        DeckClass = deckTest.getClass();

        Field field = DeckClass.getDeclaredField("actionTokens");
        field.setAccessible(true);
        deckTokens = (List<SoloActionToken>) field.get(deckTest);

        Assert.assertTrue(deckTokens.size() == soloTokensTest.size() &&
                deckTokens.containsAll(soloTokensTest) && soloTokensTest.containsAll(deckTokens));
    }

    @Test
    public void testShowToken() throws NoSuchFieldException, IllegalAccessException {

        SoloActionToken testFirstToken = deckTest.showToken();
        DeckClass = deckTest.getClass();
        Field field = DeckClass.getDeclaredField("actionTokens");
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
    public void testActivateToken() {
        //yet to be done
    }
}